package org.mentalizr.backend.auth;


import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;
import org.mentalizr.backend.rest.entities.UserRole;

import java.io.Serializable;

public class AdminHttpSessionAttribute extends UserHttpSessionAttribute implements Serializable {

    public AdminHttpSessionAttribute(UserVO userVO) {
        super(userVO);
    }

    @Override
    public UserRole getUserRole() {
        return UserRole.ADMIN;
    }
}
