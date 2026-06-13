def update(String repoOwner, String repo, String email, String version){
    withCredentials([usernamePassword(credentialsId: 'github_creds', 
    usernameVariable: 'USER', 
    passwordVariable: 'TOKEN')]) {
        withEnv([
            "REPO_OWNER=${repoOwner}",
            "REPO=${repo}",
            "EMAIL=${email}",
            "VERSION=${version}"
        ]) {
            sh '''
                echo "=============================================="
                ls -la
                echo "=============================================="
                git config user.name "$USER"
                git config user.email "$EMAIL"
                git add .app-info.json
                git commit -m "Update next app version: $VERSION in .app-info.json"
                git remote set-url origin https://x-access-token:$TOKEN@github.com/$REPO_OWNER/$REPO.git
                git push origin main
            '''
        }
    }
}