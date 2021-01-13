# Logstash Web Fetchers Plugin


## Web Plugin

This plugin will crawl websites and download all information ready to be indexed.
The data is sent as a byte[] and all headers are added to the hashmap send to elastic.
A tracking mecanism is built using an elasticsearch instance
Downloading is done with browserless/chrome (https://hub.docker.com/r/browserless/chrome/)

### Configuration

```
input {
  webfetcher {
	urls => ["http://yousite.eu/"]
	exclude => ["css","js"]
	dataFolder => "/mypath/tofolder/"
	threads => 10
	refreshInterval => 300
	proxyHost => "proxy"
	proxyPort => 80
	proxyUser => "proxyUser"
	proxyPass => "proxyPass"
  }
}
```

### Properties

| Properties  | Mandatory | Default | Description |
| ------------- | ------------- | ------------- | ------------- |
| urls  | true  | NA  | Array of urls |
| cron  | true  | NA  | Cron to define when the job starts |
| chromeDrivers  | true  | NA  | Array of browserless/chrome endpoints |
| elasticsearchHostnames  | true  | NA  | Array of elasticsearch hostnames |
| elasticsearchUsername  | false  | NA  | Elasticsearch username |
| elasticsearchPassword  | false  | NA  | Elasticsearch password |
| excludeData  | false  | empty array  | Array if regexes where matched url is crawled but not indexed |
| excludeLink  | false  | empty array  | Array if regexes where matched url is not crawled and not indexed |
| includeLink  | false  | empty array  | Array if regexes where only matched url is crawled |
| timeout  | false  | 8000  | Timeout or url |
| maxdepth  | false  | NA  | Max site depth to crawl |
| maxpages  | false  | 1000  | Max number of pages to crawl |
| refreshInterval  | false  | 86400  | time before crawler considers data old and needs re-downloading |
| proxyHost  | false  | NA  | Proxy Host |
| proxyPort  | false  | NA  | Proxy Port |
| proxyUser  | false  | NA  | Proxy User |
| proxyPass  | false  | NA  | Proxy Pass |
| sleep  | false  | NA  | Sleep between url calls |
| crawlerUserAgent  | false  | Wajja Crawler  | Custom Crawler Agent |
| crawlerReferer  | false  | http://wajja.eu/  | Custom Crawler Referer |
| waitForCssSelector  | false  | NA  | Waits for a certain CSS to appear on the page |
| maxWaitForCssSelector  | false  | 30  | max wait for waitForCssSelector | 
| readRobot  | false  | true  | read the sites robot | 
| rootUrl  | false  | site url  | root of the website | 
| reindex  | false  | false  | full reindex of what is already in the queue | 
| enableCrawl  | false  | true  | enabled or disable web crawler |  
| enableDelete  | false  | true  | enabled or disable deletion of content |     
| enableRegex  | false  | false  | enabled or disable of regex rerun (you can reprocess the queue this way) |  
| enableHashtag  | false  | false  | follow links with hashtags |      