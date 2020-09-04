def getIncrementedVersion() {
    final versionFileText = readFile('version.json')
    final versionFile = new groovy.json.JsonSlurperClassic().parseText(versionFileText)

    println("Current Version: ${versionFile.version}")

    final maximumTotalVersionNumbers = 4
    final minimumTotalVersionNumbers = 1
    final partsSeparatorToken = '_'
    final numberSepearatorToken = '.'

    final versionParts = versionFile.version.tokenize(partsSeparatorToken)
    final versionPrefix = versionParts.subList(0, versionParts.size() - 1).join(partsSeparatorToken)

    final versionBuildNumber = versionParts.last()
    final versionNumbers = versionBuildNumber.tokenize(numberSepearatorToken)
    def incrementedVersionNumbers = ''

    if (versionNumbers.size() < maximumTotalVersionNumbers && versionNumbers.size() >= minimumTotalVersionNumbers) {
        incrementedVersionNumbers = "${versionBuildNumber}${numberSepearatorToken}1"
    }
    else if (versionNumbers.size() == maximumTotalVersionNumbers) {
        final majorVersionNumbers = versionNumbers.subList(0, versionNumbers.size() - 1).join(numberSepearatorToken)
        final minorVersionNumber = Integer.parseInt(versionNumbers.last())
        incrementedVersionNumbers = "${majorVersionNumbers}${numberSepearatorToken}${minorVersionNumber + 1}"
    }
    else {
        final versionFormat = "X${numberSepearatorToken}X${numberSepearatorToken}X${numberSepearatorToken}X"
        throw new IllegalArgumentException(
            "Illegal version number input. Must be compatible with following format: ${versionFormat}")
    }

    final incrementedVersion = "${versionPrefix}_${incrementedVersion}"
    println("Incremented Version: ${incrementedVersion}")

    return incrementedVersion
}

return this
