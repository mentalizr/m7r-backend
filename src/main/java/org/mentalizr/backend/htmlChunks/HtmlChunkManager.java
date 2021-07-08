package org.mentalizr.backend.htmlChunks;

import org.mentalizr.backend.auth.Authentication;
import org.mentalizr.backend.auth.AuthenticationService;
import org.mentalizr.backend.auth.Authorization;
import org.mentalizr.backend.auth.UnauthorizedException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

public class HtmlChunkManager {

    private final HttpServletRequest httpServletRequest;
    private final HtmlChunkCache htmlChunkCache;

    public HtmlChunkManager(HttpServletRequest httpRequest) {
        this.httpServletRequest = httpRequest;
        this.htmlChunkCache = new HtmlChunkCache(httpRequest.getServletContext());
    }

    public InputStream getHtmlChunk(HtmlChunk htmlChunk) throws UnauthorizedException, IOException {

        if (htmlChunk == HtmlChunk.LOGIN) {
            return getLoginChunk();
        }

        if (htmlChunk == HtmlChunk.LOGIN_VOUCHER) {
            return getLoginVoucherChunk();
        }

        if (htmlChunk == HtmlChunk.INIT) {
            return getInitChunk();
        }

        Authentication authentication = AuthenticationService.assertIsLoggedIn(this.httpServletRequest, "htmlChunk|HtmlChunkManager", true);
        Authorization authorization = new Authorization(authentication);

        if (htmlChunk == HtmlChunk.PATIENT) {
            if (!authorization.isPatient()) throw new UnauthorizedException("UserLogin not in required role PATIENT.");
            return getPatientChunk();
        }

        throw new RuntimeException("Implementation missing for HtmlChunk: " + htmlChunk.name());

    }

    private InputStream getInitChunk() throws IOException {
        return this.htmlChunkCache.getChunk(HtmlChunk.INIT);
    }

    private InputStream getPatientChunk() throws IOException {
        return this.htmlChunkCache.getChunk(HtmlChunk.PATIENT);
    }

    private InputStream getLoginChunk() throws IOException {
        return this.htmlChunkCache.getChunk(HtmlChunk.LOGIN);
    }

    private InputStream getLoginVoucherChunk() throws IOException {
        return this.htmlChunkCache.getChunk(HtmlChunk.LOGIN_VOUCHER);
    }

}
