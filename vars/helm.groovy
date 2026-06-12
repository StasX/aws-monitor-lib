def create manifest(String envName, String envShortName, String gitOpsRepo, String appName, String dockerRepoOwner, String imageName, String tag ){
    echo "Prepare HELM manifest for ${envName} environment..."
    sh """                        
        rm -rf temp && \
        mkdir temp
        rm -rf manifests && \
        mkdir manifests
        cp chart/* -r temp/
        cp ${gitOpsRepo}/manifests/${appName}/${envShortName}/values.yaml temp/
        helm template ${appName} ./temp \
        --set-string pod.image="${ dockerRepoOwner }/${imageName}" \
        --set-string pod.tag="${tag}" \
        --set-string pod.name="${appName}" \
        --set secret.enabled=false > manifests/app.yaml
    """
}