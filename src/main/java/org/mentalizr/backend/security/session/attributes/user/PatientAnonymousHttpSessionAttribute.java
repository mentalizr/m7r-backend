package org.mentalizr.backend.security.session.attributes.user;

import org.mentalizr.backend.rest.entities.Patient;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserLoginCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserAccessKeyCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RolePatientVO;

import java.io.Serializable;

public class PatientAnonymousHttpSessionAttribute extends PatientHttpSessionAttribute implements Serializable {

    public PatientAnonymousHttpSessionAttribute(UserAccessKeyCompositeVO userAccessKeyCompositeVO) throws DataSourceException {
        super(userAccessKeyCompositeVO, getUserLoginCompositeVOTherapist(userAccessKeyCompositeVO));
    }

    private static UserLoginCompositeVO getUserLoginCompositeVOTherapist(UserAccessKeyCompositeVO userAccessKeyCompositeVO) throws DataSourceException {
        RolePatientVO rolePatientVO = userAccessKeyCompositeVO.getRolePatientVO();
        try {
            return UserLoginCompositeDAO.load(rolePatientVO.getTherapistId());
        } catch (EntityNotFoundException e) {
            throw new IllegalStateException("RDBMS constraint violation: Therapist not found for access key user.");
        }
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
