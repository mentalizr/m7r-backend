package org.mentalizr.backend.accessControl.roles;


import de.arthurpicht.webAccessControl.securityAttribute.User;
import org.mentalizr.backend.rest.entities.UserRole;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;

import java.io.Serializable;

public abstract class M7rUser extends User implements Serializable {

    private static final long serialVersionUID = -1808172120620092363L;

    protected final UserVO userVO;

    public M7rUser(UserVO userVO) {
        this.userVO = userVO;
    }

    public UserVO getUserVO() {
        return this.userVO;
    }

//    public abstract UserRole getUserRole();

    public abstract String getDisplayName();

    public abstract int getGender();

    public static PatientAbstract asPatientHttpSessionAttribute(M7rUser m7rUser) {
        if (m7rUser == null)
            throw new IllegalArgumentException("Specified userHttpSessionAttribute is null.");
        if (!(m7rUser instanceof PatientAbstract))
            throw new IllegalArgumentException("Specified userHttpSessionAttribute is not of type "
                    + PatientAbstract.class.getSimpleName() + ".");
        return (PatientAbstract) m7rUser;
    }

    public static Therapist asTherapistHttpSessionAttribute(M7rUser m7rUser) {
        if (m7rUser == null)
            throw new IllegalArgumentException("Specified userHttpSessionAttribute is null.");
        if (!(m7rUser instanceof Therapist))
            throw new IllegalArgumentException("Specified userHttpSessionAttribute is not of type "
                    + Therapist.class.getSimpleName() + ".");
        return (Therapist) m7rUser;
    }

    @Override
    public String getUserId() {
        return this.userVO.getId();
    }

}
