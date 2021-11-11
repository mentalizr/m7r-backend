package org.mentalizr.backend.rest.endpoints.admin.formData;

import org.mentalizr.backend.auth.UnauthorizedException;
import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.mongo.formData.FormDataMongoHandler;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import static org.mentalizr.backend.auth.AuthorizationService.assertIsLoggedInAsAdmin;

@Path("v1")
public class CleanFormDataREST {

    private static final String SERVICE_ID = "admin/formData/clean";

    @GET
    @Path(SERVICE_ID + "/{userId}")
    public Response clean(
            @PathParam("userId") String userId,
            @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected UserHttpSessionAttribute checkSecurityConstraints() throws UnauthorizedException {
                return assertIsLoggedInAsAdmin(httpServletRequest);
            }

            @Override
            protected Object workLoad() {
                FormDataMongoHandler.clean(userId);
                return null;
            }

        }.call();

    }

}
