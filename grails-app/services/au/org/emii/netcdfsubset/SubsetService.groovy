package au.org.emii.netcdfsubset

import au.org.emii.ncdfgenerator.*

class SubsetService {

    def dataSource
    def grailsApplication

    def subset(typeName, cqlFilter, response) {

        def generator = _getGenerator()

        try {
            generator.write(typeName, cqlFilter, dataSource.connection, response)
        }
        catch (Exception e) {
            log.error "An error has occurred when writing netcdf file", e
        }
    }

    def _getGenerator = { ->
         new NcdfGenerator(
                grailsApplication.config.netcdf_filters.layer_config_dir,
                System.getProperty('java.io.tmpdir')
        )
    }
}
