package org.mentalizr.backend.auth;

import org.mentalizr.backend.rest.entities.Patient;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;

import java.io.Serializable;

public class PatientLoginHttpSessionAttribute extends PatientHttpSessionAttribute implements Serializable {

    private final UserLoginCompositeVO userLoginCompositeVO;

    public PatientLoginHttpSessionAttribute(UserLoginCompositeVO userLoginCompositeVO) throws DataSourceException {
        super(userLoginCompositeVO);
        this.userLoginCompositeVO = userLoginCompositeVO;
    }

    @Override
    public String getDisplayName() {
        return DisplayName.obtain(this.userLoginCompositeVO.getUserLoginVO());
    }

    @Override
    public int getGender() {
        return this.userLoginCompositeVO.getGender();
    }

    @Override
    public Patient getPatient() {
        return new Patient(this.userLoginCompositeVO);
    }
}
