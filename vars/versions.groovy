def bumpUpPatch(String version){
    def (major, minor, patch) = version.tokenize('.')
    def newPatch = patch.toInteger() + 1
    return "${major}.${minor}.${newPatch}"
}
def bumpUpMinor(String version){
    def (major, minor, patch) = version.tokenize('.')
    def newMinor = minor.toInteger() + 1
    return "${major}.${newMinor}.${patch}"
}
def bumpUpMajor(String version){
    def (major, minor, patch) = version.tokenize('.')
    def newMajor = major.toInteger() + 1
    return "${newMajor}.${minor}.${patch}"
}