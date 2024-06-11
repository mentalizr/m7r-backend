package org.mentalizr.backend.rest.service;

import de.arthurpicht.webAccessControl.auth.Authorization;
import org.mentalizr.persistence.mongo.activityStatus.ActivityDAO;

public class ActivityMessage {

    public static void write(String serviceId, Authorization authorization) {
        String userId = authorization != null ? authorization.getUserId() : "";
        String roleName = authorization != null ? authorization.getRoleName() : "";
        ActivityDAO.createMessage(serviceId, userId, roleName);
    }

    public static void write(String serviceId, Authorization authorization, String message) {
        String userId = authorization != null ? authorization.getUserId() : "";
        String roleName = authorization != null ? authorization.getRoleName() : "";
        ActivityDAO.createMessage(serviceId, userId, roleName, message);
    }

}
