package org.mentalizr.backend.rest.endpoints.admin.formData;

import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.mongo.formData.FormDataDAO;
import org.mentalizr.serviceObjects.backup.FormDataCollectionSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.mentalizr.backend.auth.AuthorizationService.assertIsLoggedInAsAdmin;

@Path("v1")
public class GetAllFormDataREST {

    private final static String SERVICE_ID = "admin/formData/getAll";

    @GET
    @Path(SERVICE_ID + "/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(
            @PathParam("userId") String userId,
            @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected UserHttpSessionAttribute checkSecurityConstraints() {
                return assertIsLoggedInAsAdmin(httpServletRequest);
            }

            @Override
            protected FormDataCollectionSO workLoad() {
                return FormDataDAO.fetchAll(userId);
            }

        }.call();

    }

}
