package org.mentalizr.backend.rest.service;

import de.arthurpicht.webAccessControl.auth.Authorization;
import org.mentalizr.persistence.mongo.activityStatus.ActivityDAO;

public class ActivityMessage {

    public static void write(String serviceId, Authorization authorization) {
        if (authorization == null) return;
        ActivityDAO.createMessage(serviceId, authorization.getUserId(), authorization.getRoleName());
    }

    public static void write(String serviceId, Authorization authorization, String message) {
        if (authorization == null) return;
        ActivityDAO.createMessage(serviceId, authorization.getUserId(), authorization.getRoleName(), message);
    }

}
