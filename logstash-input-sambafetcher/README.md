# Logstash Samba Input Plugin

This Plugin reads a Samba Share
No processing of the data is done so it can be processed , as is, by the logstash filters.
A tracking mecanism is built using an elasticsearch instance. 

### Properties

| Properties  | Mandatory | Default | Description |
| ------------- | ------------- | ------------- | ------------- |
| cron  | true  | NA  | Cron to define when the job starts |
| smbHost  | true  | NA  | full SMB endpoint |
| smbUsername  | true  | NA  | Username to log into SMB Share |
| smbPassword  | true  | NA  | Password to log in to SMB Share |
| smbDomain  | true  | NA  | Domain to Access |
| smbFolder  | true  | NA  | Folder to access on SMB Share |
| sleep  | false  | NA  | Sleep between calls |
| maxDocuments  | false  | 1000  | Max number of pages to crawl |
| elasticsearchHostnames  | true  | NA  | Array of elasticsearch hostnames |
| elasticsearchUsername  | false  | NA  | Elasticsearch username |
| elasticsearchPassword  | false  | NA  | Elasticsearch password |
| exclude  | false  | empty array  | Array of regexes where matched url is not crawled and not indexed |