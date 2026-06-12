def jsonParse(String fileName){
    def info =[:]
    def jsonObj = readJSON file fileName
    info['name'] = jsonObj.name
    info['dev_version'] = jsonObj.version.dev
    info['qa_version'] = jsonObj.version.qa
    info['prod_version'] = jsonObj.version.prod
    info['description'] = jsonObj.description
    return info
}
def jsonStringify(Map data){
    return JsonOutput.prettyPrint(JsonOutput.toJson([
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
    writeJSON(file: fileName, json: payload, pretty: 4)
}