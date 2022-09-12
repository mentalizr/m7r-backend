package org.mentalizr.backend.security.auth;

import javax.servlet.http.HttpServletRequest;

public class Authentication extends AbstractAuthentication {

    public Authentication(HttpServletRequest httpServletRequest) throws UnauthorizedException {
        super(httpServletRequest);

        if (this.securityAttribute.getStagingAttribute().isIntermediate())
            throw new UnauthorizedException("[Authentication] failed: Session is expected to be [valid] but is " +
                    "[intermediate].");
    }

}
