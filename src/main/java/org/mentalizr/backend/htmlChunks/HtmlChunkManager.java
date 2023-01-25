package org.mentalizr.backend.htmlChunks;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.PatientAnonymous;
import org.mentalizr.backend.accessControl.roles.PatientLogin;
import org.mentalizr.backend.accessControl.roles.Therapist;
import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.htmlChunks.definitions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

public class HtmlChunkManager {

    private final Logger logger = LoggerFactory.getLogger(HtmlChunkManager.class);

    private final HttpServletRequest httpServletRequest;
    private final HtmlChunkCache htmlChunkCache;

    public HtmlChunkManager(HttpServletRequest httpRequest) {
        this.httpServletRequest = httpRequest;
        this.htmlChunkCache = ApplicationContext.getHtmlChunkCache();
    }

    public InputStream getHtmlChunk(String chunkName) throws UnauthorizedException {

        logger.debug("getHtmlChunk for name: [" + chunkName + "]");

        chunkName = chunkName.toUpperCase();

//        if (chunkName.equals(HtmlChunkInit.NAME)) return getInitChunk();

        if (chunkName.equals(LoginHtmlChunk.NAME))
            return this.htmlChunkCache.getChunkAsInputStream(chunkName);

        if (chunkName.equals(LoginVoucherHtmlChunk.NAME))
            return this.htmlChunkCache.getChunkAsInputStream(chunkName);

        if (chunkName.equals(PolicyHtmlChunk.NAME))
            this.htmlChunkCache.getChunkAsInputStream(chunkName);

        Authorization authorization = AccessControl.assertValidSessionForAnyRole(this.httpServletRequest);
        String roleName = authorization.getRoleName();

        if (chunkName.equals(PatientHtmlChunk.NAME)) {
            if (!(roleName.equals(PatientAnonymous.ROLE_NAME) || roleName.equals(PatientLogin.ROLE_NAME)))
                throw new UnauthorizedException("UserLogin not in required role PATIENT.");
            return this.htmlChunkCache.getChunkAsInputStream(chunkName);
        }

        if (chunkName.equals(TherapistHtmlChunk.NAME)) {
            if (!(roleName.equals(Therapist.ROLE_NAME)))
                throw new UnauthorizedException("UserLogin not in required role THERAPIST.");
            return this.htmlChunkCache.getChunkAsInputStream(chunkName);
        }

        throw new RuntimeException("Implementation missing for HtmlChunk: " + chunkName);
    }

}
