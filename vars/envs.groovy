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