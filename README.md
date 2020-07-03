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
  ```
  yum install -y jfrog-artifactory-oss-7.5.8.rpm
  ```

### Elasticsearch

### Kibana

### Logstash

### Sonarqube

<br />

## Environments
### DEV (Tomcat 7)
* <b>URL:</b> http://localhost:8080/devops/
* <b>Logs directory:</b> ```/usr/share/tomcat/logs/devops-dev.log```

### QA (Tomcat 7)
* <b>URL:</b> http://localhost:8080/devopsqa/
* <b>Logs directory:</b> ```/usr/share/tomcat/logs/devops-qa.log```

### PROD (Tomcat 8)
* <b>URL:</b> http://localhost:9080/devops/
* <b>Logs directory:</b> ```/opt/tomcat/logs/devops-prod.log```

<br />

## Scripts
### Tomcat 8 Set Environment Script
* /opt/tomcat/bin/setenv.sh
  ```
  export JAVA_OPTS="$JAVA_OPTS -Dspring.profiles.active=prod"
  ```

<br />

## Jenkins
### DvOpsDEV Pipeline Configuration
* Pre Steps
  * Execute Shell Command
    ```
    echo "spring.profiles.active=dev" >> src/main/resources/application.properties
    ```
* Post Steps
  * Execute Shell Command (Final step if deploy selected)
    ```
    curl -u ${tcuser}:${tcpass} -T target/**.war "http://localhost:8080/manager/text/deploy?path=/devops&update=true"
    ```

### DevOpsQA Pipeline Configuration
* Pre Steps
  * Execute Shell Command
    ```
    echo "spring.profiles.active=qa" >> src/main/resources/application.properties
    ```


### DevOpsPROD Pipeline Configuration
* Build
  * Execute Shell Command
    ```
    echo "Deploying war from http://localhost:8081/artifactory/libs-release-local/com/example/devops/${tag:1}/devops-${tag:1}.war"
    ```
  * Execute Shell Command
     ```
     #!/bin/bash
    curl -u ${afuser}:${afpass} -O http://localhost:8081/artifactory/libs-release-local/com/example/devops/${tag:1}/devops-${tag:1}.war
    ```
  * Execute Shell Command
    ```
    #!/bin/bash
    curl -u ${tcuser}:${tcpass} -T *.war "http://localhost:9080/manager/text/deploy?path=/devops&update=true"
    ```