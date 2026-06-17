def build(repoOwner, image, version, envName){
    echo "Building docker image..."
    if(envName != "dev" && envName != "qa"){
        sh "docker build -t docker.io/${repoOwner}/${image}:${version} ."
        return 0
    }
    sh "docker build -t docker.io/${repoOwner}/${image}:${version}-${envName} ."
}

def tag(repoOwner, image, version, envName){
    echo "Tagging docker image..."
    if(envName != "dev" && envName != "qa"){
        sh """
        docker tag \
        docker.io/${repoOwner}/${image}:${version} \
        docker.io/${repoOwner}/${image}:latest
        """
        return 0
    }
    sh """
    docker tag \
    docker.io/${repoOwner}/${image}:${version}-${envName} \
    docker.io/${repoOwner}/${image}:latest-${envName}
    """
}
def login(){
    echo "Logging in to Docker registry..."
    withCredentials([usernamePassword(credentialsId: "dockerhub-creds", usernameVariable: "DOCKERHUB_USERNAME", passwordVariable: "DOCKERHUB_PASSWORD")]) {
        sh "docker login -u ${DOCKERHUB_USERNAME} -p ${DOCKERHUB_PASSWORD} docker.io"
    }
}
def push(repoOwner, image, version, , envName){
    echo "Pushing docker image to registry..."
    if(envName != "dev" && envName != "qa"){
        sh "docker push docker.io/${repoOwner}/${image}:${version}"
        sh "docker push  docker.io/${repoOwner}/${image}:latest"
        return 0
    }
        sh "docker push docker.io/${repoOwner}/${image}:${version}-${envName}"
        sh "docker push  docker.io/${repoOwner}/${image}:latest-${envName}"
}
              
