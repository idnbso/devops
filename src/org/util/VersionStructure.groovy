package org.util

class VersionStructure {
    final String numberSeparatorToken
    final ArrayList<String> versionPartsNames
    final int minimumTotalVersionNumbers
    final int maximumTotalVersionNumbers
    final int totalSubVersionNumbers
    final GString versionSchemeRegex

    VersionStructure(Map map = [:]) {
        this.versionPartsNames = map.versionPartsNames ?: [ "Major", "Minor", "Build", "Patch" ]
        this.totalSubVersionNumbers = versionPartsNames.size() - 1
        this.numberSeparatorToken = map.numberSeparatorToken ?: '.'
        this.versionSchemeRegex = map.versionSchemeRegex ? "${map.versionSchemeRegex}" : 
                                                            /\d+(($numberSeparatorToken\d+){0,$totalSubVersionNumbers})?/
        this.maximumTotalVersionNumbers = versionPartsNames.size()
        this.minimumTotalVersionNumbers = 1
    }

    VersionStructure(VersionStructure vs) {
        this(versionPartsNames: vs.versionPartsNames, 
             numberSeparatorToken: vs.numberSeparatorToken, 
             versionSchemeRegex: vs.versionSchemeRegex)
    }

    String getVersionFormat() { versionPartsNames.join(numberSeparatorToken) }

    boolean getIsVersionBuildNumberValid(String versionBuildNumber) { versionBuildNumber ==~ versionSchemeRegex }

    ArrayList<String> getIncrementedVersionScenarios(String versionBuildNumber) {
        return versionPartsNames.collect { getIncrementedVersionNumber(versionBuildNumber, versionPartsNames.indexOf(it)) }
    }
    
    String getIncrementedVersionNumber(String versionBuildNumber, int subVersionPosition = totalSubVersionNumbers) {
        def totalVersionNumbers = versionPartsNames.size()

        def getIncrementVersionExceptionMessage = {mode -> {
            def exceptionMessage = "Illegal version number ${mode} of ${versionBuildNumber} "
            exceptionMessage += "for position ${subVersionPosition}: ${versionPartsNames[subVersionPosition]}. "
            exceptionMessage += "Must be compatible with following version format: ${versionFormat}"
        }}

        if (getIsVersionBuildNumberValid(versionBuildNumber) == false || totalVersionNumbers < minimumTotalVersionNumbers || 
            totalVersionNumbers > maximumTotalVersionNumbers) {
            throw new IllegalArgumentException(getIncrementVersionExceptionMessage('input'))
        }

        final versionNumbers = versionBuildNumber.tokenize(numberSeparatorToken)
        final versionNumber = versionNumbers[subVersionPosition] as int
        versionNumbers[subVersionPosition] = versionNumber + 1

        final incrementedVersionNumber = versionNumbers.join(numberSeparatorToken)

        if (getIsVersionBuildNumberValid(versionBuildNumber) == false) {
            throw new UnsupportedOperationException(getIncrementVersionExceptionMessage('output'))
        }

        return incrementedVersionNumber
    }
}

return this