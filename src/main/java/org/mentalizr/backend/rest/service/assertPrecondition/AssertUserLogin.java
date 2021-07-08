package org.mentalizr.backend.rest.service.assertPrecondition;

import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserLoginDAO;

public class AssertUserLogin {

    public static void existsNotWithUsername(String username) throws ServicePreconditionFailedException, DataSourceException {
        existsNotWithUsername(username, "User [%s] exists.");
    }

    public static void existsNotWithUsername(String username, String messageTemplate) throws DataSourceException, ServicePreconditionFailedException {
        try {
            UserLoginDAO.findByUk_username(username);
            throw new ServicePreconditionFailedException(String.format(messageTemplate, username));
        } catch (EntityNotFoundException e) {
            // do intentionally nothing
        }
    }

}
