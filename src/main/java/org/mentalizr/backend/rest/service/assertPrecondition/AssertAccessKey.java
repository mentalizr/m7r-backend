package org.mentalizr.backend.rest.service.assertPrecondition;

import org.mentalizr.backend.exceptions.M7rInfrastructureException;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserAccessKeyDAO;

public class AssertAccessKey {

    public static void isExistingWithAccessKey(String accessKey) throws ServicePreconditionFailedException, M7rInfrastructureException {
        try {
            UserAccessKeyDAO.findByUk_accessKey(accessKey);
        } catch (EntityNotFoundException e) {
            throw new ServicePreconditionFailedException("Access key [" + accessKey + "] does not exist.");
        } catch (DataSourceException e) {
            throw new M7rInfrastructureException(e.getMessage(), e);
        }

    }
}
