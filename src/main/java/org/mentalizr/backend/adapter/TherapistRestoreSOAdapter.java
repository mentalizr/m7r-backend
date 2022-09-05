package org.mentalizr.backend.adapter;

import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RoleTherapistVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserLoginVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;
import org.mentalizr.serviceObjects.userManagement.TherapistRestoreSO;

public class TherapistRestoreSOAdapter {

    public static TherapistRestoreSO from(UserLoginCompositeVO userLoginCompositeVO, RoleTherapistVO roleTherapistVO) {
        UserVO userVO = userLoginCompositeVO.getUserVO();
        UserLoginVO userLoginVO = userLoginCompositeVO.getUserLoginVO();
        return from(userVO, userLoginVO, roleTherapistVO);
    }

    public static TherapistRestoreSO from(UserVO userVO, UserLoginVO userLoginVO, RoleTherapistVO roleTherapistVO) {

        TherapistRestoreSO therapistRestoreSO = new TherapistRestoreSO();

        therapistRestoreSO.setUserId(userVO.getId());
        therapistRestoreSO.setActive(userVO.getActive());
        therapistRestoreSO.setFirstActive(userVO.getFirstActive());
        therapistRestoreSO.setLastActive(userVO.getLastActive());
        therapistRestoreSO.setPolicyConsent(userVO.getPolicyConsent());

        therapistRestoreSO.setUsername(userLoginVO.getUsername());
        therapistRestoreSO.setPasswordHash(userLoginVO.getPasswordHash());
        therapistRestoreSO.setEmail(userLoginVO.getEmail());
        therapistRestoreSO.setFirstname(userLoginVO.getFirstName());
        therapistRestoreSO.setLastname(userLoginVO.getLastName());
        therapistRestoreSO.setGender(userLoginVO.getGender());
        therapistRestoreSO.setSecondFA(userLoginVO.getSecondFA());
        therapistRestoreSO.setEmailConfirmation(userLoginVO.getEmailConfirmation());
        therapistRestoreSO.setEmailConfToken(userLoginVO.getEmailConfToken());
        therapistRestoreSO.setEmailConfCode(userLoginVO.getEmailConfCode());
        therapistRestoreSO.setRenewPasswordRequired(userLoginVO.getRenewPasswordRequired());

        therapistRestoreSO.setTitle(roleTherapistVO.getTitle());

        return therapistRestoreSO;
    }

}
