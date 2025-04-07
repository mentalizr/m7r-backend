package org.mentalizr.backend.adapter;

import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserAccessKeyCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginAccessKeyCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyRestoreSO;

import java.util.ArrayList;
import java.util.List;

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

    public static AccessKeyRestoreSO from(UserLoginAccessKeyCompositeVO userLoginAccessKeyCompositeVO) {
        AccessKeyRestoreSO accessKeyRestoreSO = new AccessKeyRestoreSO();

        accessKeyRestoreSO.setUserId(userLoginAccessKeyCompositeVO.getUserId());
        accessKeyRestoreSO.setAccessKey(userLoginAccessKeyCompositeVO.getAccessKey());
        accessKeyRestoreSO.setCreation(userLoginAccessKeyCompositeVO.getCreation());
        accessKeyRestoreSO.setActive(userLoginAccessKeyCompositeVO.isActive());
        accessKeyRestoreSO.setFirstActive(userLoginAccessKeyCompositeVO.getFirstActive());
        accessKeyRestoreSO.setLastActive(userLoginAccessKeyCompositeVO.getLastActive());
        accessKeyRestoreSO.setProjectId(userLoginAccessKeyCompositeVO.getProjectId());
        accessKeyRestoreSO.setProgramId(userLoginAccessKeyCompositeVO.getProgramId());
        accessKeyRestoreSO.setTherapistId(userLoginAccessKeyCompositeVO.getTherapistId());

        return accessKeyRestoreSO;
    }

    public static List<AccessKeyRestoreSO> from(List<UserLoginAccessKeyCompositeVO> userLoginAccessKeyCompositeVOs) {
        List<AccessKeyRestoreSO> accessKeyRestoreSOs = new ArrayList<>();
        for (UserLoginAccessKeyCompositeVO userLoginAccessKeyCompositeVO : userLoginAccessKeyCompositeVOs) {
            AccessKeyRestoreSO accessKeyRestoreSO = from(userLoginAccessKeyCompositeVO);
            accessKeyRestoreSOs.add(accessKeyRestoreSO);
        }
        return accessKeyRestoreSOs;
    }
}
