package org.mentalizr.backend.htmlChunks;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.PatientAnonymous;
import org.mentalizr.backend.accessControl.roles.PatientLogin;
import org.mentalizr.backend.accessControl.roles.Therapist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

public class HtmlChunkManager {

    private final Logger logger = LoggerFactory.getLogger(HtmlChunkManager.class);

    private final HttpServletRequest httpServletRequest;
    private final HtmlChunkRegistry htmlChunkRegistry;

    public HtmlChunkManager(HttpServletRequest httpRequest) throws IOException {
        this.httpServletRequest = httpRequest;
        this.htmlChunkRegistry = new HtmlChunkRegistry(httpRequest.getServletContext());
    }

    public InputStream getHtmlChunk(String chunkName) throws UnauthorizedException, IOException {

        logger.debug("getHtmlChunk for name: [" + chunkName + "]");

        chunkName = chunkName.toUpperCase();

        if (chunkName.equals(HtmlChunkInit.NAME)) return getInitChunk();

        if (chunkName.equals(HtmlChunkLogin.NAME)) return getLoginChunk();

        if (chunkName.equals(HtmlChunkLoginVoucher.NAME)) return getLoginVoucherChunk();

        if (chunkName.equals(HtmlChunkPolicy.NAME)) return getPolicyChunk();

        Authorization authorization = AccessControl.assertValidSessionForAnyRole(this.httpServletRequest);
        String roleName = authorization.getRoleName();

        if (chunkName.equals(HtmlChunkPatient.NAME)) {
            if (!(roleName.equals(PatientAnonymous.ROLE_NAME) || roleName.equals(PatientLogin.ROLE_NAME)))
                throw new UnauthorizedException("UserLogin not in required role PATIENT.");
            return getPatientChunk();
        }

        if (chunkName.equals(HtmlChunkTherapist.NAME)) {
            if (!(roleName.equals(Therapist.ROLE_NAME)))
                throw new UnauthorizedException("UserLogin not in required role THERAPIST.");
            return getTherapistChunk();
        }

        throw new RuntimeException("Implementation missing for HtmlChunk: " + chunkName);

    }

    private InputStream getInitChunk() throws IOException {
        return this.htmlChunkRegistry.getChunk(HtmlChunkInit.NAME).asInputStream();
    }

    private InputStream getPatientChunk() throws IOException {
        return this.htmlChunkRegistry.getChunk(HtmlChunkPatient.NAME).asInputStream();
    }

    private InputStream getTherapistChunk() throws IOException {
        return this.htmlChunkRegistry.getChunk(HtmlChunkTherapist.NAME).asInputStream();
    }

    private InputStream getLoginChunk() throws IOException {
        return this.htmlChunkRegistry.getChunk(HtmlChunkLogin.NAME).asInputStream();
    }

    private InputStream getLoginVoucherChunk() throws IOException {
        return this.htmlChunkRegistry.getChunk(HtmlChunkLoginVoucher.NAME).asInputStream();
    }

    private InputStream getPolicyChunk() throws IOException {
        return this.htmlChunkRegistry.getChunk(HtmlChunkPolicy.NAME).asInputStream();
    }

}
