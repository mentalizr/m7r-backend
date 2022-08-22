package org.mentalizr.backend.rest.endpoints.admin.patientStatus;

import org.mentalizr.backend.security.auth.UnauthorizedException;
import org.mentalizr.backend.security.session.attributes.user.UserHttpSessionAttribute;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.mongo.patientStatus.PatientStatusDAO;
import org.mentalizr.serviceObjects.backup.PatientStatusCollectionSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.mentalizr.backend.security.auth.AuthorizationService.assertIsLoggedInAsAdmin;

@Path("v1")
public class GetAllPatientStatusREST {

    private final static String SERVICE_ID = "admin/patientStatus/getAll";

    @GET
    @Path(SERVICE_ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(
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
            protected PatientStatusCollectionSO workLoad() {
                return PatientStatusDAO.fetchAll();
            }

        }.call();

    }

}
