def getACMReportsIncrementedVersion() {
    // Read local stored version json file and deserialize to an object    
    final jsonFileName = 'version.json'
    final versionFileText = readFile(jsonFileName)
    final versionFile = new groovy.json.JsonSlurperClassic().parseText(versionFileText)

    println("Current Version: ${versionFile.version}")

    // Set ACM Reports specific versioning scheme constant values
    final maximumTotalVersionNumbers = 4 // Environment, Major, Minor, Patch
    final minimumTotalVersionNumbers = 1
    final partsSeparatorToken = '_'
    final numberSeparatorToken = '.'
    final versionFormat = "ENVIRONMENT${numberSeparatorToken}MAJOR${numberSeparatorToken}MINOR${numberSeparatorToken}PATCH"

    final incrementedVersion = getIncrementedVersion(versionFile.version, versionFormat, 
        maximumTotalVersionNumbers, minimumTotalVersionNumbers, partsSeparatorToken, numberSeparatorToken)
    println("Incremented Version: ${incrementedVersion}")

    return incrementedVersion
}

def getIncrementedVersion(version, versionFormat, maximumTotalVersionNumbers, minimumTotalVersionNumbers, 
                          partsSeparatorToken, numberSeparatorToken) {
    final versionParts = version.tokenize(partsSeparatorToken)
    final versionPrefix = versionParts.subList(0, versionParts.size() - 1).join(partsSeparatorToken)

    final versionBuildNumber = versionParts.last()
    final versionNumbers = versionBuildNumber.tokenize(numberSeparatorToken)
    def incrementedVersionNumbers = ""

    if (versionNumbers.size() < maximumTotalVersionNumbers && versionNumbers.size() >= minimumTotalVersionNumbers) {
        incrementedVersionNumbers = "${versionBuildNumber}${numberSeparatorToken}1"
    }
    else if (versionNumbers.size() == maximumTotalVersionNumbers) {
        final majorVersionNumbers = versionNumbers.subList(0, versionNumbers.size() - 1).join(numberSeparatorToken)
        final minorVersionNumber = Integer.parseInt(versionNumbers.last())
        incrementedVersionNumbers = "${majorVersionNumbers}${numberSeparatorToken}${minorVersionNumber + 1}"
    }
    else {
        throw new IllegalArgumentException(
            "Illegal version number input. Must be compatible with following format: ${versionFormat}")
    }

    return "${versionPrefix}_${incrementedVersionNumbers}"
}

return this