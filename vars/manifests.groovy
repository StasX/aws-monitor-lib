def pull(String repo, String repoOwner) {
    echo "Cloning..."
    withEnv([
        "REPO=${repoOwner}/${repo}"
    ]) {
        sh '''
            echo cloning "https://github.com/$REPO.git" 
            git clone "https://github.com/$REPO.git"
        '''
    }
}
def create(String envName, String envShortName, String gitOpsRepo, String appName, String dockerRepoOwner, String imageName, String tag ){
    echo "Prepare HELM manifest for ${envName} environment..."
    sh """            
        rm -rf temp && \
        mkdir temp
        rm -rf manifests && \
        mkdir manifests
        cp chart/* -r temp/
        cp ${gitOpsRepo}/${appName}/${envShortName}/values.yaml temp/
        helm template ${appName} ./temp \
        --set-string pod.image="${ dockerRepoOwner }/${imageName}" \
        --set-string pod.tag="${tag}" \
        --set-string pod.name="${appName}" \
        --set secret.enabled=false > manifests/app.yaml      
    """
}
def push ( String repo, String repoOwner, String appName, String envName, String envShortName, String email){
    echo "Deploying to ${envName}..."
    withCredentials([usernamePassword(credentialsId: 'github_creds', 
    usernameVariable: 'GH_USER', 
    passwordVariable: 'GH_TOKEN')]) {
        withEnv([
        "REPO_OWNER=${repoOwner}",
        "REPO=${repo}",
        "APP_NAME=${appName}",
        "ENV_SHORT_NAME=${envShortName}",
        "GIT_EMAIL=${email}"
        ]) {
            sh '''
                echo '======================================================================================='                        
                ls -la argo-gitops
                echo '======================================================================================='   
                mkdir -p argo-gitops/manifests/aws-monitor/dev
                cp manifests/app.yaml "$REPO/manifests/$APP_NAME/$ENV_SHORT_NAME"
                git -C "$REPO" config user.name "$GH_USER"
                git -C "$REPO" config user.email "$GIT_EMAIL"
                git -C "$REPO" add manifests/$APP_NAME/$ENV_SHORT_NAME/app.yaml
                git -C "$REPO" commit -m "Update application in $ENV_SHORT_NAME environment"
                git -C "$REPO" remote set-url origin https://x-access-token:$GH_TOKEN@github.com/$REPO_OWNER/$REPO.git
                git -C "$REPO" push origin main
                rm -r temp
            '''
        }
    }
}