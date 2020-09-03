node {
  def mvnHome
  def pom
  def artifactVersion
  def tagVersion
  def retrieveArtifact
  
  stage('Prepare') {
    mvnHome = tool 'maven'

    def incrementedVersion = getIncrementedVersion()

    println "Incremented Version Variable: ${incrementedVersion}"
  }

  stage('Checkout') {
     checkout scm
  }

  stage('Build') {
     if (isUnix()) {
        sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean package"
     } else {
        bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore clean package/)
     }
  }

  stage('Unit Test') {
     junit '**/target/surefire-reports/TEST-*.xml'
     archive 'target/*.jar'
  }

  stage('Integration Test') {
    if (isUnix()) {
       sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean verify"
    } else {
       bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore clean verify/)
    }
  }

  stage('Sonar') {
     if (isUnix()) {
        sh "'${mvnHome}/bin/mvn' sonar:sonar"
     } else {
        bat(/"${mvnHome}\bin\mvn" sonar:sonar/)
     }
  }

  if(env.BRANCH_NAME == 'master'){
    stage('Validate Build Post Prod Release') {
      if (isUnix()) {
         sh "'${mvnHome}/bin/mvn' clean package"
      } else {
         bat(/"${mvnHome}\bin\mvn" clean package/)
      }
    }

  }

  if(env.BRANCH_NAME == 'develop'){
    stage('Snapshot Build And Upload Artifacts') {
      if (isUnix()) {
         sh "'${mvnHome}/bin/mvn' clean deploy"
      } else {
         bat(/"${mvnHome}\bin\mvn" clean deploy/)
      }
    }

    stage('Deploy') {
       sh 'curl -u jenkins:jenkins -T target/**.war "http://localhost:8080/manager/text/deploy?path=/devops&update=true"'
    }

    stage("Smoke Test"){
       sh "curl --retry-delay 10 --retry 5 http://localhost:8080/devops"
    }
  }

}

def getIncrementedVersion() {
   String versionFileText = readFile('version.json');
   def versionFile = new groovy.json.JsonSlurperClassic().parseText(versionFileText);

   println("Current Version: ${versionFile.version}")

   def versionParts = versionFile.version.tokenize('_')
   def versionPrefix = versionParts.subList(0, versionParts.size()-1).join('_')

   def versionBuildNumber = versionParts.last()    
   def versionNumbers = versionBuildNumber.tokenize('.')
   def incrementedVersionNumbers = ""

   if (versionNumbers.size() < 4 && versionNumbers.size() > 0) {
      incrementedVersionNumbers = "${versionBuildNumber}.1"
   }
   else if (versionNumbers.size() == 4) {
      def majorVersionNumbers = versionNumbers.subList(0, versionNumbers.size()-1).join('.')
      def minorVersionNumber = Integer.parseInt(versionNumbers[versionNumbers.size()-1])
      incrementedVersionNumbers = "${majorVersionNumbers}.${minorVersionNumber + 1}"
   }
   else {
      throw new Exception("Illegal version number")
   }

   def incrementedVersion = "${versionPrefix}_${incrementedVersion}"
   println("Incremented Version: ${incrementedVersion}")

   return incrementedVersion
}