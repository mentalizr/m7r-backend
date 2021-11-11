package org.mentalizr.backend.rest.service.assertPrecondition;

import org.mentalizr.backend.exceptions.InfrastructureException;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.RoleTherapistDAO;

public class AssertRoleTherapist {

    public static void exists(String id) throws ServicePreconditionFailedException, InfrastructureException {
        exists(id, "RoleTherapist with id [%s] does not exist.");
    }

    public static void exists(String id, String messageTemplate) throws ServicePreconditionFailedException, InfrastructureException {
        try {
            RoleTherapistDAO.load(id);
        } catch (EntityNotFoundException e) {
            throw new ServicePreconditionFailedException(String.format(messageTemplate, id));
        } catch (DataSourceException e) {
            throw new InfrastructureException(e.getMessage(), e);
        }
    }

}
