package org.mentalizr.backend.accessControl.roles;

import org.mentalizr.backend.rest.entities.UserRole;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;

import java.io.Serializable;

public class Admin extends M7rUser implements Serializable {

    public static final String ROLE_NAME ="ADMIN";

    private static final long serialVersionUID = 8621340531156354740L;

    public Admin(UserLoginCompositeVO userLoginCompositeVO) {
        super(userLoginCompositeVO.getUserVO());
    }

    @Override
    public String getDisplayName() {
        return "N.N.";
    }

    @Override
    public int getGender() {
        return 0;
    }

    @Override
    public String getRoleName() {
        return ROLE_NAME;
    }

    @Override
    public String getUserId() {
        return this.getUserVO().getId();
    }

}
