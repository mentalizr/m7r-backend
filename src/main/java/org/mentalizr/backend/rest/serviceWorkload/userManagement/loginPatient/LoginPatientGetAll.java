package org.mentalizr.backend.rest.serviceWorkload.userManagement.loginPatient;

import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.RolePatientDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserLoginCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RolePatientVO;
import org.mentalizr.serviceObjects.userManagement.PatientRestoreCollectionSO;
import org.mentalizr.serviceObjects.userManagement.PatientRestoreSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LoginPatientGetAll {

    private static Logger logger = LoggerFactory.getLogger(LoginPatientGetAll.class);

    public static PatientRestoreCollectionSO getAll() throws DataSourceException, EntityNotFoundException {

        List<UserLoginCompositeVO> userLoginCompositeVOs = UserLoginCompositeDAO.findAllPatients();
        PatientRestoreCollectionSO patientRestoreCollectionSO = new PatientRestoreCollectionSO();

        for (UserLoginCompositeVO userLoginCompositeVO : userLoginCompositeVOs) {

            logger.debug("load UserLoginCompositeVO with UUID: " + userLoginCompositeVO.getUserId());

            RolePatientVO rolePatientVO = RolePatientDAO.load(userLoginCompositeVO.getUserId());
            PatientRestoreSO patientRestoreSO = new PatientRestoreSO();
            patientRestoreSO.setUuid(userLoginCompositeVO.getUserId());
            patientRestoreSO.setActive(userLoginCompositeVO.isActive());
            patientRestoreSO.setUsername(userLoginCompositeVO.getUsername());
            patientRestoreSO.setPasswordHash(userLoginCompositeVO.getPasswordHash());
            patientRestoreSO.setEmail(userLoginCompositeVO.getEmail());
            patientRestoreSO.setFirstname(userLoginCompositeVO.getFirstName());
            patientRestoreSO.setLastname(userLoginCompositeVO.getLastName());
            patientRestoreSO.setGender(userLoginCompositeVO.getGender());
            patientRestoreSO.setProgramId(rolePatientVO.getProgramId());
            patientRestoreSO.setTherapistId(rolePatientVO.getTherapistId());

            patientRestoreCollectionSO.getCollection().add(patientRestoreSO);
        }

        return patientRestoreCollectionSO;
    }

}
