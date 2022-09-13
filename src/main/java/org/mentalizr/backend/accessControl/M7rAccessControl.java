package org.mentalizr.backend.accessControl;

import de.arthurpicht.utils.core.collection.Sets;
import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.PatientAnonymous;
import org.mentalizr.backend.accessControl.roles.PatientLogin;

import javax.servlet.http.HttpServletRequest;

public class M7rAccessControl {

    public static Authorization assertValidSessionAsPatient(HttpServletRequest httpServletRequest)
            throws UnauthorizedException {
        return AccessControl.assertValidSession(
                Sets.newHashSet(PatientAnonymous.ROLE_NAME, PatientLogin.ROLE_NAME), httpServletRequest);
    }

    public static Authorization assertValidSessionAsPatientWithId(HttpServletRequest httpServletRequest, String userId)
            throws UnauthorizedException {
        Authorization authorization = AccessControl.assertValidSession(
                Sets.newHashSet(PatientAnonymous.ROLE_NAME, PatientLogin.ROLE_NAME), httpServletRequest);
        if (!authorization.getUserId().equals(userId))
            throw new UnauthorizedException("No session for specified user.");
        return authorization;
    }

}
