package org.mentalizr.backend.patientsOverviewSOCreator;

import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserAccessKeyDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserLoginDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RolePatientVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserAccessKeyVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserLoginVO;

public class PatientOverviewSOCreatorFactory {

    public static PatientOverviewSOCreator getInstance(RolePatientVO rolePatientVO) {
        try {
            UserLoginVO userLoginVO = UserLoginDAO.load(rolePatientVO.getUserId());
            return new PatientOverviewSOCreatorUserLogin(userLoginVO);
        } catch (DataSourceException e) {
            throw new RuntimeException(e);
        } catch (EntityNotFoundException e) {
            // din
        }
        try {
            UserAccessKeyVO userAccessKeyVO = UserAccessKeyDAO.load(rolePatientVO.getUserId());
            return new PatientOverviewSOCreatorUserAccessKey(userAccessKeyVO);
        } catch (DataSourceException e) {
            throw new RuntimeException(e);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Inconsistency: For a RolePatient should exist either  a UserLogin or a " +
                    "UserAccessKey. [id=" + rolePatientVO.getUserId() + "]", e);
        }
    }

}
