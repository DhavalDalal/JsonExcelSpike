import groovy.sql.Sql
import org.json.JSONArray

class SheetsRepository {
    private final sql

    SheetsRepository(String url, String username, String password) {
        sql = Sql.newInstance(url, username, password, 'com.mysql.cj.jdbc.Driver')
    }

    def retrieveMetadataFor(int sheetid) {
        JSONArray metaData = null
        sql.eachRow("SELECT name, metadata FROM SHEETSMETADATA WHERE id = $sheetid") { row ->
            println("Parsing Meta Data Json... for sheet name => $row.name")
            metaData = new JSONArray(row.metaData)
        }
        println "metaData = $metaData"
        metaData
    }

    def retrieveValidatorsFor(int sheetid) {
        JSONArray metadata = retrieveMetadataFor(sheetid)
        validatorsFrom(metadata)
    }

    private def validatorsFrom(metaDataJson) {
        def types = metaDataJson[0] as List
        types.collect { type ->
            def (typeName, constraints) = type['type']
            switch (typeName) {
                case 'string': return new StringValidator(constraints)
                case 'set': return new SetValidator(constraints.toList())
                case 'integer': return new IntegerValidator(constraints)
                default: return new EmptyValidator(constraints)
            }
        }
    }


    def retrieveColumnsFor(int sheetid) {
        JSONArray metadata = retrieveMetadataFor(sheetid)
        metadata[1] as List
    }

    def retrieveDataFor(int sheetid) {
        JSONArray data = null
        sql.eachRow("SELECT * FROM SHEETS WHERE id = $sheetid") { row ->
            println("Parsing Data Json... for sheet id => $row.id")
            data = new JSONArray(row.data)
        }
        data
    }

    def retrieveRoleColumnConfigurationFor(int sheetid) {
        def roleColumnConfig = [:]
        sql.eachRow("SELECT role, columns FROM ROLESSHEETCOLUMNS WHERE sheetid = $sheetid") { row ->
            roleColumnConfig.put(row.role, (new JSONArray(row.columns)).toList())
        }
        println "roleColumnConfig = $roleColumnConfig"
        roleColumnConfig
    }
}
