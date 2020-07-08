# DevOps
Based on: [Udemy - DevOps with GIT(Flow) Jenkins, Artifactory, Sonar, ELK, JIRA](https://www.udemy.com/course/devops-with-git-jenkins-artifactory-and-elk-stack)

The following notes only include changes relative to the content of the course it is based on.

<br />

## Installation
### Virtual Machine
* [VirtualBox 6.1](https://www.virtualbox.org/)

### Operating System
* [CentOS 7-1908](https://sourceforge.net/projects/osboxes/files/v/vb/10-C-nt/7/7-1908/Cos-1908-VB-32bit.7z/download)

### Web Containers
* [Apache Tomcat 7.0.76](https://www.digitalocean.com/community/tutorials/how-to-install-apache-tomcat-7-on-centos-7-via-yum)

* [Apache Tomcat 8.5.56](https://www.digitalocean.com/community/tutorials/how-to-install-apache-tomcat-8-on-centos-7)

### Jenkins
* [Jenkins 2.235.1](https://linuxize.com/post/how-to-install-jenkins-on-centos-7/)

### JFrog Artifactory
* [Artifactory OSS 7.5.8](https://www.jfrog.com/confluence/display/JFROG/Installing+Artifactory#InstallingArtifactory-RPMInstallation)
  ```shell
  $ yum install -y jfrog-artifactory-oss-7.5.8.rpm
  ```

### Elasticsearch
* [Elasticsearch 7.8](https://www.elastic.co/guide/en/elasticsearch/reference/current/rpm.html)

### Kibana
* [Kibana 7.8](https://www.elastic.co/guide/en/kibana/current/rpm.html)

### Logstash
* [Logstash 7.8](https://www.elastic.co/guide/en/logstash/7.8/installing-logstash.html#_yum)

### Filebeat
* [Firebeat 7.8](https://www.digitalocean.com/community/tutorials/how-to-install-elasticsearch-logstash-and-kibana-elastic-stack-on-ubuntu-20-04)
* [Elasticsearch, Logstash, and Kibana (Elastic Stack)](https://www.digitalocean.com/community/tutorials/how-to-install-elasticsearch-logstash-and-kibana-elastic-stack-on-ubuntu-20-04)

### SonarQube
* [SonarQube 8.3](https://www.sonarqube.org/downloads/)
* [SonarQube 7.9.x LTS with Oracle JAVA 11, PostgreSQL 10.x, Nginx, and Let's Encrypt certificates](https://www.fosslinux.com/24429/how-to-install-and-configure-sonarqube-on-centos-7.htm)

<br />

## Links
### Jira Software
* [DevOps Releases](https://idnbso.atlassian.net/projects/DEV?orderField=RANK&selectedItem=com.atlassian.jira.jira-projects-plugin%3Arelease-page&status=released-unreleased)

### Confluence
* [Project: DevOps](https://idnbso.atlassian.net/wiki/spaces/DEV/overview?homepageId=65603)

<br />

## Environments
### DEV (Tomcat 7)
* <b>URL:</b> http://localhost:8080/devops/
* <b>Logs Path:</b> ```/usr/share/tomcat/logs/devops-dev.log```

### QA (Tomcat 7)
* <b>URL:</b> http://localhost:8080/devopsqa/
* <b>Logs Path:</b> ```/usr/share/tomcat/logs/devops-qa.log```

### PROD (Tomcat 8)
* <b>URL:</b> http://localhost:9080/devops/
* <b>Logs Path:</b> ```/opt/tomcat/logs/devops-prod.log```

<br />

## Scripts
### Tomcat 8 Set Environment Script
* Path: ```/opt/tomcat/bin/setenv.sh```
  ```shell
  export JAVA_OPTS="$JAVA_OPTS -Dspring.profiles.active=prod"
  ```

<br />

## Jenkins Configuration
### DevOpsDEV Pipeline Configuration
* Pre Steps
  * Execute Shell Command
    ```shell
    echo "spring.profiles.active=dev" >> src/main/resources/application.properties
    ```
* Post Steps
  * Execute Shell Command (Final step if deploy selected)
    ```shell
    curl -u ${tcuser}:${tcpass} -T target/**.war "http://localhost:8080/manager/text/deploy?path=/devops&update=true"
    ```

### DevOpsQA Pipeline Configuration
* Pre Steps
  * Execute Shell Command
    ```shell
    echo "spring.profiles.active=qa" >> src/main/resources/application.properties
    ```


### DevOpsPROD Pipeline Configuration
* Build
  * Execute Shell Command
    ```shell
    echo "Deploying war from http://localhost:8081/artifactory/libs-release-local/com/example/devops/${tag:1}/devops-${tag:1}.war"
    ```
  * Execute Shell Command
     ```shell
     #!/bin/bash
    curl -u ${afuser}:${afpass} -O http://localhost:8081/artifactory/libs-release-local/com/example/devops/${tag:1}/devops-${tag:1}.war
    ```
  * Execute Shell Command
    ```shell
    #!/bin/bash
    curl -u ${tcuser}:${tcpass} -T *.war "http://localhost:9080/manager/text/deploy?path=/devops&update=true"
    ```

<br/>

## Logstash Configuration
* logstash-pipeline.conf
  ```conf
  input {
    beats {
      port => "5044"
    }
  }

  filter {
    if [type] == "apache" {
      grok {
        match => { "message" => "%{COMBINEDAPACHELOG}" }
      }
      date {
        match => [ "timestamp" , "dd/MMM/yyyy:HH:mm:ss Z" ]
      }
    }
    if [type] == "tomcat" {
      grok {
        match => [ "message", "%{TOMCATLOG}", "message", "%{CATALINALOG}" ]
      }
      date {
        match => [ "timestamp", "yyyy-MM-dd HH:mm:ss,SSS Z", "MMM dd, yyyy HH:mm:ss a" ]
      }
    }
  }

  output {
    elasticsearch {
      hosts => ["localhost:9200"]
    }
    stdout {
      codec => rubydebug
    }

  }
  ```

<br/>

## Filebeat Configuration
* ```/etc/filebeat/filebeat.yml```
  ```yml
  filebeat.inputs:
    - type: log
        enabled: true
        paths:
          - /usr/share/tomcat/logs/devops-dev.log
        fields:  
          app: devops
          server: dev

    - type: log
        enabled: true
        paths:
          - /usr/share/tomcat/logs/devops-qa.log
        fields:  
          app: devops
          server: qa

    - type: log
        enabled: true
        paths:
          - /opt/tomcat/logs/devops-prod.log
        fields:  
          app: devops
          server: prod
  ```