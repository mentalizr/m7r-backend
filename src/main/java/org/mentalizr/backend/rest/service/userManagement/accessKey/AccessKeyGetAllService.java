package org.mentalizr.backend.rest.service.userManagement.accessKey;

import org.mentalizr.backend.auth.AuthorizationService;
import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.backend.rest.serviceWorkload.userManagement.accessKey.PatientAccessKeyGetAll;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.serviceObjects.userManagement.AccessKeyCollectionSO;

import javax.servlet.http.HttpServletRequest;

public class AccessKeyGetAllService extends Service {

    public AccessKeyGetAllService(HttpServletRequest httpServletRequest) {
        super(httpServletRequest);
    }

    @Override
    protected String getServiceId() {
        return "admin/user/accessKey/getAll";
    }

    @Override
    protected UserHttpSessionAttribute checkSecurityConstraints() {
        return AuthorizationService.assertIsLoggedInAsAdmin(this.httpServletRequest);
    }

    @Override
    protected void checkPreconditions() throws DataSourceException, ServicePreconditionFailedException {
    }

    @Override
    protected AccessKeyCollectionSO workLoad() throws DataSourceException, EntityNotFoundException {
        return PatientAccessKeyGetAll.getAll();
    }

}
