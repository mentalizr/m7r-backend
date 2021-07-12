package org.mentalizr.backend.auth;

import org.mentalizr.backend.rest.entities.UserRole;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RoleTherapistVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserLoginVO;

import java.io.Serializable;

public class TherapistHttpSessionAttribute extends UserHttpSessionAttribute implements Serializable {

    private final UserLoginVO userLoginVO;
    private final RoleTherapistVO roleTherapistVO;

    public TherapistHttpSessionAttribute(UserLoginCompositeVO userLoginCompositeVO) throws DataSourceException {
        super(userLoginCompositeVO.getUserVO());
        this.userLoginVO = userLoginCompositeVO.getUserLoginVO();
        this.roleTherapistVO = userLoginCompositeVO.getRoleTherapistVO();
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
