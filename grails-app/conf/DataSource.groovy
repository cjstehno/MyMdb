dataSource {
}

hibernate {
    cache.use_second_level_cache=true
    cache.use_query_cache=true
    cache.provider_class='net.sf.ehcache.hibernate.EhCacheProvider'
}

// environment specific settings
environments {
	development {
		dataSource {
			// dbCreate = "create-drop" // one of 'create', 'create-drop','update'
			dbCreate = "update"
            dialect = org.hibernate.dialect.HSQLDialect
			url = "jdbc:hsqldb:/home/cjstehno/hsql/mymdb/mymdb"
            pooled = true
            driverClassName = "org.hsqldb.jdbcDriver"
            username = "sa"
            password = ""
		}
	}
	test {
		dataSource {
			dbCreate = "create-drop"
			dialect = org.hibernate.dialect.HSQLDialect
            url = "jdbc:hsqldb:mem:testDb"
            pooled = true
            driverClassName = "org.hsqldb.jdbcDriver"
            username = "sa"
            password = ""
		}
	}
	production {
		dataSource {
            org.hibernate.dialect.MySQL5Dialect
			dbCreate = "update"
			jndiName = "java:comp/env/jdbc/MyMdb"
		}
	}
}