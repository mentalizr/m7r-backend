package org.mentalizr.backend.rest.service.userManagement.loginPatient;

import org.mentalizr.backend.auth.AuthorizationService;
import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.rest.endpoints.EndpointUserManagementPatient;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.backend.rest.serviceWorkload.userManagement.accessKey.PatientAccessKeyGetAll;
import org.mentalizr.backend.rest.serviceWorkload.userManagement.loginPatient.LoginPatientGetAll;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.serviceObjects.userManagement.AccessKeyCollectionSO;
import org.mentalizr.serviceObjects.userManagement.PatientRestoreCollectionSO;

import javax.servlet.http.HttpServletRequest;

public class LoginPatientGetAllService extends Service {

    public LoginPatientGetAllService(HttpServletRequest httpServletRequest) {
        super(httpServletRequest);
    }

    @Override
    protected String getServiceId() {
        return EndpointUserManagementPatient.PATH_PREFIX + "/getAll";
    }

    @Override
    protected UserHttpSessionAttribute checkSecurityConstraints() {
        return AuthorizationService.assertIsLoggedInAsAdmin(this.httpServletRequest);
    }

    @Override
    protected void checkPreconditions() throws DataSourceException, ServicePreconditionFailedException {
    }

    @Override
    protected PatientRestoreCollectionSO workLoad() throws DataSourceException, EntityNotFoundException {
        return LoginPatientGetAll.getAll();
    }

}
