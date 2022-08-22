package org.mentalizr.backend.rest.entities;

import org.mentalizr.backend.security.session.attributes.user.PatientHttpSessionAttribute;
import org.mentalizr.backend.security.session.attributes.user.UserHttpSessionAttribute;
import org.mentalizr.serviceObjects.frontend.application.UserSO;

public class UserFactory {

    public static UserSO getInstance(UserHttpSessionAttribute userHttpSessionAttribute) {
        String userId = userHttpSessionAttribute.getUserVO().getId();
        String displayName = userHttpSessionAttribute.getDisplayName();
        int gender = userHttpSessionAttribute.getGender();

        return new UserSO(userId, displayName, gender);
    }

    public static UserSO getInstanceForRelatedTherapist(PatientHttpSessionAttribute patientHttpSessionAttribute) {
        String userId = patientHttpSessionAttribute.getRoleTherapistVO().getUserId();
        String displayName = patientHttpSessionAttribute.getTherapistDisplayName();
        int gender = patientHttpSessionAttribute.getUserLoginVOTherapist().getGender();

        return new UserSO(userId, displayName, gender);
    }

}
