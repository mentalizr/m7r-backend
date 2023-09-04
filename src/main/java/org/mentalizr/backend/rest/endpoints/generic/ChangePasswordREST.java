package org.mentalizr.backend.rest.endpoints.generic;

import de.arthurpicht.utils.core.collection.Sets;
import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.M7rAuthorization;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.accessControl.roles.M7rUser;
import org.mentalizr.backend.accessControl.roles.PatientLogin;
import org.mentalizr.backend.accessControl.roles.Therapist;
import org.mentalizr.backend.exceptions.M7rIllegalServiceInputException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.utils.CredentialsSanity;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;
import org.mentalizr.persistence.rdbms.edao.UserLoginEDAO;
import org.mentalizr.persistence.rdbms.utils.Argon2Hash;
import org.mentalizr.serviceObjects.frontend.application.ChangePasswordSO;
import org.mentalizr.serviceObjects.userManagement.PatientAddSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class ChangePasswordREST {

    private static final String SERVICE_ID = "generic/changePassword";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @POST
    @Path(SERVICE_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response program(
            ChangePasswordSO changePasswordSO,
            @Context HttpServletRequest httpServletRequest
    ) {

        return new Service(httpServletRequest) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected Authorization checkSecurityConstraints() throws UnauthorizedException {
                return AccessControl.assertValidSession(
                        Sets.newHashSet(Admin.ROLE_NAME, PatientLogin.ROLE_NAME, Therapist.ROLE_NAME),
                        httpServletRequest);
            }

            @Override
            protected Object workLoad() throws M7rIllegalServiceInputException, DataSourceException {

                char[] newPassword = changePasswordSO.getPassword().toCharArray();
                try {
                    CredentialsSanity.checkPasswordSanity(newPassword);
                } catch (CredentialsSanity.BadCredentialsException e) {
                    throw new M7rIllegalServiceInputException(e.getMessage(), e);
                }

                String hash = Argon2Hash.getHash(newPassword);
                M7rAuthorization m7rAuthorization = new M7rAuthorization(this.authorization);
                M7rUser m7rUser = m7rAuthorization.getUserAsM7rUser();
                UserVO userVO = m7rUser.getUserVO();

                UserLoginEDAO.updatePasswordHash(userVO.getId(), hash);
                UserLoginEDAO.unsetRenewPasswordRequired(userVO.getId());

                return null;
            }
        }.call();

    }

}
