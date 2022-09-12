package org.mentalizr.backend.rest.service;

import org.mentalizr.backend.Const;
import org.mentalizr.backend.exceptions.IllegalServiceInputException;
import org.mentalizr.backend.security.session.attributes.SecurityAttribute;
import org.mentalizr.backend.security.session.attributes.user.PatientHttpSessionAttribute;
import org.mentalizr.backend.security.session.attributes.user.TherapistHttpSessionAttribute;
import org.mentalizr.backend.security.auth.UnauthorizedException;
import org.mentalizr.backend.security.session.attributes.user.UserHttpSessionAttribute;
import org.mentalizr.backend.exceptions.InfrastructureException;
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
    private static final Logger authLogger = Const.authLogger;

    protected HttpServletRequest httpServletRequest;
    protected Object serviceObjectRequest;
    protected SecurityAttribute securityAttribute;

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

    protected abstract SecurityAttribute checkSecurityConstraints() throws UnauthorizedException;

    /**
     * Check business preconditions that are not yet checked as security constraints.
     *
     * @throws DataSourceException
     * @throws ServicePreconditionFailedException
     */
    protected void checkPreconditions() throws ServicePreconditionFailedException, InfrastructureException {
    }

    protected abstract Object workLoad() throws RESTException, ContentManagerException, InfrastructureException, IOException, DataSourceException, EntityNotFoundException, IllegalServiceInputException;

    protected void updateActivityStatus(){
    }

    protected void logLeave() {
        if (this.securityAttribute != null) {
            String userId = this.securityAttribute.getUserHttpSessionAttribute().getUserVO().getId();
            logger.debug("[" + getServiceId() + "][" + userId + "] completed.");
        } else {
            logger.debug("[" + getServiceId() + "] completed.");
        }
    }

    public Response call() {

        try {
            logEntry();
        } catch (RuntimeException e) {
            logger.error("A RuntimeException occurred on executing method logEntry for service [" + getServiceId() + "]: " + e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
        }

        try {
            this.securityAttribute = checkSecurityConstraints();
        } catch (UnauthorizedException e) {
            authLogger.warn("Authorization failed for service [" + getServiceId() + "]: " + e.getMessage());
            return ResponseFactory.unauthorized();
        }

        try {
            this.checkPreconditions();
        } catch (ServicePreconditionFailedException e) {
            String message = "Service precondition failed for service [" + getServiceId() + "]: " + e.getMessage();
            logger.error(message, e);
            return ResponseFactory.preconditionFailed(message);
        } catch (InfrastructureException e) {
            logger.error(e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
        }

        Object responseSO;
        try {
            responseSO = workLoad();
        } catch (RESTException | ContentManagerException | IOException | InfrastructureException |
                 DataSourceException e) {
            logger.error("A " + e.getClass().getSimpleName() + " occurred on executing method workload for service ["
                    + getServiceId() + "]: " + e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
        } catch (EntityNotFoundException e) {
            logger.error("A " + e.getClass().getSimpleName() + " occurred on executing method workload for service ["
                    + getServiceId() + "]: " + e.getMessage());
            return ResponseFactory.entityNotFound(e);
        } catch (IllegalServiceInputException e) {
            logger.error("A " + e.getClass().getSimpleName() + " occurred on executing method workload for service ["
                    + getServiceId() + "]: " + e.getMessage());
            return ResponseFactory.badRequestError(e);
        }

        try {
            updateActivityStatus();
        } catch (RuntimeException e) {
            logger.error("A RuntimeException occurred on executing method updateActivityStatus for service [" + getServiceId() + "]: " + e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
        }

        try {
            logLeave();
        } catch (RuntimeException e) {
            logger.error("A RuntimeException occurred on executing method logLeave for service [" + getServiceId() + "]: " + e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
        }

        return ResponseFactory.ok(responseSO);
    }

    protected PatientHttpSessionAttribute getPatientHttpSessionAttribute() {
        UserHttpSessionAttribute userHttpSessionAttribute = this.securityAttribute.getUserHttpSessionAttribute();
        return UserHttpSessionAttribute
                .asPatientHttpSessionAttribute(userHttpSessionAttribute);
    }

    protected TherapistHttpSessionAttribute getTherapistHttpSessionAttribute() {
        UserHttpSessionAttribute userHttpSessionAttribute = this.securityAttribute.getUserHttpSessionAttribute();
        return UserHttpSessionAttribute.asTherapistHttpSessionAttribute(userHttpSessionAttribute);
    }

}
