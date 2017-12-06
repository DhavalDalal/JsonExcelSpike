import com.wix.mysql.EmbeddedMysql
import com.wix.mysql.config.MysqldConfig
import groovy.sql.Sql

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql
import static com.wix.mysql.ScriptResolver.classPathScripts
import static com.wix.mysql.config.Charset.UTF8
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig
//import static com.wix.mysql.ScriptResolver.classPathScript
import static com.wix.mysql.distribution.Version.v5_7_latest

println("Downloading...")
MysqldConfig config = aMysqldConfig(v5_7_latest)
        .withCharset(UTF8)
        .withPort(3306)
        .withUser("dhaval", "")
        .build()

EmbeddedMysql mysqld = anEmbeddedMysql(config)
        .addSchema("test", classPathScripts("schema/*.sql"))
//        .addSchema("test", classPathScript("schema/01_createTableWithJsonColumn.sql"))
        .start()

println("Started...")
def sql = Sql.newInstance("jdbc:mysql://localhost:3306/test", "dhaval", "", 'com.mysql.cj.jdbc.Driver')

println("Executing query...")
sql.eachRow("SELECT * FROM SHEETS") { row ->
    println "$row.name has data => $row.data"
}

println("Stopping Mysql...")
mysqld.stop()
println("stopped!")