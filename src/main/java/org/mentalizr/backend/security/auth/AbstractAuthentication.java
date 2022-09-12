package org.mentalizr.backend.security.auth;

import org.mentalizr.backend.security.session.SessionManager;
import org.mentalizr.backend.security.session.attributes.SecurityAttribute;
import org.mentalizr.backend.security.session.attributes.staging.StagingAttribute;
import org.mentalizr.backend.security.session.attributes.user.UserHttpSessionAttribute;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractAuthentication {
    protected String httpSessionId;
    protected SecurityAttribute securityAttribute;

    public AbstractAuthentication(HttpServletRequest httpServletRequest) throws UnauthorizedException {

        if (!SessionManager.hasSessionInAnyStaging(httpServletRequest))
            throw new UnauthorizedException("[Authentication] failed: No running session.");

        this.httpSessionId = SessionManager.getSessionId(httpServletRequest);
        this.securityAttribute = SessionManager.getSecurityAttribute(httpServletRequest);
    }

    public String getHttpSessionId() {
        return this.httpSessionId;
    }

    public SecurityAttribute getSecurityAttribute() {
        return this.securityAttribute;
    }

    public UserHttpSessionAttribute getUserHttpSessionAttribute() {
        return this.securityAttribute.getUserHttpSessionAttribute();
    }

    public StagingAttribute getStagingAttribute() {
        return this.securityAttribute.getStagingAttribute();
    }

}
