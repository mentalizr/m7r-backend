package org.mentalizr.backend.rest.service.assertPrecondition;

import org.mentalizr.backend.exceptions.M7rInfrastructureException;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.RoleTherapistDAO;

public class AssertRoleTherapist {

    public static void exists(String id) throws ServicePreconditionFailedException, M7rInfrastructureException {
        exists(id, "RoleTherapist with id [%s] does not exist.");
    }

    public static void exists(String id, String messageTemplate) throws ServicePreconditionFailedException, M7rInfrastructureException {
        try {
            RoleTherapistDAO.load(id);
        } catch (EntityNotFoundException e) {
            throw new ServicePreconditionFailedException(String.format(messageTemplate, id));
        } catch (DataSourceException e) {
            throw new M7rInfrastructureException(e.getMessage(), e);
        }
    }

}
