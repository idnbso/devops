class VersionStructureTests {
    def getVersionFormat_ValidVersionFormat_ReturnsValidResult() {
        // Arrange
        final versionStructure = getFakeGenericVersionStructure()

        // Act
        final versionFormat = versionStructure.getVersionFormat()

        // Assert
        assert versionFormat == "Major.Minor.Build.Patch"
    }

    def getIsVersionBuildNumberValid_ValidVersionFormat_ReturnsValidResult() {
        // Arrange
        final versionBuildNumber = "1.0.0.0"
        final versionStructure = getFakeGenericVersionStructure()

        // Act
        final isValid = versionStructure.getIsVersionBuildNumberValid(versionBuildNumber)

        // Assert
        assert isValid == true
    }

    def getIncrementedVersionNumber_ValidVersionBuildNumber_ReturnsValidResult() {
        // Arrange
        final versionBuildNumber = "1.0.0.0"
        final versionStructure = getFakeGenericVersionStructure()

        // Act
        final incrementedVersion = versionStructure.getIncrementedVersionNumber(versionBuildNumber)

        // Assert
        assert incrementedVersion == "1.0.0.1"
    }

    def getIncrementedVersionScenarios_ValidVersionScenarios_ReturnsValidResult() {
        // Arrange
        final versionBuildNumber = "1.0.0.0"
        final versionStructure = getFakeGenericVersionStructure()

        // Act
        def (majorRelease, minorRelease, buildRelease, patchRelease) = 
            versionStructure.getIncrementedVersionScenarios(versionBuildNumber)

        // Assert
        assert patchRelease == "1.0.0.1"
        assert buildRelease == "1.0.1.0"
        assert minorRelease == "1.1.0.0"
        assert majorRelease == "2.0.0.0"
    }

    def getFakeGenericVersionStructure() {
        return new VersionStructure(
            versionPartsNames: [ "Major", "Minor", "Build", "Patch" ], 
            numberSeparatorToken: '.',
            versionSchemeRegex: "\\d+((\\.\\d+){0,3})?"
        )
    }
}