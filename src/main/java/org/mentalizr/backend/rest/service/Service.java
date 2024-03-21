package org.mentalizr.backend.rest.service;

import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.Const;
import org.mentalizr.backend.exceptions.M7rBusinessConstraintException;
import org.mentalizr.backend.exceptions.M7rIllegalServiceInputException;
import org.mentalizr.backend.exceptions.M7rInfrastructureException;
import org.mentalizr.backend.exceptions.M7rUnknownEntityException;
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
    protected Authorization authorization;

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

    protected abstract Authorization checkSecurityConstraints() throws UnauthorizedException, M7rIllegalServiceInputException;

    /**
     * Check business preconditions that are not yet checked as security constraints.
     *
     * @throws DataSourceException
     * @throws ServicePreconditionFailedException
     */
    protected void checkPreconditions() throws ServicePreconditionFailedException, M7rInfrastructureException {
    }

    protected abstract Object workLoad() throws RESTException, ContentManagerException, M7rInfrastructureException, IOException, DataSourceException, EntityNotFoundException, M7rIllegalServiceInputException, M7rUnknownEntityException, M7rBusinessConstraintException;

    protected void updateActivityStatus(){
    }

    protected void logLeave() {
        if (this.authorization != null) {
            String userId = this.authorization.getUserId();
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
            this.authorization = checkSecurityConstraints();
        } catch (UnauthorizedException e) {
            authLogger.warn("Authorization failed for service [" + getServiceId() + "]: " + e.getMessage());
            return ResponseFactory.unauthorized();
        } catch (M7rIllegalServiceInputException e) {
            return handleIllegalServiceInput(e);
        } catch (RuntimeException e) {
            logger.error("A RuntimeExceptioni occurred on executing method checkSecurityConstraints for service ["
                    + getServiceId() + "]: " + e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
        }

        try {
            this.checkPreconditions();
        } catch (ServicePreconditionFailedException e) {
            String message = "Service precondition failed for service [" + getServiceId() + "]: " + e.getMessage();
            logger.error(message, e);
            return ResponseFactory.preconditionFailed(message);
        } catch (M7rInfrastructureException e) {
            logger.error(e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
        } catch (RuntimeException e) {
            logger.error("A RuntimeException occurred on executing method checkSecurityConstraints for service ["
                    + getServiceId() + "]: " + e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
        }

        Object responseSO;
        try {
            responseSO = workLoad();
        } catch (RESTException | ContentManagerException | IOException | M7rInfrastructureException |
                 DataSourceException | RuntimeException e) {
            logger.error(getWorkloadExceptionMessage(e));
            return ResponseFactory.internalServerError(e);
        } catch (EntityNotFoundException | M7rUnknownEntityException e) {
            logger.error(getWorkloadExceptionMessage(e));
            return ResponseFactory.entityNotFound(e);
        } catch (M7rBusinessConstraintException e) {
            logger.error(getWorkloadExceptionMessage(e));
            return ResponseFactory.businessConstraintFailed(e);
        } catch (M7rIllegalServiceInputException e) {
            return handleIllegalServiceInput(e);
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

    private Response handleIllegalServiceInput(M7rIllegalServiceInputException e) {
        logger.error("A " + e.getClass().getSimpleName() + " occurred on executing method workload for service ["
                + getServiceId() + "]: " + e.getMessage());
        return ResponseFactory.badRequestError(e);
    }

    private String getWorkloadExceptionMessage(Exception e) {
        return "A " + e.getClass().getSimpleName() + " occurred on executing method workload for service ["
        + getServiceId() + "]: " + e.getMessage();
    }

}
