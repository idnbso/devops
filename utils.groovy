String getIncrementedVersion() {
    String versionFileText = readFile('version.json')
    Object versionFile = new groovy.json.JsonSlurperClassic().parseText(versionFileText)

    println("Current Version: ${versionFile.version}")

    int maximumTotalVersionNumbers = 4
    int minimumTotalVersionNumbers = 1
    String partsSeparatorToken = '_'
    String numberSepearatorToken = '.'

    String[] versionParts = versionFile.version.tokenize(partsSeparatorToken)
    String versionPrefix = versionParts.subList(0, versionParts.size() - 1).join(partsSeparatorToken)

    String versionBuildNumber = versionParts.last()
    String[] versionNumbers = versionBuildNumber.tokenize(numberSepearatorToken)
    String incrementedVersionNumbers = ''

    if (versionNumbers.size() < maximumTotalVersionNumbers && versionNumbers.size() >= minimumTotalVersionNumbers) {
        incrementedVersionNumbers = "${versionBuildNumber}${numberSepearatorToken}1"
    }
   else if (versionNumbers.size() == maximumTotalVersionNumbers) {
        String majorVersionNumbers = versionNumbers.subList(0, versionNumbers.size() - 1).join(numberSepearatorToken)
        int minorVersionNumber = Integer.parseInt(versionNumbers.last())
        incrementedVersionNumbers = "${majorVersionNumbers}${numberSepearatorToken}${minorVersionNumber + 1}"
   }
   else {
        throw new IllegalArgumentException(
            'Illegal version number input. Must be compatible with following format: X.X.X.X')
   }

    String incrementedVersion = "${versionPrefix}_${incrementedVersion}"
    println("Incremented Version: ${incrementedVersion}")

    return incrementedVersion
}

return this
