import org.json.JSONArray

class StringValidator {
    final int size

    StringValidator(size = -1) {
        this.size = size
    }

    def validate(String data) {
        size == -1 ? true : data.length() <= size
    }
}

class IntegerValidator {
    final int length

    IntegerValidator(length = 10) {
        this.length = length
    }

    def validate(int data) {
        data.toString().length() <= length
//        data.length() <= length
    }
}

class SetValidator {
    final List<String> validValues

    SetValidator(validValues) {
        this.validValues = validValues
    }

    def validate(String value) {
        validValues.contains(value)
    }
}

class EmptyValidator {
    EmptyValidator(constraints) {
    }

    def validate(String value) {
        false
    }
}


def metaDataJson = '''
[
   [{"type" : ["string", 6]}, { "type" : ["integer", 2]}, {"type" : ["set", ["one", "two", "three"]]}, { "type" : ["string", -1] }],
   ["name", "age", "likes", "description"]
]'''

println("Parsing Meta Data Json...")
JSONArray metaDataInfo = new JSONArray(metaDataJson)
println("Parsed MetaData Json!!")

def types = metaDataInfo[0] as List
def validators = types.collect { type ->
    def (typeName, constraints) = type['type']
    switch (typeName) {
        case 'string': return new StringValidator(constraints)
        case 'set': return new SetValidator(constraints.toList())
        case 'integer': return new IntegerValidator(constraints)
        default: return new EmptyValidator(constraints)
    }
}

println("Created Validators for types in Meta Data = $validators")
def columnNames = metaDataInfo[1]
println ("Column Names = $columnNames")


def jsonData = '''
[
   ["Anand", 12, "one", "enjoys travel"],
   ["Dhaval", 10, "two", "enjoys food"],
   ["Shameer", 35, "three", "enjoys cars"],
   ["Senthil", 100, "four", "enjoys books"]
]
'''

def validate(JSONArray data, validators) {
    data.findAll { JSONArray row ->
        row.withIndex().every { item, columnIdx ->
            validators[columnIdx].validate(item)
        }
    }
}

def withDisplayOrder(data, Closure displayOrder = {-> [:]}) {
    data.collect { JSONArray row ->
        def modifyDisplayOrder = displayOrder()
        if(modifyDisplayOrder) {
            JSONArray newRow = new JSONArray()
            row.withIndex(1).each { item, columnIdx ->
                newRow.put(modifyDisplayOrder[columnIdx] - 1, item)

            }
            return newRow
        } else {
            return row
        }
    }
}

JSONArray data = new JSONArray(jsonData)
def validData = validate(data, validators)
println("Valid Data = $validData")

def defaultOrder = withDisplayOrder(validData)
println("Default Order Data = $defaultOrder")

def newOrder = withDisplayOrder(validData) {
    [1:1, 2:3, 3:4, 4:2]
}
println("New Order Data = $newOrder")

// TODO: SETUP PIPELINE LATER
//data >> validate >> withDisplayOrder
