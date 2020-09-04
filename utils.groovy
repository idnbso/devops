def getACMReportsIncrementedVersion() {
    // Read local stored version json file and deserialize to an object
    final jsonFileName = 'version.json'
    final versionFileText = readFile(jsonFileName)
    final versionFile = new groovy.json.JsonSlurperClassic().parseText(versionFileText)

    println("Current Version: ${versionFile.version}")

    // Set ACM Reports specific versioning scheme constant values
    final versionPartsNames = [ "Environment", "Major", "Minor", "Patch" ].collect { it.toUpperCase() }
    final maximumTotalVersionNumbers = versionPartsNames.size()
    final minimumTotalVersionNumbers = 1
    final partsSeparatorToken = '_'
    final numberSeparatorToken = '.'
    final versionFormat = versionPartsNames.join(numberSeparatorToken)

    // Build incremented version
    final versionParts = versionFile.version.tokenize(partsSeparatorToken)
    final versionPrefix = versionParts.subList(0, versionParts.size() - 1).join(partsSeparatorToken)
    final versionBuildNumber = versionParts.last()

    final incrementedVersionNumber = getIncrementedVersionNumber(versionBuildNumber, versionFormat, numberSeparatorToken,
                                        maximumTotalVersionNumbers, minimumTotalVersionNumbers)
    final incrementedVersion = "${versionPrefix}_${incrementedVersionNumber}"
    println("Incremented Version: ${incrementedVersion}")

    return incrementedVersion
}

def getIncrementedVersionNumber(versionBuildNumber, versionFormat, numberSeparatorToken,
                                maximumTotalVersionNumbers, minimumTotalVersionNumbers) {
    final versionNumbers = versionBuildNumber.tokenize(numberSeparatorToken)
    String incrementedVersionNumber

    if (versionNumbers.size() < maximumTotalVersionNumbers && versionNumbers.size() >= minimumTotalVersionNumbers) {
        incrementedVersionNumber = "${versionBuildNumber}${numberSeparatorToken}1"
    }
    else if (versionNumbers.size() == maximumTotalVersionNumbers) {
        final majorVersionNumbers = versionNumbers.subList(0, versionNumbers.size() - 1).join(numberSeparatorToken)
        final minorVersionNumber = Integer.parseInt(versionNumbers.last())
        incrementedVersionNumber = "${majorVersionNumbers}${numberSeparatorToken}${minorVersionNumber + 1}"
    }
    else {
        throw new IllegalArgumentException(
            "Illegal version number input. Must be compatible with following format: ${versionFormat}")
    }

    return incrementedVersionNumber
}

return this