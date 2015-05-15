package au.org.emii.netcdfsubset

import au.org.emii.ncdfgenerator.NcdfGenerator
import grails.test.mixin.TestFor
import spock.lang.Specification
import javax.sql.DataSource
import java.sql.Connection

@TestFor(SubsetService)
class SubsetServiceSpec extends Specification {

    def ncdfGenerator
    def writeCallCount = 0
    def typeName = "anmn_ts"
    def dataSource
    def cqlFilter = "INTERSECTS()"
    def response = [:] as OutputStream

    def setup() {
        ncdfGenerator = new NcdfGenerator(null, null)
        ncdfGenerator.metaClass.write = { String typename, String filterExpr, Connection conn, OutputStream os ->
            assertEquals typeName, typename
            assertEquals cqlFilter, filterExpr
            assertEquals dataSource.connection, conn
            assertEquals response, os

            writeCallCount++
        }
        service._getGenerator = {-> ncdfGenerator}

        dataSource = Mock(DataSource)
        service.dataSource = dataSource
    }

    def "subset writes using a netcdf generator"() {
        when:
        service.subset(typeName, cqlFilter, response)

        then:
        assertEquals 1, writeCallCount
    }
}
