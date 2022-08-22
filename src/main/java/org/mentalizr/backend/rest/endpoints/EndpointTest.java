package org.mentalizr.backend.rest.endpoints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Path;

@Path("v1")
public class EndpointTest {

    private static final Logger logger = LoggerFactory.getLogger(EndpointTest.class);

//    @GET
//    @Path("test/patient/formData/clean/{userId}")
//    public Response cleanFormData(
//            @PathParam("userId") String userId,
//            @Context HttpServletRequest httpServletRequest) {
//        logger.debug("[test/patient/formData/clean] [" + userId + "]");
//
//        assertIsLoggedInAsAdmin(httpServletRequest);
//
//        FormDataMongoHandler.clean(userId);
//
//        return ResponseFactory.ok();
//    }

}
