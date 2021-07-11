package org.mentalizr.backend.auth;

import org.mentalizr.backend.rest.entities.Patient;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RolePatientVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RoleTherapistVO;

import java.io.Serializable;

public class PatientLoginHttpSessionAttribute extends PatientHttpSessionAttribute implements Serializable {

    private final UserLoginCompositeVO userLoginCompositeVO;

    public PatientLoginHttpSessionAttribute(UserLoginCompositeVO userLoginCompositeVO, RolePatientVO rolePatientVO, UserLoginCompositeVO therapistUserLoginCompositeVO, RoleTherapistVO roleTherapistVO) {
        super(userLoginCompositeVO.getUserVO(), rolePatientVO, therapistUserLoginCompositeVO, roleTherapistVO);
        this.userLoginCompositeVO = userLoginCompositeVO;
    }

    public String getFirstName() {
        return this.userLoginCompositeVO.getFirstName();
    }

    public String getLastName() {
        return this.userLoginCompositeVO.getLastName();
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
