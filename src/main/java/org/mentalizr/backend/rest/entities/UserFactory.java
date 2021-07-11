package org.mentalizr.backend.rest.entities;

import org.mentalizr.backend.auth.UserHttpSessionAttribute;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;
import org.mentalizr.serviceObjects.frontend.application.UserSO;

public class UserFactory {

    public static UserSO getInstance(UserHttpSessionAttribute userHttpSessionAttribute) {
        String userId = userHttpSessionAttribute.getUserVO().getUserId();
        String displayName = userHttpSessionAttribute.getDisplayName();
        int gender = userHttpSessionAttribute.getGender();;

        return new UserSO(userId, displayName, gender);
    }

    public static UserSO getInstanceForAnonymous(UserVO userVO) {
        String userId = userVO.getUserId();
        String displayName = "N.N.";
        int gender = 0;
        return new UserSO(userId, displayName, 0);
    }

}
