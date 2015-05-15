package au.org.emii.netcdfsubset

import au.org.emii.ncdfgenerator.NcdfGenerator
import grails.test.mixin.TestFor
import spock.lang.Specification
import javax.sql.DataSource
import java.sql.Connection

@TestFor(SubsetService)
class SubsetServiceSpec extends Specification {

    def ncdfGenerator
    def writeCallCount
    def typeName
    def dataSource
    def cqlFilter
    def response

    def setup() {
        typeName = "anmn_ts"
        cqlFilter = "INTERSECTS()"
        response = new OutputStream() {
            @Override
            void write(int b) throws IOException {}
        }

        ncdfGenerator = new NcdfGenerator(null, null)
        writeCallCount = 0
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
