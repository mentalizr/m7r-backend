package org.mentalizr.backend.adapter;

import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyRestoreSO;

public class AccessKeyRestoreSOAdapter {

    public static AccessKeyRestoreSO from(UserLoginCompositeVO userLoginCompositeVO)
            throws DataSourceException {
        AccessKeyRestoreSO accessKeyRestoreSO = new AccessKeyRestoreSO();

        UserVO userVO = userLoginCompositeVO.getUserVO();
        accessKeyRestoreSO.setUserId(userVO.getId());
        accessKeyRestoreSO.setActive(userVO.getActive());
        accessKeyRestoreSO.setCreation(userVO.getCreation());
        accessKeyRestoreSO.setFirstActive(userVO.getFirstActive());
        accessKeyRestoreSO.setLastActive(userVO.getLastActive());

        accessKeyRestoreSO.setAccessKey(userVO.getUserAccessKeyVO().getFirst().getAccessKey());
        accessKeyRestoreSO.setTherapistId(userVO.getRoleTherapistVO().getFirst().getUserId());
        accessKeyRestoreSO.setProgramId(userVO.getRolePatientVO().getFirst()
                .getPatientProgramVO().getFirst().getProgramId());

        return accessKeyRestoreSO;
    }
}
