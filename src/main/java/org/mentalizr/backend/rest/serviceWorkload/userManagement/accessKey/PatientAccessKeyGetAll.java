package org.mentalizr.backend.rest.serviceWorkload.userManagement.accessKey;

import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.RolePatientDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserAccessKeyCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserAccessKeyPatientCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserAccessKeyCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserAccessKeyPatientCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RolePatientVO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyCollectionSO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyRestoreSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PatientAccessKeyGetAll {

    private static final Logger logger = LoggerFactory.getLogger(PatientAccessKeyGetAll.class);

    public static AccessKeyCollectionSO getAll() throws DataSourceException, EntityNotFoundException {

        List<UserAccessKeyPatientCompositeVO> userAccessKeyPatientCompositeVOs = UserAccessKeyPatientCompositeDAO.findAll();
        AccessKeyCollectionSO accessKeyCollectionSO = new AccessKeyCollectionSO();

        for (UserAccessKeyPatientCompositeVO userAccessKeyPatientCompositeVO : userAccessKeyPatientCompositeVOs) {
            AccessKeyRestoreSO accessKeyRestoreSO = new AccessKeyRestoreSO();
            accessKeyRestoreSO.setUserId(userAccessKeyPatientCompositeVO.getUserId());
            accessKeyRestoreSO.setActive(userAccessKeyPatientCompositeVO.isActive());
            accessKeyRestoreSO.setAccessKey(userAccessKeyPatientCompositeVO.getAccessKey());
            accessKeyRestoreSO.setProgramId(userAccessKeyPatientCompositeVO.getProgramId());
            accessKeyRestoreSO.setTherapistId(userAccessKeyPatientCompositeVO.getTherapistId());

            accessKeyCollectionSO.getCollection().add(accessKeyRestoreSO);
        }

        return accessKeyCollectionSO;
    }

}
