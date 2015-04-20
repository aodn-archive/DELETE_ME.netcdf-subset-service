package au.org.emii.netcdfsubset

import grails.validation.Validateable
import org.joda.time.*
import org.joda.time.format.*
import org.springframework.validation.Errors

class SubsetController {
    def subsetService

    def messageSource

    def index(SubsetRequest subsetRequest) {
        log.debug("command: ${subsetRequest}")

        if (subsetRequest.hasErrors()) {
            render(status: 400, text: "Invalid request: ${getAllErrors(subsetRequest.errors)}")
            return
        }

        response.setContentType("application/octet-stream")
        response.setHeader("Content-disposition", "filename=${filenameToServe(subsetRequest)}")

        try {
            subsetService.subset(subsetRequest.typeName, subsetRequest.CQL_FILTER, response.outputStream)
        }
        catch (Exception e) {
            log.error "Unhandled exception during subset", e
        }
    }

    private String filenameToServe(subsetRequest) {
        DateTime subsetDateTime = new DateTime()
        String subsetDateTimeFormattedForFilename = ISODateTimeFormat.basicDateTime().print(subsetDateTime)

        return String.format(
            "%s-%s.zip",
            subsetRequest.typeName,
            subsetDateTimeFormattedForFilename
        )
    }

    private List getAllErrors(Errors errors) {
        return errors.allErrors.collect {error ->
            messageSource.getMessage(error, Locale.default)
        }
    }

}
