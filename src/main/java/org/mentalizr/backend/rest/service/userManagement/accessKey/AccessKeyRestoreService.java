package org.mentalizr.backend.rest.service.userManagement.accessKey;

import org.mentalizr.backend.auth.AuthorizationService;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertProgram;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertRoleTherapist;
import org.mentalizr.backend.rest.serviceWorkload.userManagement.accessKey.PatientAccessKeyRestore;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.serviceObjects.userManagement.AccessKeyRestoreSO;

import javax.servlet.http.HttpServletRequest;

public class AccessKeyRestoreService extends Service {

    public AccessKeyRestoreService(HttpServletRequest httpServletRequest, AccessKeyRestoreSO accessKeyRestoreSO) {
        super(httpServletRequest, accessKeyRestoreSO);
    }

    @Override
    protected void logEntry() {
        logger.info("[userManagement:accessKey:restore] entry");
    }

    @Override
    protected void checkSecurityConstraints() {
        AuthorizationService.assertIsLoggedInAsAdmin(this.httpServletRequest);
    }

    @Override
    protected void checkPreconditions() throws DataSourceException, ServicePreconditionFailedException {

        AssertRoleTherapist.exists(
                getAccessKeyRestoreSO().getTherapistId(),
                "Referenced therapist [%s] not existing."
        );

        AssertProgram.exists(
                getAccessKeyRestoreSO().getProgramId(),
                "Referenced program [%s] not existing."
        );
    }

    @Override
    protected Object workLoad() throws DataSourceException {

        PatientAccessKeyRestore.restore(getAccessKeyRestoreSO());
        return null;
    }

    @Override
    protected void logLeave() {
        logger.info("[userManagement:accessKey:restore] leave");
    }

    private AccessKeyRestoreSO getAccessKeyRestoreSO() {
        return (AccessKeyRestoreSO) this.serviceObjectRequest;
    }
}
