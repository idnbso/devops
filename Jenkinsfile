@Library('pipeline-shared-lib')
import com.cwctravel.hudson.plugins.extended_choice_parameter.ExtendedChoiceParameterDefinition

node {
  def mvnHome
  def pom
  def artifactVersion
  def tagVersion
  def retrieveArtifact
  def utils = new org.util.utils()

  stage('Prepare') {
    mvnHome = tool 'maven'
   
    // Read local stored version json file and deserialize to an object
    final jsonFileName = 'version.json'
    final versionFileText = readFile(jsonFileName)
    final versionFile = new groovy.json.JsonSlurperClassic().parseText(versionFileText)
    final versionBuildNumber = versionFile.build_version

    def (majorRelease, minorRelease, buildRelease, patchRelease) = utils.getACMReportsIncrementedVersion(versionBuildNumber)
    println "Incremented Version Variables: Major: ${majorRelease}, Minor: ${minorRelease}, Build: ${buildRelease}, Patch: ${patchRelease}"

	 properties([parameters([new ExtendedChoiceParameterDefinition(
				"Release", // name
				"PT_RADIO", // type
				"${majorRelease},${minorRelease},${buildRelease},${patchRelease}",  // propertyValue
				"", // projectName
				"", // propertyFile
				"", // groovyScript
				"", // groovyScriptFile
				"", // bindings
				"", // groovyClasspath
				"", // propertyKey
				"", // defaultPropertyValue
				"", // defaultPropertyFile
				"", // defaultGroovyScript
				"", // defaultGroovyScriptFile
				"", // defaultBindings
				"", // defaultGroovyClasspath
				"", // defaultPropertyKey
				"Major: ${majorRelease},Minor: ${minorRelease},Build: ${buildRelease},Patch: ${patchRelease}", // descriptionPropertyValue
				"", // descriptionPropertyFile
				"", // descriptionGroovyScript
				"", // descriptionGroovyScriptFile
				"", // descriptionBindings
				"", // descriptionGroovyClasspath
				"", // descriptionPropertyKey
				"", // javascriptFile
				"", // javascript
				false, // saveJSONParameterToFile
				false, // quoteValue
				4, // visibleItemCount
				"Choose the next release version (last version was ${versionBuildNumber})", // description
				"," // multiSelectDelimiter
            )])])
	   
	   echo "Selected ${params.Release}"
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
   //   if (isUnix()) {
   //      sh "'${mvnHome}/bin/mvn' sonar:sonar"
   //   } else {
   //      bat(/"${mvnHome}\bin\mvn" sonar:sonar/)
   //   }
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

