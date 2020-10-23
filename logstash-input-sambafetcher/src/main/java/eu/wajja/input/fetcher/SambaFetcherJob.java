package eu.wajja.input.fetcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.msdtyp.SecurityDescriptor;
import com.hierynomus.msdtyp.SecurityInformation;
import com.hierynomus.msdtyp.ace.ACE;
import com.hierynomus.msfscc.fileinformation.FileAllInformation;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Advapi32Util.Account;

import eu.wajja.input.fetcher.config.Command;
import eu.wajja.input.fetcher.config.MetadataConstant;
import eu.wajja.input.fetcher.elasticsearch.ElasticSearchService;

public class SambaFetcherJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(SambaFetcherJob.class);

    private List<String> excludedUrls;
    private ElasticSearchService elasticSearchService;
    private Long sleep;
    private Long maxDocuments;
    private Long documentCount;
    private String smbDomain;
    private String shareFolder;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        Consumer<Map<String, Object>> consumer = (Consumer<Map<String, Object>>) dataMap.get(SambaFetcher.PROPERTY_CONSUMER);

        this.smbDomain = (String) dataMap.get(SambaFetcher.PROPERTY_SMB_DOMAIN);
        String smbFolder = (String) dataMap.get(SambaFetcher.PROPERTY_SMB_FOLDER);
        
        String smbHost = (String) dataMap.get(SambaFetcher.PROPERTY_SMB_HOST);
        String smbPassword = (String) dataMap.get(SambaFetcher.PROPERTY_SMB_PASSWORD);
        String smbUsername = (String) dataMap.get(SambaFetcher.PROPERTY_SMB_USERNAME);

        this.sleep = (Long) dataMap.get(SambaFetcher.PROPERTY_SLEEP);
        this.maxDocuments = (Long) dataMap.get(SambaFetcher.PROPERTY_MAX_DOCUMENTS);
        this.documentCount = 0l;

        List<String> hostnames = (List<String>) dataMap.get(SambaFetcher.PROPERTY_ELASTIC_HOSTNAMES);
        excludedUrls = (List<String>) dataMap.get(SambaFetcher.PROPERTY_EXCLUDE);

        String username = dataMap.getString(SambaFetcher.PROPERTY_ELASTIC_USERNAME);
        String password = dataMap.getString(SambaFetcher.PROPERTY_ELASTIC_PASSWORD);

        elasticSearchService = new ElasticSearchService(hostnames, username, password);

        SMBClient client = new SMBClient();
        String index = "logstash_smb_fetcher_" + Base64.getEncoder().encodeToString(smbFolder.getBytes()).toLowerCase();
        elasticSearchService.checkIndex(index);

        try (Connection connection = client.connect(smbHost)) {

            AuthenticationContext ac = new AuthenticationContext(smbUsername, smbPassword.toCharArray(), smbDomain);
            Session session = connection.authenticate(ac);

            String startPath = "";
            this.shareFolder = smbFolder;

            if (smbFolder.contains("\\")) {

                String[] splitPath = smbFolder.split("\\\\");

                startPath = smbFolder.substring(splitPath[0].length() + 1);
                this.shareFolder = splitPath[0];

            }

            LOGGER.info("startPath : {}", startPath);
            LOGGER.info("shareFolder : {}", this.shareFolder);

            try (DiskShare diskShare = (DiskShare) session.connectShare(this.shareFolder)) {
                parseFile(diskShare, index, startPath, consumer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        client.close();

        LOGGER.info("Finished Filesystem job");

    }

    private void parseFile(DiskShare diskShare, String index, String fileName, Consumer<Map<String, Object>> consumer) {

        if (this.documentCount >= this.maxDocuments) {
            return;
        }
        
        if(excludedUrls != null && excludedUrls.stream().anyMatch(ex -> fileName.matches(ex))){
            LOGGER.info("Skipping : {}", fileName);
            return;
        }

        FileAllInformation fileInformation = diskShare.getFileInformation(fileName);

        if (!fileInformation.getStandardInformation().isDirectory()) {

            LOGGER.info("File : {}", fileName);
            Long modifiedDate = fileInformation.getBasicInformation().getChangeTime().toEpochMillis();

            if (!elasticSearchService.documentExists(index, fileName, modifiedDate)) {

                File smbFile = diskShare.openFile(fileName, EnumSet.of(AccessMask.GENERIC_READ), null, SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_OPEN, null);

                try (InputStream stream = smbFile.getInputStream()) {

                    byte[] bytesArray = IOUtils.toByteArray(stream);
                    Map<String, Object> metadata = new HashMap<>();

                    mapSMBSecurity(diskShare, fileName, metadata);

                    metadata.put(MetadataConstant.METADATA_COMMAND, Command.ADD.toString());
                    metadata.put(MetadataConstant.METADATA_REFERENCE, Base64.getEncoder().encodeToString(fileName.getBytes()));
                    metadata.put(MetadataConstant.METADATA_URL, "\\\\" + smbDomain + "\\" + this.shareFolder + "\\" + fileName);
                    metadata.put(MetadataConstant.METADATA_EPOCH, modifiedDate);
                    metadata.put(MetadataConstant.METADATA_CONTENT, Base64.getEncoder().encodeToString(bytesArray));

                    consumer.accept(metadata);
                    this.documentCount++;

                    elasticSearchService.addDocument(index, fileName, modifiedDate);

                } catch (Exception e) {
                    LOGGER.error("Failed to get file content", e);
                }
            }

        } else {

            LOGGER.info("Folder : {}", fileName);

            List<String> files = diskShare.list(fileName).stream().filter(x -> !x.getFileName().endsWith(".")).map(FileIdBothDirectoryInformation::getFileName).collect(Collectors.toList());
            files.stream().forEach(fName -> parseFile(diskShare, index, fileName + "/" + fName, consumer));

        }

        try {
            Thread.sleep(sleep);

        } catch (InterruptedException e) {
            LOGGER.info("Failed to sleep", e);
            Thread.currentThread().interrupt();
        }
    }

    private void mapSMBSecurity(DiskShare diskShare, String fileName, Map<String, Object> metadata) {

        try {

            Set<SecurityInformation> securityInfo = new HashSet<>();
            securityInfo.add(SecurityInformation.DACL_SECURITY_INFORMATION);
            SecurityDescriptor ss = diskShare.getSecurityInfo(fileName, securityInfo);

            if (ss.getDacl() != null) {

                List<Account> accounts = ss.getDacl().getAces().stream().map(ACE::getSid).map(sid -> Advapi32Util.getAccountBySid(sid.toString())).collect(Collectors.toList());
                List<String> aclGroups = new ArrayList<>();
                List<String> aclUsers = new ArrayList<>();

                for (Account account : accounts) {

                    if (account.accountType == 2) {
                        aclGroups.add(account.name);
                    } else {
                        aclUsers.add(account.name);
                    }
                }

                metadata.put(MetadataConstant.METADATA_ACL_GROUPS, aclGroups);
                metadata.put(MetadataConstant.METADATA_ACL_USERS, aclUsers);
            }

        } catch (Exception e) {
            LOGGER.error("Failed to retrieve permissions", e);
        }
    }

}
