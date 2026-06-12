def jsonParse(String fileName){
    def result =[:]
    def jsonObj = readJSON file fileName
    result['name'] = jsonObj.name
    result['dev_version'] = jsonObj.version.dev
    result['qa_version'] = jsonObj.version.qa
    result['prod_version'] = jsonObj.version.prod
    result['description'] = jsonObj.description
    return result
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
    return writeJSON(file: fileName, json: payload, pretty: 4)
}