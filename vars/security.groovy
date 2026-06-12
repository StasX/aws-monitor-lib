def banditScan (){
    sh """
    pip install --user bandit

    python -m bandit -r ./ -x ./.venv,./venv
    """
}
def checkovScan(){
    sh """
    python3 -m venv .venv
    . .venv/bin/activate
    pip install checkov
    .venv/bin/python -m checkov.main -f Dockerfile --framework dockerfile
    .venv/bin/python -m checkov.main -d ./chart --framework helm
    """
}
def semgrepScan(){
    sh """
    python3 -m venv .venv
    . .venv/bin/activate
    .venv/bin/python -m pip install semgrep
    .venv/bin/semgrep scan \
    --config=p/python \
    --config=p/dockerfile \
    --config=p/kubernetes \
    --config=p/github-actions \
    --metrics=off \
    --error
    """
}

def trivyScan(String repoOwner, String image, String tag){
    sh """
    docker run -v /var/run/docker.sock:/var/run/docker.sock \
    aquasec/trivy image ${repoOwner}/${image}:${tag} \
    --severity HIGH,CRITICAL \
    --exit-code 1
    """
}