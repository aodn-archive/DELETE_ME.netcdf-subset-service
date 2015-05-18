package au.org.emii.netcdfsubset

import au.org.emii.ncdfgenerator.NcdfGenerator
import grails.test.mixin.TestFor
import spock.lang.Specification
import javax.sql.DataSource

@TestFor(SubsetService)
class SubsetServiceSpec extends Specification {

    def ncdfGenerator
    def typeName = "anmn_ts"
    def dataSource
    def cqlFilter = "INTERSECTS()"
    def response = [:] as OutputStream

    def setup() {
        ncdfGenerator = Mock(NcdfGenerator)
        service._getGenerator = {-> ncdfGenerator}

        dataSource = Mock(DataSource)
        service.dataSource = dataSource
    }

    def "subset writes using a netcdf generator"() {
        when:
        service.subset(typeName, cqlFilter, response)

        then:
        1 * ncdfGenerator.write(typeName, cqlFilter, dataSource.connection, response)
    }
}
