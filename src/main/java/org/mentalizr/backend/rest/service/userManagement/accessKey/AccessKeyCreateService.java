package org.mentalizr.backend.rest.service.userManagement.accessKey;

import org.mentalizr.backend.auth.AuthorizationService;
import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.rest.endpoints.EndpointUserManagementAccessKey;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertProgram;
import org.mentalizr.backend.rest.service.assertPrecondition.AssertRoleTherapist;
import org.mentalizr.backend.rest.serviceWorkload.userManagement.accessKey.PatientAccessKeyCreate;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.serviceObjects.userManagement.AccessKeyCollectionSO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyCreateSO;

import javax.servlet.http.HttpServletRequest;

public class AccessKeyCreateService extends Service {

    public static final String NAME = "create";

    public AccessKeyCreateService(HttpServletRequest httpServletRequest, AccessKeyCreateSO accessKeyCreateSO) {
        super(httpServletRequest, accessKeyCreateSO);
    }

    @Override
    protected String getServiceId() {
        return EndpointUserManagementAccessKey.PATH_PREFIX + "/" + NAME;
    }

    @Override
    protected UserHttpSessionAttribute checkSecurityConstraints() {
        return AuthorizationService.assertIsLoggedInAsAdmin(this.httpServletRequest);
    }

    @Override
    protected void checkPreconditions() throws DataSourceException, ServicePreconditionFailedException {

        AssertRoleTherapist.exists(
                getAccessKeyCreateSO().getTherapistId(),
                "Referenced therapist [%s] does not exist."
        );

        AssertProgram.exists(
                getAccessKeyCreateSO().getProgramId(),
                "Referenced program [%s] does not exist."
        );
    }

    @Override
    protected AccessKeyCollectionSO workLoad() throws DataSourceException {

        return PatientAccessKeyCreate.create(getAccessKeyCreateSO());
    }

    private AccessKeyCreateSO getAccessKeyCreateSO() {
        return (AccessKeyCreateSO) this.serviceObjectRequest;
    }
}
