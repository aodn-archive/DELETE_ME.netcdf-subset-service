package au.org.emii.netcdfsubset

import grails.validation.Validateable

@Validateable
class SubsetRequest {
    String typeName
    String CQL_FILTER

    static constraints = {
        typeName(blank: false)
        CQL_FILTER(nullable: true)
    }
}
