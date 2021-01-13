# Logstash Filesystem Input Plugin

This plugin will crawl a local drive and download all information ready to be indexed.
The data is sent as a byte[] and all headers are added to the hashmap send to elastic.
No processing of the data is done so it can be processed , as is, by the logstash filters.
A tracking mecanism is built using an elasticsearch instance. 

### Properties

| Properties  | Mandatory | Default | Description |
| ------------- | ------------- | ------------- | ------------- |
| cron  | true  | NA  | Cron to define when the job starts |
| paths  | true  | NA  | full system path |
| exclude  | false  | empty array  | Array of regexes where matched url is not crawled and not indexed |
| threads  | false  | 1  | Number of concurrent threads |
| elasticsearchHostnames  | true  | NA  | Array of elasticsearch hostnames |
| elasticsearchUsername  | false  | NA  | Elasticsearch username |
| elasticsearchPassword  | false  | NA  | Elasticsearch password |
| proxyHost  | false  | NA  | Proxy Host |
| proxyPort  | false  | NA  | Proxy Port |
| proxyUser  | false  | NA  | Proxy User |
| proxyPass  | false  | NA  | Proxy Pass |
