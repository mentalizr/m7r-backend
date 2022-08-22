package org.mentalizr.backend.security.auth;

import org.mentalizr.backend.Const;
import org.mentalizr.backend.security.session.attributes.staging.StagingAttribute;
import org.mentalizr.backend.security.session.attributes.user.UserHttpSessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Authentication {

    private final String httpSessionId;
    private final UserHttpSessionAttribute userHttpSessionAttribute;
    private final StagingAttribute stagingAttribute;

    public Authentication(HttpServletRequest httpServletRequest) throws UnauthorizedException {

        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null)
            throw new UnauthorizedException("[Authentication] failed: No running session.");

        this.httpSessionId = httpSession.getId();

        this.userHttpSessionAttribute = (UserHttpSessionAttribute) httpSession.getAttribute(UserHttpSessionAttribute.USER);
        if (userHttpSessionAttribute == null) {
            String errorMessage = getErrorMessage(UserHttpSessionAttribute.USER);
            Const.authLogger.error(errorMessage);
            throw new UnauthorizedException(errorMessage);
        }

        this.stagingAttribute = (StagingAttribute) httpSession.getAttribute(StagingAttribute.ATTRIBUTE_NAME);
        if (stagingAttribute == null) {
            String errorMessage = getErrorMessage(StagingAttribute.ATTRIBUTE_NAME);
            throw new UnauthorizedException(errorMessage);
        }

        if (stagingAttribute.isIntermediate())
            throw new UnauthorizedException("[Authentication] failed: Session is in state [intermediate].");
    }

    public String getHttpSessionId() {
        return this.httpSessionId;
    }

    public UserHttpSessionAttribute getUserHttpSessionAttribute() {
        return this.userHttpSessionAttribute;
    }

    public StagingAttribute getStagingAttribute() {
        return this.stagingAttribute;
    }

    private String getErrorMessage(String attributeName) {
        return "[Authentication] failed due to internal inconsistency: No attribute '" + attributeName + "' found in running session.";
    }

}
