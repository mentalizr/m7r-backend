package org.mentalizr.backend.rest.service;

import org.mentalizr.backend.rest.ResponseFactory;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

public abstract  class Service {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected HttpServletRequest httpServletRequest;
    protected Object serviceObjectRequest;

    public Service(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
        this.serviceObjectRequest = null;
    }

    public Service(HttpServletRequest httpServletRequest, Object serviceObjectRequest) {
        this.httpServletRequest = httpServletRequest;
        this.serviceObjectRequest = serviceObjectRequest;
    }

    protected abstract void logEntry();

    protected abstract void checkSecurityConstraints();

    protected abstract void checkPreconditions() throws DataSourceException, ServicePreconditionFailedException;

    protected abstract Object workLoad() throws DataSourceException, EntityNotFoundException;

    protected abstract void logLeave();

    public Response call() {

        try {
            logEntry();

            checkSecurityConstraints();

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
        } catch (RuntimeException e) {
            logger.error("RuntimeException: " + e.getMessage(), e);
            return ResponseFactory.internalServerError(e);
        } finally {
            logLeave();
        }
    }

}
