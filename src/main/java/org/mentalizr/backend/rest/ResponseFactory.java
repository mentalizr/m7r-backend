package org.mentalizr.backend.rest;

import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.serviceObjects.ErrorSO;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;

public class ResponseFactory {

    private static final int ENTITY_NOT_FOUND = 470;
    private static final int ENTITY_PREEXISTING = 471;
    private static final int PRECONDITION_FAILED = 471;

    public static Response ok() {
        return Response.ok().build();
    }

    public static Response ok(Object serviceObject) {
        return Response.ok(serviceObject).build();
    }

    public static Response unauthorized() {
        return Response.status(Response.Status.UNAUTHORIZED).build();
//        return Response.status(401).entity("Unauthorized").build();
    }

    public static Response preconditionFailed(String message) {
        // TODO use official HTTP error code PRECONDITION_FAILED
        return Response.status(PRECONDITION_FAILED).entity(ErrorSO.withMessage(message)).build();
//        return Response.status(PRECONDITION_FAILED).build();
    }

    public static Response preconditionFailed(ServicePreconditionFailedException e) {
        // TODO use official HTTP error code PRECONDITION_FAILED
        return Response.status(PRECONDITION_FAILED).entity(ErrorSO.withMessage(e.getMessage())).build();
    }

    public static Response internalServerError(Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorSO.withMessage(e.getMessage())).build();
    }

}

