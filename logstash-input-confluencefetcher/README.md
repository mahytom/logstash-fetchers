# Logstash Confluence Input Plugin

This Plugin reads confluence spaces and 

- Extracts user and groups
- Extracts Sites / Pages and Attachments

No processing of the data is done so it can be processed , as is, by the logstash filters.

### Configuration

```
input {
  confluencefetcher {
   url => "http://localhost/confluence"
   dataFolder => "D:/path/to/logstash/data"
   enableUserSync => false
   userSyncBatchSize => 10
   userSyncThreadSize => 1
   userSyncCron => "0 0 0 ? * *"
   enableDataSync => true 
   dataSyncBatchSize => 10
   dataSyncThreadSize => 1
   dataSyncCron => "0 0 0 ? * *"
   spaces => ["SPACE_NAME"]
   pageLimit => 10
   dataAttachmentsInclude => ["regex"]
   dataAttachmentsExclude => ["regex"]
   dataAttachmentsMaxSize => 0
   dataPageExclude => ["regex"]
   dataSpaceExclude => ["regex"]
   username => "username"
   password => "password"
   sleep => 10
  }
}
```

```
   url => "http://localhost/confluence"
```
-- Endpoint to the rest endpoint (MANDATORY)

```   
   dataFolder => "D:/path/to/logstash/data"
```
-- Full path to the logstash data folder (MANDATORY)
   
```
   enableUserSync => false
```
-- if user sync should be enabled (MANDATORY)
   
```
   userSyncBatchSize => 10
```
-- Size of pages retrieved from confluence when quering via rest  (DEFAULT : 10)

```
   userSyncThreadSize => 1
```
-- Number of threads retrieved from confluence when quering via rest  (DEFAULT : 1)

```
   userSyncCron => "0 0 0 ? * *"
```
-- Trigger when the job should start  (MANDATORY)

```
   enableDataSync => true 
```
-- if data sync should be enabled (MANDATORY)

```
   dataSyncBatchSize => 10
```
-- Size of pages retrieved from confluence when quering via rest  (DEFAULT : 10)

```
   dataSyncThreadSize => 1
```
-- Number of threads retrieved from confluence when quering via rest  (DEFAULT : 1)

```
   dataSyncCron => "0 0 0 ? * *"
```
-- Trigger when the job should start  (MANDATORY)

```
   spaces => ["SPACE_NAME"]
```
-- List of spaces to crawl. Leave empty to crawl everything

```
   pageLimit => 10
```
-- Maximum of pages to fetch per space

```
   dataAttachmentsInclude => ["regex"]
```
-- Regex to include by attachment name

```
   dataAttachmentsExclude => ["regex"]
```
-- Regex to exclude by attachment name

```
   dataAttachmentsMaxSize => 0
```
-- Maximum size of attachment by kbyte

```
   dataPageExclude => ["regex"]
```
-- Regex to exclude by page name

```
   dataSpaceExclude => ["regex"]
```
-- Regex to exclude by space name

```
   username => "username"
```
-- Username to login into confluence

```
   password => "password"
```
-- Password to login into confluence 

```
   sleep => 10
```
-- Sleep between HTTP calls, in ms.

## How Build and Run the Confluence Fetcher in Logstash

At the root of the of the web fetcher
```
gradlew.bat clean gem
```
Install in logstash
```
/logstash-7.2.0/bin/logstash-plugin.bat install --no-verify --local /logstash-output-search-plugin/logstash-input-searchoutput-1.0.0.gem
```
In logstash conf you can configure the tool

Run logstash with your config
```
logstash.bat -f ..\logstash.conf
```