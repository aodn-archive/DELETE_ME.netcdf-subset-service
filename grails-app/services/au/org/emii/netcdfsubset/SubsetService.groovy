package au.org.emii.netcdfsubset

import au.org.emii.ncdfgenerator.NcdfGenerator

class SubsetService {

    def dataSource
    def grailsApplication

    def subset(typeName, cqlFilter, response) {

        NcdfGenerator generator = new NcdfGenerator(
            grailsApplication.config.netcdf_filters.layer_config_dir,
            System.getProperty('java.io.tmpdir')
        )

        generator.write(typeName, cqlFilter, dataSource.connection, response)
    }
}
