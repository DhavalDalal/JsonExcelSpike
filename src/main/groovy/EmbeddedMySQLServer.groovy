import com.wix.mysql.EmbeddedMysql
import com.wix.mysql.config.MysqldConfig

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql
import static com.wix.mysql.ScriptResolver.classPathScripts
import static com.wix.mysql.config.Charset.UTF8
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig
import static com.wix.mysql.distribution.Version.v5_7_latest

class EmbeddedMySQLServer {
    private final MysqldConfig config
    private EmbeddedMysql mysqld

    EmbeddedMySQLServer(int port, String username, String password) {
        println "Building.EmbeddedMySQLServer..."
        config = aMysqldConfig(v5_7_latest)
                .withCharset(UTF8)
                .withPort(port)
                .withUser(username, password)
                .build()
        println "Building.EmbeddedMySQLServer Complete!"
    }

    def start(String schemaName, String classPathScriptsPattern) {
        println "EmbeddedMySQLServer.starting..."
        mysqld = anEmbeddedMysql(config)
                .addSchema(schemaName, classPathScripts(classPathScriptsPattern))
//        .addSchema("test", classPathScript("schema/01_createSheetsMetaDataAndSheetsTables.sql"))
                .start()
        println "EmbeddedMySQLServer.started!"
    }

    def stop() {
        println "EmbeddedMySQLServer.stop"
        mysqld.stop()
    }
}
