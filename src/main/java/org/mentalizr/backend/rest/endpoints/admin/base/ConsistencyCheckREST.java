package org.mentalizr.backend.rest.endpoints.admin.base;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.backend.rest.serviceWorkload.base.ConsistencyCheck;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.serviceObjects.base.ConsistencyCheckResultSO;
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
public class ConsistencyCheckREST {

    private static final String SERVICE_ID = "admin/base/consistencyCheck";
    private static final Logger log = LoggerFactory.getLogger(ConsistencyCheckREST.class);

    @GET
    @Path(SERVICE_ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Response clean(
            @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected Authorization checkSecurityConstraints() throws UnauthorizedException {
                return AccessControl.assertValidSession(Admin.ROLE_NAME, httpServletRequest);
            }

            @Override
            protected ConsistencyCheckResultSO workLoad() throws DataSourceException {
                log.info("consistency check requested");
                ConsistencyCheck consistencyCheck = new ConsistencyCheck();
                return consistencyCheck.getConsistencyCheckResultSO();
            }

        }.call();

    }

}
