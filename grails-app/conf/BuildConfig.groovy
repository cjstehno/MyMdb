import groovy.xml.MarkupBuilder
import groovy.util.ConfigSlurper

grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir	= "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits( "global" ) {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {        
        grailsPlugins()
        grailsHome()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        //mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        runtime 'com.h2database:h2:1.3.149'
    }

}

grails.war.resources = { stagingDir, args ->
    def myConfig = new ConfigSlurper().parse(new File('mymdb-config.groovy').toURL())

    // write the configured context.xml file for Tomcat
    new File("${stagingDir}/META-INF/context.xml").withWriter { w->
        w.write '<?xml version="1.0" encoding="UTF-8"?>\n'

        new MarkupBuilder( w ).Context {
            delegate.Resource(
                name:myConfig.mymdb.jndi.dataSource.name,
                auth:'Container',
                type:'javax.sql.DataSource',
                username:myConfig.mymdb.jndi.dataSource.user,
                password:myConfig.mymdb.jndi.dataSource.pass,
                driverClassName:myConfig.mymdb.jndi.dataSource.driver,
                url:myConfig.mymdb.jndi.dataSource.url,

                maxActive:myConfig.mymdb.jndi.dataSource.pool.maxActive,
                maxIdle:myConfig.mymdb.jndi.dataSource.pool.maxIdle,
                maxWait:myConfig.mymdb.jndi.dataSource.pool.maxWait,
                removeAbandoned:myConfig.mymdb.jndi.dataSource.pool.removeAbandoned,
                removeAbandonedTimeout:myConfig.mymdb.jndi.dataSource.pool.removeAbandonedTimeout,
                validationQuery:'select 1'
            )
        }
    }
}