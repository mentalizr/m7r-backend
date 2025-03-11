package org.mentalizr.backend.adapter;

import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserAccessKeyCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyRestoreSO;

public class AccessKeyRestoreSOAdapter {

    public static AccessKeyRestoreSO from(UserAccessKeyCompositeVO userAccessKeyCompositeVO) {
        AccessKeyRestoreSO accessKeyRestoreSO = new AccessKeyRestoreSO();

        UserVO userVO = userAccessKeyCompositeVO.getUserVO();
        accessKeyRestoreSO.setUserId(userVO.getId());
        accessKeyRestoreSO.setActive(userVO.getActive());
        accessKeyRestoreSO.setCreation(userVO.getCreation());
        accessKeyRestoreSO.setFirstActive(userVO.getFirstActive());
        accessKeyRestoreSO.setLastActive(userVO.getLastActive());

        return accessKeyRestoreSO;
    }
}
