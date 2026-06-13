def parse( jsonObj, String envType){
    def info =[:]
    info['name'] = jsonObj.name
    info['dev_version'] = jsonObj.version.dev
    info['qa_version'] = jsonObj.version.qa
    info['prod_version'] = jsonObj.version.prod
    info['description'] = jsonObj.description
    def image = info["name"].toLowerCase().replaceAll(' ', '-')
    return [info, info["${envType}_version"], image]
}
def stringify(Map data){
    return groovy.json.JsonOutput.prettyPrint(groovy.json.JsonOutput.toJson([
        name: data['name'],
        version: [
            dev : data['dev_version'],
            qa  : data['qa_version'],
            prod: data['prod_version']
        ],
        description: data['description']
    ]))
}

def saveToJson(Map data, String fileName){
    def payload = [
        name: data['name'],
        version: [
            dev : data['dev_version'],
            qa  : data['qa_version'],
            prod: data['prod_version']
        ],
        description: data['description']
    ]
    writeJSON file: fileName, json: payload, pretty: 4
}