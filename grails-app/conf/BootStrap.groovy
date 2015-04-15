class BootStrap {

    def grailsApplication
    def init = { servletContext ->
        log.info("App version: ${grailsApplication.metadata['app.version']}, " +
                 "build number: ${grailsApplication.metadata['app.buildNumber']}")
    }
}
