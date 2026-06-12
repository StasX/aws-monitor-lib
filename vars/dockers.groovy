def build(repoOwner, image, version){
    echo "Building docker image..."
    sh "docker build -t docker.io/${repoOwner}/${image}:${version} ."
}

def tag(repoOwner, image, version){
    echo "Tagging docker image..."
    sh """
    docker tag \
    docker.io/${repoOwner}/${image}:${version} \
    docker.io/${repoOwner}/${image}:latest
    """
}
def login(){
    echo "Logging in to Docker registry..."
    withCredentials([usernamePassword(credentialsId: "dockerhub-creds", usernameVariable: "DOCKERHUB_USERNAME", passwordVariable: "DOCKERHUB_PASSWORD")]) {
        sh "docker login -u ${DOCKERHUB_USERNAME} -p ${DOCKERHUB_PASSWORD} docker.io"
    }
}
def push(repoOwner, image, version){
    echo "Pushing docker image to registry..."
    sh "docker push docker.io/${repoOwner}/${image}:${version}"
    sh "docker push  docker.io/${repoOwner}/${image}:latest"    
}
              
