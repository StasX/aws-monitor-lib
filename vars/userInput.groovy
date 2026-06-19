def choiceEnv(){
    def envType = input(
    id: 'Proceed', 
    message: 'Select which environment you want to deploy',
    parameters: [
        choice(
            name: 'ENVIRONMENT', 
            choices: ['Development', 'QA', 'Production'], 
            description: 'Select which environment You want to deploy'
        )
    ])
    def envShortType
    switch(envType){
        case 'Development' : 
            envShortType = "dev"
            break
        case 'QA' :
            envShortType = "qa"
            break
        case 'Production' :
            envShortType = "prod"
            break
        default :
            throw new Exception("Invalid  environment")
    }
    return [envShortType, envType]
}

def wrongConfiguration(){
    def userInput = input(
        message: 'The .app-info.json not match this application.  Do you want to continue with it?',
        ok: 'Yes',
        parameters: [
            choice(choices: ['Yes', 'No'], name: 'PROCEED_CHOICE')
        ]
    )
    if (userInput == 'No') {
        currentBuild.result = 'ABORTED'
        error("Build manually aborted.")
    }
}