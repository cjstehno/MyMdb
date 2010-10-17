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
			dbCreate = "create-drop"
			url = "jdbc:hsqldb:mem:devDb"
                        pooled = true
                        driverClassName = "org.hsqldb.jdbcDriver"
                        username = "sa"
                        password = ""
		}
	}
	test {
		dataSource {
			dbCreate = "create-drop"
			url = "jdbc:hsqldb:mem:testDb"
                        pooled = true
                        driverClassName = "org.hsqldb.jdbcDriver"
                        username = "sa"
                        password = ""    
		}
	}
	production {
		dataSource {
			dbCreate = "update"
			jndiName = "java:comp/env/jdbc/MyMdb"
		}
	}
}