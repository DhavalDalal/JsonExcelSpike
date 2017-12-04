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


def metaDataAndDataJson = '''
[
   [{"type" : ["string", 5]}, { "type" : ["integer", 3]}, {"type" : ["set", ["one", "two", "three"]]}, { "type" : ["string", -1] }],
   ["name", "age", "likes", "description"],
   ["Anand", 12, "one", "enjoys travel"],
   ["Dhaval", 15, "two", "enjoys food"],
   ["Shameer", 5, "three", "enjoys cars"],
   ["Senthil", 50000, "four", "enjoys books"]
]
'''


println("Parsing Json...")
JSONArray json = new JSONArray(metaDataAndDataJson)
println("Parsed Json!!")

def types = json[0] as List
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

def columnNames = json[1]
println ("Column Names = $columnNames")

def noOfRows = json.toList().size()
//println(noOfRows)
(2..(noOfRows-1)).each { rowIdx ->
    def row = json.get(rowIdx)
    println("row = $row")
    row.eachWithIndex{ item, idx ->
        println(validators[idx].validate(item))
    }
}