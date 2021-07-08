package org.mentalizr.backend.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Authentication {

    private final HttpSession httpSession;
    private final UserHttpSessionAttribute userHttpSessionAttribute;

    public Authentication(HttpServletRequest httpServletRequest) throws UnauthorizedException {

        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null) {
            throw new UnauthorizedException("[Authentication] failed: No valid session.");
        }

        this.httpSession = httpSession;
        this.userHttpSessionAttribute = (UserHttpSessionAttribute) httpSession.getAttribute(UserHttpSessionAttribute.USER);
        if (userHttpSessionAttribute == null) {
            throw new UnauthorizedException("[Authentication] failed: No attribute '" + UserHttpSessionAttribute.USER + "'.");
        }
    }

    public HttpSession getHttpSession() {
        return this.httpSession;
    }

    public UserHttpSessionAttribute getUserHttpSessionAttribute() {
        return this.userHttpSessionAttribute;
    }

}
