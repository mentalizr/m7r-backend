package org.mentalizr.backend.accessControl.roles;

import org.mentalizr.backend.accessControl.helper.DisplayName;
import org.mentalizr.backend.rest.entities.Patient;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;

import java.io.Serializable;

public class PatientLogin extends PatientAbstract implements Serializable {

    public static final String ROLE_NAME = "LOGIN_PATIENT";


    private final UserLoginCompositeVO userLoginCompositeVO;

    public PatientLogin(UserLoginCompositeVO userLoginCompositeVO) throws DataSourceException {
        super(userLoginCompositeVO);
        this.userLoginCompositeVO = userLoginCompositeVO;
    }

    @Override
    public String getDisplayName() {
        return DisplayName.obtain(this.userLoginCompositeVO.getUserLoginVO());
    }

    @Override
    public int getGender() {
        return this.userLoginCompositeVO.getUserLoginVO().getGender();
    }

    @Override
    public Patient getPatient() {
        return new Patient(this.userLoginCompositeVO);
    }

}
