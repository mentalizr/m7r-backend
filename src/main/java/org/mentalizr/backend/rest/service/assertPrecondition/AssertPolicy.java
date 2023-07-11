package org.mentalizr.backend.rest.service.assertPrecondition;

import org.mentalizr.backend.exceptions.M7rInfrastructureException;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.PolicyConsentDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.PolicyConsentPK;

public class AssertPolicy {

    public static void notExisting(String userId, String version) throws ServicePreconditionFailedException, M7rInfrastructureException {
        try {
            PolicyConsentDAO.load(new PolicyConsentPK(userId, version));
            throw new ServicePreconditionFailedException("Policy [" + userId + ", " + version + "] is existing.");
        } catch (EntityNotFoundException e) {
            // DIN
        } catch (DataSourceException e) {
            throw new M7rInfrastructureException(e.getMessage(), e);
        }
    }

}
