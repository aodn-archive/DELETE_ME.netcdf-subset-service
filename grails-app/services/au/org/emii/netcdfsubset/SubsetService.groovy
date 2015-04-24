package au.org.emii.netcdfsubset

import au.org.emii.ncdfgenerator.*

class SubsetService {

    static transactional = false

    def dataSource
    def grailsApplication

    def subset(typeName, cqlFilter, response) {

        def generator = _getGenerator()

        try {
            _writeTemplateToLayerConfigDir(typeName)
            generator.write(typeName, cqlFilter, dataSource.connection, response)
        }
        catch (Exception e) {
            log.error "An error has occurred when generating netcdf file", e
        }
    }

    def _getGenerator = { ->
         new NcdfGenerator(
             grailsApplication.config.ncdfgenerator.layerConfigDir,
             System.getProperty('java.io.tmpdir')
        )
    }

    def _writeTemplateToLayerConfigDir(typeName) {
        def loader = this.class.classLoader
        def templateFilename = "${typeName}.xml"
        def url = loader.getResource("templates/${templateFilename}")

        if (!url) {
            throw new IllegalArgumentException("Template file not found: ${templateFilename}")
        }

        new FileOutputStream(new File(grailsApplication.config.ncdfgenerator.layerConfigDir, templateFilename), false) << url.text
    }
}
