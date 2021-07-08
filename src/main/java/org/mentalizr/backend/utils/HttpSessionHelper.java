package org.mentalizr.backend.utils;

import javax.servlet.http.HttpServletRequest;

public class HttpSessionHelper {

    public static void rememberMe(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession(false).setMaxInactiveInterval(0);
    }
}
