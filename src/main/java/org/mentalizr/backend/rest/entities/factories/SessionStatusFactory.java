package org.mentalizr.backend.rest.entities.factories;

import org.mentalizr.backend.rest.entities.UserRole;
import org.mentalizr.serviceObjects.SessionStatusSO;

public class SessionStatusFactory {

//    public static SessionStatus getInstance(boolean hasSession, UserRole userRole, String sessionId) {
//        this.hasSession = hasSession;
//        this.userRole = userRole.name();
//        this.sessionId = sessionId;
//    }

    public static SessionStatusSO getInstanceForValidSession(UserRole userRole, String sessionId) {
        return new SessionStatusSO(true, userRole.name(), sessionId);
    }

    public static SessionStatusSO getInstanceForInvalidSession() {
        return new SessionStatusSO(false, UserRole.UNDEFINED.name(), "");
    }

}
