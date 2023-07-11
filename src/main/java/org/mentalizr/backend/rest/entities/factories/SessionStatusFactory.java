package org.mentalizr.backend.rest.entities.factories;

import org.mentalizr.backend.rest.entities.UserRole;
import org.mentalizr.serviceObjects.SessionStatusSO;

public class SessionStatusFactory {

    public static SessionStatusSO getInstanceForValidSession(String roleName, String sessionId) {
        return new SessionStatusSO(SessionStatusSO.STATUS_VALID, roleName, sessionId, "");
    }

    public static SessionStatusSO getInstanceForInvalidSession() {
        return new SessionStatusSO(SessionStatusSO.STATUS_INVALID, "UNDEFINED", "", "");
    }

    public static SessionStatusSO getInstanceForIntermediateSession(String roleName, String sessionId, String require) {
        return new SessionStatusSO(SessionStatusSO.STATUS_INTERMEDIATE, roleName, sessionId, require);
    }

}
