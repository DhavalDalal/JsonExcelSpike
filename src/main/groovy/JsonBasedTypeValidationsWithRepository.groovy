import org.json.JSONArray

EmbeddedMySQLServer dbserver = new EmbeddedMySQLServer(3306, 'dhaval', '')
dbserver.start('test', 'schema/*.sql')

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

def forRole(data, String role, roleColumnConfiguration) {
    def columns = roleColumnConfiguration[role]
    if(columns == null) {
        return new JSONArray()
    }

    if(columns == []) {
        return data
    }

    data.collect { JSONArray row ->
        JSONArray newRow = new JSONArray()
        columns.sort().each { wantedColumnIdx ->
            newRow.put(row.get(wantedColumnIdx-1))
        }
        return newRow
    }
}

SheetsRepository repository = new SheetsRepository('jdbc:mysql://localhost:3306/test', 'dhaval', '')
int sheetId = 1
println "columnNames = " + repository.retrieveColumnsFor(sheetId)
def validators = repository.retrieveValidatorsFor(sheetId)
def data = repository.retrieveDataFor(sheetId)

def validData = validate(data, validators)
println("Valid Data = $validData")

def defaultOrder = withDisplayOrder(validData)
println("Default Order Data = $defaultOrder")

def newOrder = withDisplayOrder(validData) {
    [1:1, 2:3, 3:4, 4:2]  //New Column Order
}

println("New Order Data = $newOrder")
def roleColumnConfiguration = repository.retrieveRoleColumnConfigurationFor(sheetId)
def roleBasedData = forRole(newOrder, 'toto', roleColumnConfiguration)
println("Role Based Data = $roleBasedData")


dbserver.stop()

// TODO: SETUP PIPELINE LATER
// data >> validate >> store
// retrieve >> forRole('foo') >> withDisplayOrder

