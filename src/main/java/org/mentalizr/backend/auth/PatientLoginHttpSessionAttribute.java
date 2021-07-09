package org.mentalizr.backend.auth;

import org.mentalizr.persistence.rdbms.barnacle.vo.RolePatientVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RoleTherapistVO;
import org.mentalizr.backend.rest.entities.Patient;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;

import java.io.Serializable;

public class PatientLoginHttpSessionAttribute extends PatientHttpSessionAttribute implements Serializable {

    private final UserLoginCompositeVO userLoginCompositeVO;

    public PatientLoginHttpSessionAttribute(UserLoginCompositeVO userLoginCompositeVO, RolePatientVO rolePatientVO, UserLoginCompositeVO therapistUserLoginCompositeVO, RoleTherapistVO roleTherapistVO) {
        super(userLoginCompositeVO.getUserVO(), rolePatientVO, therapistUserLoginCompositeVO, roleTherapistVO);
        this.userLoginCompositeVO = userLoginCompositeVO;
    }

    @Override
    public String getFirstName() {
        return this.userLoginCompositeVO.getFirstName();
    }

    @Override
    public String getLastName() {
        return this.userLoginCompositeVO.getLastName();
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
