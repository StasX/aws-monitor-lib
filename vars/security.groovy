def banditScan (){
    sh "python -m bandit -r ./ -x ./.venv,./venv"
}

def checkovScan(String path, String pathFlag, String framework, String venv = ".venv") {
    sh "${venv}/bin/python -m checkov.main ${pathFlag} ${path} --framework ${framework}"
}

def semgrepScan(){
    sh """
    .venv/bin/semgrep scan \
    --config=p/python \
    --config=p/dockerfile \
    --config=p/kubernetes \
    --config=p/github-actions \
    --metrics=off \
    --error
    """
}

def trivyScanImage(String repoOwner, String image, String tag, envName, envShortName){
    if(envName=="Production"){
        sh """
        docker run -v /var/run/docker.sock:/var/run/docker.sock \
        aquasec/trivy image ${repoOwner}/${image}:${tag} \
        --severity HIGH,CRITICAL \
        --exit-code 1
        """
        return 0
    } 
    sh """
    docker run -v /var/run/docker.sock:/var/run/docker.sock \
    aquasec/trivy image ${repoOwner}/${image}:${tag}-${envShortName} \
    --severity HIGH,CRITICAL \
    --exit-code 1
    """
}
def trivyScanFile(String path, String file){
    sh"""
    docker run --rm \
    -v ${path}:/work \
    -w /work \
    aquasec/trivy:latest \
    config ${file} \
    --severity HIGH,CRITICAL \
    --exit-code 1
    """
} 
def kubeScoreScan(String path, String file){
    withEnv([
        "PATH=${path}",
        "FILE=${file}",
        ]) {
    sh '''
    docker run --rm \
      -v $PATH:/work \
      zegl/kube-score:latest \
      score /work/$FILE
    '''    
}
}