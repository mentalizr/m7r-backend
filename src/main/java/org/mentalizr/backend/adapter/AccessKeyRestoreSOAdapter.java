package org.mentalizr.backend.adapter;

import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserAccessKeyPatientCompositeVO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyRestoreSO;

public class AccessKeyRestoreSOAdapter {

    public static AccessKeyRestoreSO from(UserAccessKeyPatientCompositeVO userAccessKeyPatientCompositeVO) {
        AccessKeyRestoreSO accessKeyRestoreSO = new AccessKeyRestoreSO();
        accessKeyRestoreSO.setUserId(userAccessKeyPatientCompositeVO.getUserId());
        accessKeyRestoreSO.setActive(userAccessKeyPatientCompositeVO.isActive());
        accessKeyRestoreSO.setCreation(userAccessKeyPatientCompositeVO.getCreation());
        accessKeyRestoreSO.setFirstActive(userAccessKeyPatientCompositeVO.getFirstActive());
        accessKeyRestoreSO.setLastActive(userAccessKeyPatientCompositeVO.getLastActive());
        accessKeyRestoreSO.setAccessKey(userAccessKeyPatientCompositeVO.getAccessKey());
        accessKeyRestoreSO.setProgramId(userAccessKeyPatientCompositeVO.getProgramId());
        accessKeyRestoreSO.setTherapistId(userAccessKeyPatientCompositeVO.getTherapistId());
        accessKeyRestoreSO.setProjectId(userAccessKeyPatientCompositeVO.getProjectId());
        return accessKeyRestoreSO;
    }

}
