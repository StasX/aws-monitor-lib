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
                git init $REPO

                git -C "$REPO" config user.name "$USER"
                git -C "$REPO" config user.email "$EMAIL"   

                git -C "$REPO" checkout -b main
                git -C "$REPO" remote add origin "https://x-access-token:$TOKEN@github.com/$REPO_OWNER/$REPO.git"
                git -C "$REPO" sparse-checkout set

                git -C "$REPO" sparse-checkout add ".app-info.json"
                git -C "$REPO" pull origin main
                cp .app-info.json $REPO/
                git -C "$REPO" add .app-info.json
                git -C "$REPO" commit -m "Update next app version: $VERSION in .app-info.json"
                git -C "$REPO" push origin main
            '''
        }
    }
}