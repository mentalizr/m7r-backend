package org.mentalizr.backend.rest.endpoints.generic;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.exceptions.IllegalServiceInputException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.serviceObjects.frontend.application.ChangePasswordSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class ConsentPolicyREST {

    private static final String SERVICE_ID = "generic/consentPolicy";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @GET
    @Path(SERVICE_ID)
    @Produces(MediaType.APPLICATION_JSON)
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
                return AccessControl.assertIntermediateSession(this.httpServletRequest);
            }

            @Override
            protected Object workLoad() throws IllegalServiceInputException, DataSourceException {

                // TODO
                throw new RuntimeException("NIY");

//                char[] newPassword = changePasswordSO.getPassword().toCharArray();
//                CredentialsSanity.checkPasswordSanity(newPassword);
//
//                String hash = Argon2Hash.getHash(newPassword);
//                UserVO userVO = this.userHttpSessionAttribute.getUserVO();
//
//                UserLoginEDAO.updatePasswordHash(userVO.getId(), hash);
//                UserLoginEDAO.unsetRenewPasswordRequired(userVO.getId());

//                return null;
            }
        }.call();

    }

}
