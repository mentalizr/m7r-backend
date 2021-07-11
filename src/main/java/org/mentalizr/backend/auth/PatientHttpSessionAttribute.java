package org.mentalizr.backend.auth;

import org.mentalizr.backend.rest.entities.Patient;
import org.mentalizr.backend.rest.entities.Therapeut;
import org.mentalizr.backend.rest.entities.UserRole;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RolePatientVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RoleTherapistVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;

import java.io.Serializable;

public abstract class PatientHttpSessionAttribute extends UserHttpSessionAttribute implements Serializable {

    private final UserLoginCompositeVO userLoginCompositVOTherapist;
    private final RolePatientVO rolePatientVO;
    private final RoleTherapistVO roleTherapistVO;

    public PatientHttpSessionAttribute(UserVO userVO, RolePatientVO rolePatientVO, UserLoginCompositeVO userLoginCompositVOTherapist, RoleTherapistVO roleTherapistVO) {
        super(userVO);
        this.rolePatientVO = rolePatientVO;
        this.userLoginCompositVOTherapist = userLoginCompositVOTherapist;
        this.roleTherapistVO = roleTherapistVO;
    }

    public RolePatientVO getRolePatientVO() {
        return this.rolePatientVO;
    }

    public Therapeut getTherapeut() {
        return new Therapeut(this.userLoginCompositVOTherapist, this.roleTherapistVO);
    }

    @Override
    public UserRole getUserRole() {
        return UserRole.PATIENT;
    }

    public abstract Patient getPatient();

}
