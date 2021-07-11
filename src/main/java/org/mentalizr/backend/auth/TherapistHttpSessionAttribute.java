package org.mentalizr.backend.auth;

import de.arthurpicht.utils.core.strings.Strings;
import org.mentalizr.backend.rest.entities.UserRole;
import org.mentalizr.persistence.rdbms.barnacle.vo.RoleTherapistVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserLoginVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;

import java.io.Serializable;

public class TherapistHttpSessionAttribute extends UserHttpSessionAttribute implements Serializable {

    private final UserLoginVO userLoginVO;
    private final RoleTherapistVO roleTherapistVO;

    public TherapistHttpSessionAttribute(
            UserVO userVO,
            UserLoginVO userLoginVO,
            RoleTherapistVO roleTherapistVO) {

        super(userVO);
        this.userLoginVO = userLoginVO;
        this.roleTherapistVO = roleTherapistVO;
    }

    @Override
    public UserRole getUserRole() {
        return UserRole.THERAPIST;
    }

    @Override
    public String getDisplayName() {
        return DisplayName.obtain(this.userLoginVO, this.roleTherapistVO);
    }

    @Override
    public int getGender() {
        return this.userLoginVO.getGender();
    }

    public String getTitle() {
        return this.roleTherapistVO.getTitle();
    }

}
