package org.mentalizr.backend.rest.endpoints.generic;

import de.arthurpicht.utils.core.collection.Sets;
import de.arthurpicht.utils.core.strings.Strings;
import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.PatientAnonymous;
import org.mentalizr.backend.accessControl.roles.PatientLogin;
import org.mentalizr.backend.accessControl.roles.Therapist;
import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.exceptions.M7rIllegalServiceInputException;
import org.mentalizr.backend.htmlChunks.HtmlChunkCache;
import org.mentalizr.backend.htmlChunks.definitions.*;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageConverter;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageMongoHandler;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class HtmlChunkREST {

    private static final String SERVICE_ID = "generic/htmlChunk";

    @GET
    @Path(SERVICE_ID + "/{chunkName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response service(
            @PathParam("chunkName") String chunkName,
            @Context HttpServletRequest httpServletRequest
    ) {

        return new Service(httpServletRequest) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected Authorization checkSecurityConstraints()
                    throws UnauthorizedException, M7rIllegalServiceInputException {

                String chunkNameUpperCase = chunkName.toUpperCase();

                if (Strings.isOneOf(chunkNameUpperCase,
                        LoginHtmlChunk.NAME, LoginVoucherHtmlChunk.NAME, PolicyConsentHtmlChunk.NAME,
                        PolicyModalHtmlChunk.NAME, ImprintHtmlChunk.NAME))
                    return null;

                if (chunkNameUpperCase.equals(PatientHtmlChunk.NAME))
                    return AccessControl.assertValidSession(
                            Sets.newHashSet(PatientAnonymous.ROLE_NAME, PatientLogin.ROLE_NAME),
                            this.httpServletRequest);

                if (chunkNameUpperCase.equals(TherapistHtmlChunk.NAME))
                    return AccessControl.assertValidSession(Therapist.ROLE_NAME, this.httpServletRequest);

                throw new M7rIllegalServiceInputException("Unrecognized HtmlChunk requested: [" + chunkName + "].");
            }

            @Override
            protected String workLoad() {
                HtmlChunkCache htmlChunkCache = ApplicationContext.getHtmlChunkCache();
                return htmlChunkCache.getChunkAsString(chunkName.toUpperCase());
            }

            @Override
            protected void updateActivityStatus() {
                ActivityStatusMessageMongoHandler.insertOne(ActivityStatusMessageConverter
                        .convert(createMessageObject()));
            }

        }.call();

    }

}
