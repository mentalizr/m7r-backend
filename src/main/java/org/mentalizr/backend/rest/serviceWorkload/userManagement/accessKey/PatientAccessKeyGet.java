package org.mentalizr.backend.rest.serviceWorkload.userManagement.accessKey;

import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserAccessKeyPatientCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginAccessKeyCompositeVO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyCollectionSO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyRestoreSO;

import java.util.List;

public class PatientAccessKeyGet {

    public static AccessKeyCollectionSO getAll() throws DataSourceException, EntityNotFoundException {
        List<UserLoginAccessKeyCompositeVO> userLoginAccessKeyCompositeVOS = UserAccessKeyPatientCompositeDAO.findAll();
        AccessKeyCollectionSO accessKeyCollectionSO = new AccessKeyCollectionSO();

        for (UserLoginAccessKeyCompositeVO userLoginAccessKeyCompositeVO : userLoginAccessKeyCompositeVOS) {
            AccessKeyRestoreSO accessKeyRestoreSO = create(userLoginAccessKeyCompositeVO);
            accessKeyCollectionSO.getCollection().add(accessKeyRestoreSO);
        }

        return accessKeyCollectionSO;
    }

    public static AccessKeyRestoreSO get(String accessKey) throws DataSourceException, EntityNotFoundException {
        UserLoginAccessKeyCompositeVO userLoginAccessKeyCompositeVO = UserAccessKeyPatientCompositeDAO.findByAccessKey(accessKey);
        return create(userLoginAccessKeyCompositeVO);
    }

    private static AccessKeyRestoreSO create(UserLoginAccessKeyCompositeVO userLoginAccessKeyCompositeVO) {
        AccessKeyRestoreSO accessKeyRestoreSO = new AccessKeyRestoreSO();
        accessKeyRestoreSO.setUserId(userLoginAccessKeyCompositeVO.getUserId());
        accessKeyRestoreSO.setActive(userLoginAccessKeyCompositeVO.isActive());
        accessKeyRestoreSO.setCreation(userLoginAccessKeyCompositeVO.getCreation());
        accessKeyRestoreSO.setFirstActive(userLoginAccessKeyCompositeVO.getFirstActive());
        accessKeyRestoreSO.setLastActive(userLoginAccessKeyCompositeVO.getLastActive());
        accessKeyRestoreSO.setAccessKey(userLoginAccessKeyCompositeVO.getAccessKey());
        accessKeyRestoreSO.setProgramId(userLoginAccessKeyCompositeVO.getProgramId());
        accessKeyRestoreSO.setTherapistId(userLoginAccessKeyCompositeVO.getTherapistId());
        accessKeyRestoreSO.setProjectId(userLoginAccessKeyCompositeVO.getProjectId());
        return accessKeyRestoreSO;
    }

}
