package org.mentalizr.backend.rest.endpoints.generic;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import de.arthurpicht.webAccessControl.securityAttribute.requirements.PolicyConsentRequirement;
import de.arthurpicht.webAccessControl.securityAttribute.requirements.Requirement;
import org.mentalizr.backend.accessControl.RequirementsFulfill;
import org.mentalizr.backend.accessControl.roles.M7rUser;
import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;
import org.mentalizr.persistence.rdbms.barnacle.vob.UserVOB;
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

    @GET
    @Path(SERVICE_ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Response service(
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
                    throw new ServicePreconditionFailedException(
                            "Inconsistency check failed. Staging does not require policy consent.");

                M7rUser m7rUser = (M7rUser) this.authorization.getUser();
                UserVOB userVOB = new UserVOB(m7rUser.getUserId());
                String policyVersion = ApplicationContext.getCurrentPolicyVersion();
                boolean policyConsented;
                try {
                    policyConsented = userVOB.hasPolicy(policyVersion);
                } catch (DataSourceException e) {
                    throw new RuntimeException(e);
                }
                if (policyConsented)
                    throw new ServicePreconditionFailedException(
                            "Inconsistency check failed. Policy consent already done.");
            }

            @Override
            protected String workLoad() throws DataSourceException {
                M7rUser m7rUser = (M7rUser) this.authorization.getUser();
                RequirementsFulfill.policyConsent(m7rUser);
                AccessControl.updateSession(this.httpServletRequest);
                return "OK";
            }

        }.call();

    }

}
