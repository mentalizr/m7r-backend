package org.mentalizr.backend.rest.service.assertPrecondition;

import org.mentalizr.backend.exceptions.InfrastructureException;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.PolicyDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.ProgramDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.PolicyPK;

public class AssertPolicy {

    public static void notExisting(String userId, String version) throws ServicePreconditionFailedException, InfrastructureException {
        try {
            PolicyDAO.load(new PolicyPK(userId, version));
            throw new ServicePreconditionFailedException("Policy [" + userId + ", " + version + "] is existing.");
        } catch (EntityNotFoundException e) {
            // DIN
        } catch (DataSourceException e) {
            throw new InfrastructureException(e.getMessage(), e);
        }
    }

}
