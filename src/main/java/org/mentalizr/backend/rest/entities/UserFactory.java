package org.mentalizr.backend.rest.entities;

import org.mentalizr.backend.accessControl.roles.M7rUser;
import org.mentalizr.backend.accessControl.roles.PatientAbstract;
import org.mentalizr.serviceObjects.frontend.application.UserSO;

public class UserFactory {

    public static UserSO getInstance(M7rUser m7rUser) {
        String userId = m7rUser.getUserVO().getId();
        String displayName = m7rUser.getDisplayName();
        int gender = m7rUser.getGender();

        return new UserSO(userId, displayName, gender);
    }

    public static UserSO getInstanceForRelatedTherapist(PatientAbstract patientAbstract) {
        String userId = patientAbstract.getRoleTherapistVO().getUserId();
        String displayName = patientAbstract.getTherapistDisplayName();
        int gender = patientAbstract.getUserLoginVOTherapist().getGender();

        return new UserSO(userId, displayName, gender);
    }

}
