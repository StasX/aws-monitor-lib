def build(String repoOwner, String image, String version, String envName, String envShortName){
    echo "Building docker image..."
    if(envName=="Production"){
        sh "docker build -t docker.io/${repoOwner}/${image}:${version} ."
        return 0
    }
    sh "docker build -t docker.io/${repoOwner}/${image}:${version}-${envShortName} ."
}

def tag(String repoOwner, String image, String version, String envName, String envShortName){
    echo "Tagging docker image..."
    if(envName=="Production"){
        sh """
        docker tag \
        docker.io/${repoOwner}/${image}:${version} \
        docker.io/${repoOwner}/${image}:latest
        """
        return 0
    }
    sh """
    docker tag \
    docker.io/${repoOwner}/${image}:${version}-${envShortName} \
    docker.io/${repoOwner}/${image}:latest-${envShortName}
    """
}
def login(){
    echo "Logging in to Docker registry..."
    withCredentials([usernamePassword(credentialsId: "dockerhub-creds", usernameVariable: "DOCKERHUB_USERNAME", passwordVariable: "DOCKERHUB_PASSWORD")]) {
        sh "docker login -u ${DOCKERHUB_USERNAME} -p ${DOCKERHUB_PASSWORD} docker.io"
    }
}
def push(String repoOwner, String image, String version, String envName, String envShortName){
    echo "Pushing docker image to registry..."
    if(envName=="Production"){
        sh """
        docker push docker.io/${repoOwner}/${image}:${version}
        docker push  docker.io/${repoOwner}/${image}:latest
        """
        return  0
    }
    sh """"
    docker push docker.io/${repoOwner}/${image}:${version}-${envShortName}
    docker push  docker.io/${repoOwner}/${image}:latest-${envShortName}
    """
}
              
