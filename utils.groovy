def getIncrementedVersion() {
    String incrementedVersion

    try {
        def versionFileText = readFile('version.json')
        def versionFile = new groovy.json.JsonSlurperClassic().parseText(versionFileText)

        println("Current Version: ${versionFile.version}")

        def maximumTotalVersionNumbers = 4
        def minimumTotalVersionNumbers = 1
        def partsSeparatorToken = '_'
        def numberSepearatorToken = '.'

        def versionParts = versionFile.version.tokenize(partsSeparatorToken)
        def versionPrefix = versionParts.subList(0, versionParts.size() - 1).join(partsSeparatorToken)

        def versionBuildNumber = versionParts.last()
        def versionNumbers = versionBuildNumber.tokenize(numberSepearatorToken)
        def incrementedVersionNumbers = ''

        if (versionNumbers.size() < maximumTotalVersionNumbers && versionNumbers.size() >= minimumTotalVersionNumbers) {
            incrementedVersionNumbers = "${versionBuildNumber}${numberSepearatorToken}1"
        }
        else if (versionNumbers.size() == maximumTotalVersionNumbers) {
             def majorVersionNumbers = versionNumbers.subList(0, versionNumbers.size() - 1).join(numberSepearatorToken)
             def minorVersionNumber = Integer.parseInt(versionNumbers.last())
             incrementedVersionNumbers = "${majorVersionNumbers}${numberSepearatorToken}${minorVersionNumber + 1}"
        }
        else {
             def versionFormat = "X${numberSepearatorToken}X${numberSepearatorToken}X${numberSepearatorToken}X"
             throw new IllegalArgumentException(
                 "Illegal version number input. Must be compatible with following format: ${versionFormat}")
        }

        incrementedVersion = "${versionPrefix}_${incrementedVersion}"
        println("Incremented Version: ${incrementedVersion}")
    }
    catch (Exception ex) {
        println("${ex}")
    }

    return incrementedVersion
}

return this