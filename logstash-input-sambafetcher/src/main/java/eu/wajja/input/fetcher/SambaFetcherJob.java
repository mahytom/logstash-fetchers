package eu.wajja.input.fetcher;

import java.io.IOException;
import java.io.InputStream;
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

import com.hierynomus.msdtyp.ACL;
import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.msdtyp.SecurityDescriptor;
import com.hierynomus.msdtyp.SecurityInformation;
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

import eu.wajja.input.fetcher.elasticsearch.ElasticSearchService;

public class SambaFetcherJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(SambaFetcherJob.class);

    private List<String> excludedUrls;
    private ElasticSearchService elasticSearchService;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        Consumer<Map<String, Object>> consumer = (Consumer<Map<String, Object>>) dataMap.get(SambaFetcher.PROPERTY_CONSUMER);

        String smbDomain = (String) dataMap.get(SambaFetcher.PROPERTY_SMB_DOMAIN);
        String smbFolder = (String) dataMap.get(SambaFetcher.PROPERTY_SMB_FOLDER);
        String smbHost = (String) dataMap.get(SambaFetcher.PROPERTY_SMB_HOST);
        String smbPassword = (String) dataMap.get(SambaFetcher.PROPERTY_SMB_PASSWORD);
        String smbUsername = (String) dataMap.get(SambaFetcher.PROPERTY_SMB_USERNAME);

        List<String> hostnames = (List<String>) dataMap.get(SambaFetcher.PROPERTY_ELASTIC_HOSTNAMES);
        excludedUrls = (List<String>) dataMap.get(SambaFetcher.PROPERTY_EXCLUDE);

        String username = dataMap.getString(SambaFetcher.PROPERTY_ELASTIC_USERNAME);
        String password = dataMap.getString(SambaFetcher.PROPERTY_ELASTIC_PASSWORD);

        elasticSearchService = new ElasticSearchService(hostnames, username, password);

        SMBClient client = new SMBClient();
        String index = Base64.getEncoder().encodeToString(smbFolder.getBytes()).toLowerCase();
        elasticSearchService.checkIndex(index);

        try (Connection connection = client.connect(smbHost)) {

            AuthenticationContext ac = new AuthenticationContext(smbUsername, smbPassword.toCharArray(), smbDomain);
            Session session = connection.authenticate(ac);

            try (DiskShare diskShare = (DiskShare) session.connectShare(smbFolder)) {

                List<String> files = diskShare.list("").stream().filter(x -> !x.getFileName().endsWith(".")).map(FileIdBothDirectoryInformation::getFileName).collect(Collectors.toList());
                files.stream().forEach(fileName -> parseFile(diskShare, index, fileName, consumer));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        client.close();

        LOGGER.info("Finished Filesystem job");

    }

    private void parseFile(DiskShare diskShare, String index, String fileName, Consumer<Map<String, Object>> consumer) {

        if (diskShare.fileExists(fileName)) {

            LOGGER.info("File : {}", fileName);

            FileAllInformation fileInformation = diskShare.getFileInformation(fileName);
            Long modifiedDate = fileInformation.getBasicInformation().getChangeTime().toEpochMillis();

            if (!elasticSearchService.documentExists(index, fileName, modifiedDate) && (excludedUrls == null || excludedUrls.stream().noneMatch(ex -> fileName.endsWith(ex)))) {

                File smbFile = diskShare.openFile(fileName, EnumSet.of(AccessMask.GENERIC_READ), null, SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_OPEN, null);

                try (InputStream stream = smbFile.getInputStream()) {

                    Set<SecurityInformation> securityInfo = new HashSet<>();
                    securityInfo.add(SecurityInformation.DACL_SECURITY_INFORMATION);
                    securityInfo.add(SecurityInformation.SACL_SECURITY_INFORMATION);

                    SecurityDescriptor ss = diskShare.getSecurityInfo(fileName, securityInfo);

                    ACL dacl = ss.getDacl();
                    ACL sacl = ss.getSacl();

                    Map<String, Object> metadata = new HashMap<>();

                    if (dacl != null) {
                        metadata.put("aclGroups", dacl.getAces().stream().map(ace -> ace.getSid()).map(sid -> Advapi32Util.getAccountBySid(sid.toString()).name).collect(Collectors.toList()));
                    }

                    if (sacl != null) {
                        metadata.put("aclUsers", sacl.getAces().stream().map(ace -> ace.getSid()).map(sid -> Advapi32Util.getAccountBySid(sid.toString()).name).collect(Collectors.toList()));
                    }

                    metadata.put("reference", Base64.getEncoder().encodeToString(fileName.getBytes()));
                    metadata.put("url", fileName);
                    metadata.put("epochSecond", modifiedDate);
                    metadata.put("path", fileName);
                    metadata.put("content", IOUtils.toByteArray(stream));

                    consumer.accept(metadata);

                    elasticSearchService.addDocument(index, fileName, modifiedDate);

                } catch (Exception e) {
                    LOGGER.error("Failed to get file content", e);
                }
            }

        } else if (diskShare.folderExists(fileName)) {

            LOGGER.info("Folder : {}", fileName);

            List<String> files = diskShare.list(fileName).stream().filter(x -> !x.getFileName().endsWith(".")).map(FileIdBothDirectoryInformation::getFileName).collect(Collectors.toList());
            files.stream().forEach(fName -> parseFile(diskShare, index, fileName + "/" + fName, consumer));

        }

    }

}
