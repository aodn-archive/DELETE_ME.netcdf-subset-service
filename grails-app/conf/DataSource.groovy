dataSource {
    pooled = true
}

// environment specific settings
def env = System.getenv()

environments {
    development {
        dataSource {
            driverClassName = "org.postgresql.Driver"
            url = env['DATA_SOURCE_URL']
            username = env['DATA_SOURCE_USERNAME']
            password = env['DATA_SOURCE_PASSWORD']
        }
    }

    production {
        dataSource {
            jndiName = "java:comp/env/jdbc/harvest_read"
        }
    }
}
