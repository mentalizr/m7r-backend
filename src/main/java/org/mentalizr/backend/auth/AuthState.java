package org.mentalizr.backend.auth;

import de.arthurpicht.utils.core.assertion.AssertMethodPrecondition;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class AuthState {

    private final HttpServletRequest httpServletRequest;

    public AuthState(HttpServletRequest httpServletRequest) {

        AssertMethodPrecondition.parameterNotNull("httpServletRequest", httpServletRequest);

        this.httpServletRequest = httpServletRequest;
    }

    public boolean isLoggedIn() {
        HttpSession httpSession = httpServletRequest.getSession(false);
        return (httpSession != null);
    }

    public boolean isPatient() {

        Object userHttpSessionAttribute = obtainUserHttpSessionAttribute();
        return (userHttpSessionAttribute instanceof PatientHttpSessionAttribute);
    }

    public boolean isTherapist() {

        Object userHttpSessionAttribute = obtainUserHttpSessionAttribute();
        return (userHttpSessionAttribute instanceof TherapistHttpSessionAttribute);

    }

    public void logOut() {

        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession != null) httpSession.invalidate();
    }

    private Object obtainUserHttpSessionAttribute() {

        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession == null) throw new IllegalStateException("HttpSession is null.");

        Object userHttpSessionAttribute = httpSession.getAttribute(UserHttpSessionAttribute.USER);
        if (userHttpSessionAttribute == null) throw new IllegalStateException("Inconsistent State: Logged in but no UserHttpSessionAttribute found.");

        return userHttpSessionAttribute;
    }

}
