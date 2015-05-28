package au.org.emii.netcdfsubset

import grails.validation.Validateable
import org.joda.time.*
import org.joda.time.format.*
import org.springframework.validation.Errors

import java.util.concurrent.*

class SubsetController {
    def subsetService
    def messageSource
    static def mutex = new Semaphore(1, true)

    def index(SubsetRequest subsetRequest) {
        log.debug("command: ${subsetRequest}")

        if (subsetRequest.hasErrors()) {
            render(status: 400, text: "Invalid request: ${getAllErrors(subsetRequest.errors)}")
            return
        }

        response.setContentType("application/octet-stream")
        response.setHeader("Content-disposition", "filename=${filenameToServe(subsetRequest)}")

        if (isThreadAvailable()) {
            try {
                getThreadLock()
                subsetService.subset(subsetRequest.typeName, subsetRequest.CQL_FILTER, response.outputStream)
            }
            catch (InterruptedException e) {
                log.info "The maximum number of concurrent jobs has been exceeded", e
                render status: 503, text: "The maximum number of concurrent jobs has been exceeded, please try again later"
            }
            catch (Exception e) {
                log.error "Unhandled exception during subset", e
                releaseThreadLock()
                render status: 500, text: "Invalid request for typeName: " + subsetRequest.typeName
            }
            finally {
                releaseThreadLock()
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

    private void getThreadLock() throws InterruptedException {
        mutex.acquire()
    }

    private void releaseThreadLock() {
        mutex.release()
    }

    private boolean isThreadAvailable() {
        return (mutex.availablePermits() > 0)
    }
}
