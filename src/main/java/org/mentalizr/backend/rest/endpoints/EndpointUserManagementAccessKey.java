package org.mentalizr.backend.rest.endpoints;

import org.mentalizr.backend.rest.service.userManagement.accessKey.AccessKeyCreateService;
import org.mentalizr.backend.rest.service.userManagement.accessKey.AccessKeyDeleteService;
import org.mentalizr.backend.rest.service.userManagement.accessKey.AccessKeyGetAllService;
import org.mentalizr.backend.rest.service.userManagement.accessKey.AccessKeyRestoreService;
import org.mentalizr.serviceObjects.userManagement.AccessKeyCreateSO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyDeleteSO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyRestoreSO;
import org.mentalizr.serviceObjects.userManagement.PatientRestoreSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(EndpointUserManagementAccessKey.PATH_PREFIX)
public class EndpointUserManagementAccessKey {

    public static final String PATH_PREFIX = "v1/admin/user/accessKey";

    private static final Logger logger = LoggerFactory.getLogger(EndpointUserManagementAccessKey.class);

    @POST
    @Path(AccessKeyCreateService.NAME)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(AccessKeyCreateSO accessKeyCreateSO,
                           @Context HttpServletRequest httpServletRequest) {
        return new AccessKeyCreateService(httpServletRequest, accessKeyCreateSO).call();
    }

    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(
            @Context HttpServletRequest httpServletRequest
    ) {

        return new AccessKeyGetAllService(httpServletRequest).call();
    }

    @POST
    @Path("restore")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response restore(AccessKeyRestoreSO accessKeyRestoreSO,
                            @Context HttpServletRequest httpServletRequest) {

        return new AccessKeyRestoreService(httpServletRequest, accessKeyRestoreSO).call();
    }

    @GET
    @Path("get/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(
            @PathParam("username") String username,
            @Context HttpServletRequest httpServletRequest
    ) {

        logger.error("NIY");
        throw new RuntimeException("NIY");
    }

    @POST
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(
            AccessKeyDeleteSO accessKeyDeleteSO,
            @Context HttpServletRequest httpServletRequest
    ) {

        return new AccessKeyDeleteService(httpServletRequest, accessKeyDeleteSO).call();
    }

}
