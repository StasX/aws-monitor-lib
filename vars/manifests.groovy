def pull(String repo, String repoOwner, String branch) {
    echo "Cloning..."
    withEnv([
        "REPO=${repoOwner}/${repo}",
        "BRANCH=${branch}",
    ]) {
        sh '''
        git clone -b $BRANCH --single-branch "https://github.com/$REPO.git"
        '''
    }
}
def create(String envName, String envShortName, String gitOpsRepo, String appName, String dockerRepoOwner, String imageName, String tag){
    echo "Prepare HELM manifest for ${envName} environment..."
    if(envName=="Production"){
        sh """            
            rm -rf temp && \
            mkdir temp
            rm -rf manifests && \
            mkdir manifests
            cp chart/* -r temp/
            cp ${gitOpsRepo}/${envShortName}/values.yaml temp/
            helm template ${appName} ./temp \
            -n ${envShortName} \
            --set-string pod.image="${ dockerRepoOwner }/${imageName}" \
            --set-string pod.tag="${tag}" \
            --set-string pod.name="${appName}" \
            --set secret.enabled=false > manifests/app.yaml      
        """
        return 0
    }
    sh """            
    rm -rf temp && \
    mkdir temp
    rm -rf manifests && \
    mkdir manifests
    cp chart/* -r temp/
    cp ${gitOpsRepo}/${envShortName}/values.yaml temp/
    helm template ${appName} ./temp \
    --set-string pod.image="${ dockerRepoOwner }/${imageName}" \
    --set-string pod.tag="${tag}-${envShortName}" \
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
                mkdir -p $REPO/$APP_NAME/$ENV_SHORT_NAME
                cp manifests/app.yaml "$REPO/$APP_NAME/$ENV_SHORT_NAME"
                git -C "$REPO" config user.name "$GH_USER"
                git -C "$REPO" config user.email "$GIT_EMAIL"
                git -C "$REPO" add $APP_NAME/$ENV_SHORT_NAME/app.yaml
                git -C "$REPO" commit -m "Update application in $ENV_SHORT_NAME environment"
                git -C "$REPO" remote set-url origin https://x-access-token:$GH_TOKEN@github.com/$REPO_OWNER/$REPO.git
                git -C "$REPO" push origin main
                rm -r temp
            '''
        }
    }
}