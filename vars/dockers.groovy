def build(repoOwner, image, version, envName, envShortName){
    echo "Building docker image..."
    if(envName=="Production"){
        sh "docker build -t docker.io/${repoOwner}/${image}:${version} ."
    } else {
        sh "docker build -t docker.io/${repoOwner}/${image}:${version}-${envShortName} ."
    }
}

def tag(repoOwner, image, version, envName, envShortName){
    echo "Tagging docker image..."
    if(envName=="Production"){
        sh """
        docker tag \
        docker.io/${repoOwner}/${image}:${version} \
        docker.io/${repoOwner}/${image}:latest
        """
    } else {
        sh """
        docker tag \
        docker.io/${repoOwner}/${image}:${version}-${envShortName} \
        docker.io/${repoOwner}/${image}:latest-${envShortName}
        """
    }
}
def login(){
    echo "Logging in to Docker registry..."
    withCredentials([usernamePassword(credentialsId: "dockerhub-creds", usernameVariable: "DOCKERHUB_USERNAME", passwordVariable: "DOCKERHUB_PASSWORD")]) {
        sh "docker login -u ${DOCKERHUB_USERNAME} -p ${DOCKERHUB_PASSWORD} docker.io"
    }
}
def push(repoOwner, image, version, , envName, envShortName){
    echo "Pushing docker image to registry..."
    if(envName=="Production"){
        sh "docker push docker.io/${repoOwner}/${image}:${version}"
        sh "docker push  docker.io/${repoOwner}/${image}:latest"
    } else {
        sh "docker push docker.io/${repoOwner}/${image}:${version}-${envShortName}"
        sh "docker push  docker.io/${repoOwner}/${image}:latest-${envShortName}"
    }
}
              
