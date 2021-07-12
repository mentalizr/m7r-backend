package org.mentalizr.backend.auth;

import org.mentalizr.backend.rest.entities.Patient;
import org.mentalizr.backend.rest.entities.Therapeut;
import org.mentalizr.backend.rest.entities.UserRole;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.RoleTherapistDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserLoginDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserLoginCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserAccessKeyCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RolePatientVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RoleTherapistVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserLoginVO;
import org.mentalizr.serviceObjects.frontend.application.UserSO;

import java.io.Serializable;

public abstract class PatientHttpSessionAttribute extends UserHttpSessionAttribute implements Serializable {

    private final RolePatientVO rolePatientVO;
    private final UserLoginVO userLoginVOTherapist;
    private final RoleTherapistVO roleTherapistVO;

    public PatientHttpSessionAttribute(UserLoginCompositeVO userLoginCompositeVO) throws DataSourceException {
        super(userLoginCompositeVO.getUserVO());
        this.rolePatientVO = userLoginCompositeVO.getRolePatientVO();
        try {
            this.userLoginVOTherapist = UserLoginDAO.load(rolePatientVO.getTherapistId());
            this.roleTherapistVO = RoleTherapistDAO.load(rolePatientVO.getTherapistId());
        } catch (EntityNotFoundException e) {
            throw new IllegalStateException("RDBMS constraint violation: " + e.getMessage(), e);
        }
    }

    public PatientHttpSessionAttribute(UserAccessKeyCompositeVO userAccessKeyCompositeVO, UserLoginCompositeVO userLoginCompositeVOTherapist) throws DataSourceException {
        super(userAccessKeyCompositeVO.getUserVO());
        this.rolePatientVO = userAccessKeyCompositeVO.getRolePatientVO();
        try {
            this.userLoginVOTherapist = UserLoginDAO.load(rolePatientVO.getTherapistId());
            this.roleTherapistVO = RoleTherapistDAO.load(rolePatientVO.getTherapistId());
        } catch (EntityNotFoundException e) {
            throw new IllegalStateException("RDBMS constraint violation: " + e.getMessage(), e);
        }
    }

    public RolePatientVO getRolePatientVO() {
        return this.rolePatientVO;
    }

    public String getTherapistDisplayName() {
        return DisplayName.obtain(this.userLoginVOTherapist, this.roleTherapistVO);
    }

    public UserLoginVO getUserLoginVOTherapist() {
        return userLoginVOTherapist;
    }

    public RoleTherapistVO getRoleTherapistVO() {
        return this.roleTherapistVO;
    }

    @Override
    public UserRole getUserRole() {
        return UserRole.PATIENT;
    }

    public abstract Patient getPatient();

}
