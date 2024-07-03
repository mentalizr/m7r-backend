package org.mentalizr.backend.rest.service.assertPrecondition;

import org.mentalizr.backend.exceptions.M7rInfrastructureException;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.ProjectDAO;

public class AssertProject {

    public static void notExisting(String projectId) throws ServicePreconditionFailedException, M7rInfrastructureException {
        try {
            ProjectDAO.load(projectId);
            throw new ServicePreconditionFailedException("Project [" + projectId + "] already exists.");
        } catch (EntityNotFoundException e) {
            // DIN
        } catch (DataSourceException e) {
            throw new M7rInfrastructureException(e.getMessage(), e);
        }
    }

    public static void exists(String projectId) throws ServicePreconditionFailedException, M7rInfrastructureException {
        try {
            ProjectDAO.load(projectId);
        } catch (EntityNotFoundException e) {
            throw new ServicePreconditionFailedException(String.format("Project [%s] not existing.", projectId));
        } catch (DataSourceException e) {
            throw new M7rInfrastructureException(e.getMessage(), e);
        }
    }

}
