package au.org.emii.netcdfsubset

import au.org.emii.netcdfsubset.SubsetController;
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(SubsetController)
class SubsetControllerSpec extends Specification {

    def setup() {
        controller.subsetService = Mock(SubsetService)
    }

    def "invalid subset request returns 400 error"() {
        when:
        controller.index()

        then:
        response.status == 400
        response.text == 'Invalid request: [Property [typeName] of class [class au.org.emii.netcdfsubset.SubsetRequest] cannot be null]'
    }

    def "valid subset request performs subset"() {
        given:
        params.typeName = 'layer_name'
        params.CQL_FILTER = 'cql_filter'

        when:
        controller.index()

        then:
        1 * controller.subsetService.subset('layer_name', 'cql_filter', response.outputStream)
        response.status == 200
    }
}
