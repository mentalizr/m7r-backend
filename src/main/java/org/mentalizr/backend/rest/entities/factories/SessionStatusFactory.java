package org.mentalizr.backend.rest.entities.factories;

import org.mentalizr.backend.rest.entities.UserRole;
import org.mentalizr.serviceObjects.SessionStatusSO;

public class SessionStatusFactory {

    public static SessionStatusSO getInstanceForValidSession(UserRole userRole, String sessionId) {
        return new SessionStatusSO(SessionStatusSO.STATUS_VALID, userRole.name(), sessionId, "");
    }

    public static SessionStatusSO getInstanceForInvalidSession() {
        return new SessionStatusSO(SessionStatusSO.STATUS_INVALID, UserRole.UNDEFINED.name(), "", "");
    }

    public static SessionStatusSO getInstanceForIntermediateSession(UserRole userRole, String sessionId, String require) {
        return new SessionStatusSO(SessionStatusSO.STATUS_INTERMEDIATE, userRole.name(), sessionId, require);
    }

}
