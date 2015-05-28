package au.org.emii.netcdfsubset

import org.joda.time.*
import org.joda.time.format.*
import org.springframework.validation.Errors

class SubsetController {
    def subsetService
    def messageSource
    static def jobCount = 0

    def index(SubsetRequest subsetRequest) {
        log.debug("command: ${subsetRequest}")

        if (subsetRequest.hasErrors()) {
            render(status: 400, text: "Invalid request: ${getAllErrors(subsetRequest.errors)}")
            return
        }

        response.setContentType("application/octet-stream")
        response.setHeader("Content-disposition", "filename=${filenameToServe(subsetRequest)}")

        if (registerJob(grailsApplication.config.ncdfgenerator.maxConcurrentJobs)) {
            try {
                subsetService.subset(subsetRequest.typeName, subsetRequest.CQL_FILTER, response.outputStream)
            }
            catch (Exception e) {
                log.error "Unhandled exception during subset", e
                render status: 500, text: "Invalid request for typeName: " + subsetRequest.typeName
            }
            finally {
                unregisterJob()
            }
        }
        else {
            log.info "The maximum number of concurrent jobs has been exceeded"
            render status: 503, text: "The maximum number of concurrent jobs has been exceeded, please try again later"
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

    private synchronized static boolean registerJob(maxConcurrentJobs) {
        if (jobCount < maxConcurrentJobs) {
            jobCount++
            return true
        }
        else {
            return false
        }
    }

    private synchronized static void unregisterJob() {
        if (jobCount > 0) {
            jobCount--
        }
    }
}
