package org.mentalizr.backend.security.session.attributes.user;


import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.mentalizr.backend.rest.entities.UserRole;

import java.io.Serializable;

public class AdminHttpSessionAttribute extends UserHttpSessionAttribute implements Serializable {

    public AdminHttpSessionAttribute(UserLoginCompositeVO userLoginCompositeVO) {
        super(userLoginCompositeVO.getUserVO());
    }

    @Override
    public UserRole getUserRole() {
        return UserRole.ADMIN;
    }

    @Override
    public String getDisplayName() {
        return "N.N.";
    }

    @Override
    public int getGender() {
        return 0;
    }
}
