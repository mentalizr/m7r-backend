package org.mentalizr.backend.accessControl.roles;

import org.mentalizr.backend.accessControl.helper.DisplayName;
import org.mentalizr.backend.rest.entities.Patient;
import org.mentalizr.backend.rest.entities.UserRole;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.PatientProgramDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.RoleTherapistDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserLoginDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserAccessKeyCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.PatientProgramVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RolePatientVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RoleTherapistVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserLoginVO;

import java.io.Serializable;

public abstract class PatientAbstract extends M7rUser implements Serializable {

    private static final long serialVersionUID = -1758127595328698137L;

    private final RolePatientVO rolePatientVO;
    private final PatientProgramVO patientProgramVO;
    private final UserLoginVO userLoginVOTherapist;
    private final RoleTherapistVO roleTherapistVO;

    public PatientAbstract(UserLoginCompositeVO userLoginCompositeVO) throws DataSourceException {
        super(userLoginCompositeVO.getUserVO());
        this.rolePatientVO = userLoginCompositeVO.getRolePatientVO();
        try {
            this.patientProgramVO = PatientProgramDAO.findByUk_user_id(userLoginCompositeVO.getUserId());
            this.userLoginVOTherapist = UserLoginDAO.load(rolePatientVO.getTherapistId());
            this.roleTherapistVO = RoleTherapistDAO.load(rolePatientVO.getTherapistId());
        } catch (EntityNotFoundException e) {
            throw new IllegalStateException("RDBMS constraint violation: " + e.getMessage(), e);
        }
    }

    public PatientAbstract(UserAccessKeyCompositeVO userAccessKeyCompositeVO, UserLoginCompositeVO userLoginCompositeVOTherapist) throws DataSourceException {
        super(userAccessKeyCompositeVO.getUserVO());
        this.rolePatientVO = userAccessKeyCompositeVO.getRolePatientVO();
        try {
            this.patientProgramVO = PatientProgramDAO.findByUk_user_id(userAccessKeyCompositeVO.getUserId());
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

    public PatientProgramVO getPatientProgramVO() {
        return this.patientProgramVO;
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
