package org.mentalizr.backend.rest.service.assertPrecondition;

import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserDAO;

public class AssertUser {

    public static void existsNot(String userUUID) throws DataSourceException, ServicePreconditionFailedException {
        existsNot(userUUID, "User [%s] exists.");
    }

    public static void existsNot(String userUUID, String messageTemplate) throws DataSourceException, ServicePreconditionFailedException {
        try {
            UserDAO.load(userUUID);
            throw new ServicePreconditionFailedException(String.format(messageTemplate, userUUID));
        } catch (EntityNotFoundException e) {
            // do intentionally nothing
        }
    }

}
