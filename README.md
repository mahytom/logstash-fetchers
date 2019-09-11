# Logstash Fetchers Plugins

These tools have been created to crawl applications/sites to fetch data and send them to elastic for searching.

No processing of the data is done so it can be processed , as is, by the logstash filters.

# Current supported tools

## Web Plugin

This plugin will crawl websites and download all information ready to be indexed.

The data is sent as a byte[] and all headers are added to the hashmap send to elastic.

### Configuration

```
input {
  webfetcher {
	urls => ["http://yousite.eu/"]
	exclude => ["css","js"]
	dataFolder => "/mypath/tofolder/"
	threads => 10
	refreshInterval => 300
  }
}
```

## Sharepoint Plugin

Work in progress

## Filesystem Plugin

This plugin will crawl a local drive and download all information ready to be indexed.

The data is sent as a byte[] and all headers are added to the hashmap send to elastic.

No permissions are being extracted. This will be added at a later date.

### Configuration

```
input {
  filesystemfetcher {
	paths => ["/my/data/directory"]
	exclude => ["docx","pdf"]
	dataFolder => "/mypath/tofolder/"
	threads => 10
  }
}
```

## Confluence Plugin

Work in progress


## Initial configuration

Download the latest logstash and put it at the root of the project, it needs to be outside of this clone

```git clone --branch 7.2 --single-branch https://github.com/elastic/logstash.git logstash-7.2```

Build logstash
```.\gradlew.bat assemble```

Once the project is cloned create a gradle.properties at the root

Add the following path to the logstash you just built

```LOGSTASH_CORE_PATH=../../logstash-7.2/logstash-core```

On windows machine your JAR might be blocked by gradle.

You can disable the gradle deamon by adding the following property in gradle.properties

```org.gradle.daemon=false```

## Run the project

At the root of the of the web fetcher

```
gradlew.bat clean
gradlew.bat gem
```

Install in logstash

```/logstash-7.2.0/bin/logstash-plugin.bat install --no-verify --local /logstash-***-plugin/***-webfetcher-1.0.0.gem```

Run logstash with your config

```logstash.bat -f ..\logstash.conf```

## Usefull commands

To refresh eclipse dependencies

```gradlew cleanEclipse eclipse```

then refresh inside eclipse.