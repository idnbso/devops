def runTestSuite() {
    println "\t\t\t##############################################"
    println "\t\t\t### ACM Reports DevOps Scripts Test Runner ###"
    println "\t\t\t##############################################\n\n"

    runVersionStructureTests() // Fail on first assert exception

    println "\n\nDone."
}

def runVersionStructureTests() {
    println "Running Version Structure Tests:"
    println "###############################\n"
    def testsRunner = new VersionStructureTests()

    testsRunner.getVersionFormat_ValidVersionFormat_ReturnsValidResult()
    println "Success: getVersionFormat_ValidVersionFormat_ReturnsValidResult"

    testsRunner.getIsVersionBuildNumberValid_ValidVersionFormat_ReturnsValidResult()
    println "Success: getIsVersionBuildNumberValid_ValidVersionFormat_ReturnsValidResult"

    testsRunner.getIncrementedVersionNumber_ValidVersionBuildNumber_ReturnsValidResult()
    println "Success: getIncrementedVersionNumber_ValidVersionBuildNumber_ReturnsValidResult"

    testsRunner.getIncrementedVersionScenarios_ValidVersionScenarios_ReturnsValidResult()
    println "Success: getIncrementedVersionScenarios_ValidVersionScenarios_ReturnsValidResult"
}

runTestSuite()