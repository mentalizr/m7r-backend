package org.mentalizr.backend.rest.service;

import org.mentalizr.backend.auth.PatientHttpSessionAttribute;
import org.mentalizr.backend.auth.TherapistHttpSessionAttribute;
import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.rest.RESTException;
import org.mentalizr.backend.rest.ResponseFactory;
import org.mentalizr.contentManager.exceptions.ContentManagerException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.io.IOException;

@SuppressWarnings("JavaDoc")
public abstract  class Service {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected HttpServletRequest httpServletRequest;
    protected Object serviceObjectRequest;
    protected UserHttpSessionAttribute userHttpSessionAttribute;

    public Service(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
        this.serviceObjectRequest = null;
    }

    public Service(HttpServletRequest httpServletRequest, Object serviceObjectRequest) {
        this.httpServletRequest = httpServletRequest;
        this.serviceObjectRequest = serviceObjectRequest;
    }

    protected abstract String getServiceId();

    protected void logEntry() {
        logger.trace("[" + getServiceId() + "] called.");
    }

    protected abstract UserHttpSessionAttribute checkSecurityConstraints();

    /**
     * Check business preconditions that are not yet checked as security constraints.
     *
     * @throws DataSourceException
     * @throws ServicePreconditionFailedException
     */
    protected void checkPreconditions() throws DataSourceException, ServicePreconditionFailedException {
    }

    protected abstract Object workLoad() throws DataSourceException, EntityNotFoundException, RESTException, ContentManagerException, IOException;

    protected void logLeave() {
        if (this.userHttpSessionAttribute != null) {
            String userId = this.userHttpSessionAttribute.getUserVO().getId();
            logger.debug("[" + getServiceId() + "][" + userId + "] completed.");
        } else {
            logger.debug("[" + getServiceId() + "] completed.");
        }
    }

    public Response call() {

        try {

            logEntry();

            this.userHttpSessionAttribute = checkSecurityConstraints();

            checkPreconditions();

            Object responseSO = workLoad();

            return ResponseFactory.ok(responseSO);

        } catch (ServicePreconditionFailedException e) {
            logger.warn("Service precondition failed: " + e.getMessage());
            return ResponseFactory.preconditionFailed(e.getMessage());
        } catch (DataSourceException e) {
            logger.error("DataSourceException: " + e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
        } catch (EntityNotFoundException e) {
            logger.error("EntityNotFoundException: " + e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
        } catch (RESTException e) {
            logger.error("RESTException: " + e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
        } catch (ContentManagerException e) {
            logger.error("ContentManagerException: " + e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
        } catch (IOException e) {
            logger.error("IOException: " + e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
        } catch (RuntimeException e) {
            logger.error("RuntimeException: " + e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
        } finally {
            logLeave();
        }
    }

    protected PatientHttpSessionAttribute getPatientHttpSessionAttribute() {
        return UserHttpSessionAttribute.asPatientHttpSessionAttribute(this.userHttpSessionAttribute);
    }

    protected TherapistHttpSessionAttribute getTherapistHttpSessionAttribute() {
        return UserHttpSessionAttribute.asTherapistHttpSessionAttribute(this.userHttpSessionAttribute);
    }

}
