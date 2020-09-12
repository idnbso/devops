class VersionStructureTests {
    final VersionStructure fakeGenericVersionStructure    

    VersionStructureTests() {
        fakeGenericVersionStructure = new VersionStructure(
            versionPartsNames: [ "Major", "Minor", "Build", "Patch" ], 
            numberSeparatorToken: '.',
            versionSchemeRegex: "\\d+((\\.\\d+){0,3})?"
        )
    }

    def getVersionFormat_ValidVersionFormat_ReturnsValidResult() {
        // Arrange
        final versionStructure = new VersionStructure(fakeGenericVersionStructure)

        // Act
        final versionFormat = versionStructure.getVersionFormat()

        // Assert
        assert versionFormat == "Major.Minor.Build.Patch"
    }

    def getIsVersionBuildNumberValid_ValidVersionFormat_ReturnsValidResult() {
        // Arrange
        final versionBuildNumber = "1.0.0.0"
        final versionStructure = new VersionStructure(fakeGenericVersionStructure)

        // Act
        final isValid = versionStructure.getIsVersionBuildNumberValid(versionBuildNumber)

        // Assert
        assert isValid == true
    }

    def getIncrementedVersionNumber_ValidVersionBuildNumber_ReturnsValidResult() {
        // Arrange
        final versionBuildNumber = "1.0.0.0"
        final versionStructure = new VersionStructure(fakeGenericVersionStructure)

        // Act
        final incrementedVersion = versionStructure.getIncrementedVersionNumber(versionBuildNumber)

        // Assert
        assert incrementedVersion == "1.0.0.1"
    }

    def getIncrementedVersionScenarios_ValidVersionScenarios_ReturnsValidResult() {
        // Arrange
        final versionBuildNumber = "1.0.0.0"
        final versionStructure = new VersionStructure(fakeGenericVersionStructure)

        // Act
        def (majorRelease, minorRelease, buildRelease, patchRelease) = 
            versionStructure.getIncrementedVersionScenarios(versionBuildNumber)

        // Assert
        assert patchRelease == "1.0.0.1"
        assert buildRelease == "1.0.1.0"
        assert minorRelease == "1.1.0.0"
        assert majorRelease == "2.0.0.0"
    }
}