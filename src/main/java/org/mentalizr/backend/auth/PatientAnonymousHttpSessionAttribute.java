package org.mentalizr.backend.auth;

import org.mentalizr.persistence.rdbms.barnacle.vo.RolePatientVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RoleTherapistVO;
import org.mentalizr.backend.rest.entities.Patient;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserAccessKeyCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;

import java.io.Serializable;

public class PatientAnonymousHttpSessionAttribute extends PatientHttpSessionAttribute implements Serializable {

    public PatientAnonymousHttpSessionAttribute(UserAccessKeyCompositeVO userAccessKeyCompositeVO, RolePatientVO rolePatientVO, UserLoginCompositeVO userLoginCompositeVOTherapist, RoleTherapistVO roleTherapistVO) {
        super(userAccessKeyCompositeVO.getUserVO(), rolePatientVO, userLoginCompositeVOTherapist, roleTherapistVO);
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
    public Patient getPatient() {
        return new Patient(this.userVO);
    }
}
