package org.mentalizr.backend.htmlChunks;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.PatientAnonymous;
import org.mentalizr.backend.accessControl.roles.PatientLogin;
import org.mentalizr.backend.accessControl.roles.Therapist;
import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.htmlChunks.definitions.*;
import org.mentalizr.backend.htmlChunks.producer.HtmlChunkProducer;
import org.mentalizr.backend.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class HtmlChunkManager {

    private final Logger logger = LoggerFactory.getLogger(HtmlChunkManager.class);

    private final HttpServletRequest httpServletRequest;
    private final HtmlChunkCache htmlChunkCache;

    public HtmlChunkManager(HttpServletRequest httpRequest) throws IOException {
        this.httpServletRequest = httpRequest;
        this.htmlChunkCache = ApplicationContext.getHtmlChunkCache();
    }

    public InputStream getHtmlChunk(String chunkName) throws UnauthorizedException, IOException {

        logger.debug("getHtmlChunk for name: [" + chunkName + "]");

        chunkName = chunkName.toUpperCase();

//        if (chunkName.equals(HtmlChunkInit.NAME)) return getInitChunk();

        if (chunkName.equals(LoginHtmlChunk.NAME)) return getChunk(LoginHtmlChunk.NAME);

        if (chunkName.equals(LoginVoucherHtmlChunk.NAME)) return getChunk(LoginVoucherHtmlChunk.NAME);

        if (chunkName.equals(PolicyHtmlChunk.NAME)) return getChunk(PolicyHtmlChunk.NAME);

        Authorization authorization = AccessControl.assertValidSessionForAnyRole(this.httpServletRequest);
        String roleName = authorization.getRoleName();

        if (chunkName.equals(PatientHtmlChunk.NAME)) {
            if (!(roleName.equals(PatientAnonymous.ROLE_NAME) || roleName.equals(PatientLogin.ROLE_NAME)))
                throw new UnauthorizedException("UserLogin not in required role PATIENT.");
            return getChunk(PatientHtmlChunk.NAME);
        }

        if (chunkName.equals(TherapistHtmlChunk.NAME)) {
            if (!(roleName.equals(Therapist.ROLE_NAME)))
                throw new UnauthorizedException("UserLogin not in required role THERAPIST.");
            return getChunk(TherapistHtmlChunk.NAME);
        }

        throw new RuntimeException("Implementation missing for HtmlChunk: " + chunkName);
    }

//    private InputStream getInitChunk() {
//        String htmlChunk = this.htmlChunkCache.getHtmlChunk(HtmlChunkInit.NAME);
//        return StringUtils.asInputStream(htmlChunk, StandardCharsets.UTF_8);
//    }

    private InputStream getChunk(String chunkName) {
        HtmlChunkProducer htmlChunkProducer = this.htmlChunkCache.getHtmlChunk(chunkName).getProducer();
        String htmlChunk = htmlChunkProducer.getHtml();
        return StringUtils.asInputStream(htmlChunk, StandardCharsets.UTF_8);
    }

//    private InputStream getPatientChunk() {
//        PatientHtmlChunkProducer patientHtmlChunkProducer = new PatientHtmlChunkProducer();
//        String htmlChunk = patientHtmlChunkProducer.getHtml();
//        return StringUtils.asInputStream(htmlChunk, StandardCharsets.UTF_8);
//    }

//    private InputStream getTherapistChunk() {
//        String htmlChunk = this.htmlChunkCache.getHtmlChunkAsString(HtmlChunkTherapist.NAME);
//        return StringUtils.asInputStream(htmlChunk, StandardCharsets.UTF_8);
//    }

//    private InputStream getLoginChunk() {
//        String htmlChunk = this.htmlChunkCache.getHtmlChunkAsString(LoginHtmlChunk.NAME);
//
//        LoginHtmlChunkModifier loginHtmlChunkModifier = new LoginHtmlChunkModifier();
//        loginHtmlChunkModifier.setRawChunk(htmlChunk);
//        InstanceConfiguration instanceConfiguration = ApplicationContext.getInstanceConfiguration();
//        String logo = instanceConfiguration.getApplicationConfigGenericSO().getLogo();
//        loginHtmlChunkModifier.addLogo(logo);
//
//        return StringUtils.asInputStream(htmlChunk, StandardCharsets.UTF_8);
//    }

//    private InputStream getLoginVoucherChunk() {
//        String htmlChunk = this.htmlChunkCache.getHtmlChunk(HtmlChunkLoginVoucher.NAME);
//        return StringUtils.asInputStream(htmlChunk, StandardCharsets.UTF_8);
//    }

//    private InputStream getPolicyChunk() {
//        String htmlChunk = this.htmlChunkCache.getHtmlChunkAsString(PolicyHtmlChunk.NAME);
//        return StringUtils.asInputStream(htmlChunk, StandardCharsets.UTF_8);
//    }

}
