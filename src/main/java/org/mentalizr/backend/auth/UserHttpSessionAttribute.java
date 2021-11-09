package org.mentalizr.backend.auth;


import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;
import org.mentalizr.backend.rest.entities.UserRole;

import java.io.Serializable;

public abstract class UserHttpSessionAttribute implements Serializable {

    public static final String USER = "user";

    protected final UserVO userVO;

    public UserHttpSessionAttribute(UserVO userVO) {
        this.userVO = userVO;
    }

    public UserVO getUserVO() {
        return this.userVO;
    }

    public abstract UserRole getUserRole();

    public abstract String getDisplayName();

    public abstract int getGender();

    public static PatientHttpSessionAttribute asPatientHttpSessionAttribute(UserHttpSessionAttribute userHttpSessionAttribute) {
        if (userHttpSessionAttribute == null)
            throw new IllegalArgumentException("Specified userHttpSessionAttribute is null.");
        if (!(userHttpSessionAttribute instanceof PatientHttpSessionAttribute))
            throw new IllegalArgumentException("Specified userHttpSessionAttribute is not of type "
                    + PatientHttpSessionAttribute.class.getSimpleName() + ".");
        return (PatientHttpSessionAttribute) userHttpSessionAttribute;
    }

}
