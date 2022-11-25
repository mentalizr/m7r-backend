package org.mentalizr.backend.rest.endpoints.generic;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import de.arthurpicht.webAccessControl.securityAttribute.requirements.PolicyConsentRequirement;
import de.arthurpicht.webAccessControl.securityAttribute.requirements.Requirement;
import org.mentalizr.backend.accessControl.RequirementsFulfill;
import org.mentalizr.backend.accessControl.roles.M7rUser;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;
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
            protected void checkPreconditions() throws ServicePreconditionFailedException {
                Requirement requirement = this.authorization.getNextRequirement();
                if (!(requirement instanceof PolicyConsentRequirement))
                    throw new ServicePreconditionFailedException("Inconsistency check failed. Staging does not " +
                            "require policy consent.");

                M7rUser m7rUser = (M7rUser) this.authorization.getUser();
                UserVO userVO = m7rUser.getUserVO();
                if (userVO.getPolicyConsent() != null && userVO.getPolicyConsent() > 0)
                    throw new ServicePreconditionFailedException("Inconsistency check failed. " +
                            "Policy consent already done.");

            }

            @Override
            protected Object workLoad() throws DataSourceException {
                M7rUser m7rUser = (M7rUser) this.authorization.getUser();
                RequirementsFulfill.policyConsent(m7rUser);
                AccessControl.updateSession(this.httpServletRequest);
                return null;
            }
        }.call();

    }

}
