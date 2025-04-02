package org.mentalizr.backend.rest.serviceWorkload.userManagement.accessKey;

import org.mentalizr.backend.adapter.AccessKeyRestoreSOAdapter;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserAccessKeyPatientCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserAccessKeyPatientCompositeVO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyCollectionSO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyRestoreSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PatientAccessKeyGet {

    private static final Logger logger = LoggerFactory.getLogger(PatientAccessKeyGet.class);

    public static AccessKeyCollectionSO getAll() throws DataSourceException, EntityNotFoundException {
        List<UserAccessKeyPatientCompositeVO> userAccessKeyPatientCompositeVOs = UserAccessKeyPatientCompositeDAO.findAll();
        AccessKeyCollectionSO accessKeyCollectionSO = new AccessKeyCollectionSO();

        for (UserAccessKeyPatientCompositeVO userAccessKeyPatientCompositeVO : userAccessKeyPatientCompositeVOs) {
            AccessKeyRestoreSO accessKeyRestoreSO =
                    AccessKeyRestoreSOAdapter.from(userAccessKeyPatientCompositeVO);
            accessKeyCollectionSO.getCollection().add(accessKeyRestoreSO);
        }

        return accessKeyCollectionSO;
    }

    public static AccessKeyRestoreSO get(String accessKey) throws DataSourceException, EntityNotFoundException {
        UserAccessKeyPatientCompositeVO userAccessKeyPatientCompositeVO = UserAccessKeyPatientCompositeDAO.findByAccessKey(accessKey);
        return AccessKeyRestoreSOAdapter.from(userAccessKeyPatientCompositeVO);
    }

    public static AccessKeyRestoreSO getById(String id) throws DataSourceException, EntityNotFoundException {
        UserAccessKeyPatientCompositeVO userAccessKeyPatientCompositeVO = UserAccessKeyPatientCompositeDAO.load(id);
        return AccessKeyRestoreSOAdapter.from(userAccessKeyPatientCompositeVO);
    }

}
