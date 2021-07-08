package org.mentalizr.backend.rest.service.userManagement.accessKey;

import org.mentalizr.backend.auth.AuthorizationService;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertAccessKey;
import org.mentalizr.backend.rest.serviceWorkload.userManagement.accessKey.PatientAccessKeyDelete;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.serviceObjects.userManagement.AccessKeyDeleteSO;

import javax.servlet.http.HttpServletRequest;

public class AccessKeyDeleteService extends Service {

    public AccessKeyDeleteService(HttpServletRequest httpServletRequest, AccessKeyDeleteSO accessKeyDeleteSO) {
        super(httpServletRequest, accessKeyDeleteSO);
    }

    @Override
    protected void logEntry() {
        logger.info("[userManagement:accessKey:delete] entry");
    }

    @Override
    protected void checkSecurityConstraints() {
        AuthorizationService.assertIsLoggedInAsAdmin(this.httpServletRequest);
    }

    @Override
    protected void checkPreconditions() throws DataSourceException, ServicePreconditionFailedException {
        AssertAccessKey.isExistingWithAccessKey(getAccessKeyDeleteSO().getAccessKey());
    }

    @Override
    protected Object workLoad() throws DataSourceException, EntityNotFoundException {
        PatientAccessKeyDelete.delete(getAccessKeyDeleteSO());
        return null;
    }

    @Override
    protected void logLeave() {
        logger.info("[userManagement:accessKey:delete] leave");
    }

    private AccessKeyDeleteSO getAccessKeyDeleteSO() {
        return (AccessKeyDeleteSO) this.serviceObjectRequest;
    }
}
