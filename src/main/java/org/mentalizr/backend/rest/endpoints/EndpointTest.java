package org.mentalizr.backend.rest.endpoints;

import org.mentalizr.backend.rest.ResponseFactory;
import org.mentalizr.persistence.mongo.formData.FormDataMongoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import static org.mentalizr.backend.auth.AuthorizationService.assertIsLoggedInAsAdmin;

@Path("v1")
public class EndpointTest {

    private static final Logger logger = LoggerFactory.getLogger(EndpointTest.class);

    @GET
    @Path("test/patient/formData/clean/{userId}")
    public Response cleanFormData(
            @PathParam("userId") String userId,
            @Context HttpServletRequest httpServletRequest) {
        logger.debug("[test/patient/formData/clean] [" + userId + "]");

        assertIsLoggedInAsAdmin(httpServletRequest);

        FormDataMongoHandler.clean(userId);

        return ResponseFactory.ok();
    }

}
