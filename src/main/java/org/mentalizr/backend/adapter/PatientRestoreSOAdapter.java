package org.mentalizr.backend.adapter;

import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserLoginVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;
import org.mentalizr.serviceObjects.userManagement.PatientRestoreSO;

public class PatientRestoreSOAdapter {

    public static PatientRestoreSO from(UserLoginCompositeVO userLoginCompositeVO) {
        PatientRestoreSO patientRestoreSO = new PatientRestoreSO();

        UserVO userVO = userLoginCompositeVO.getUserVO();
        patientRestoreSO.setUserId(userVO.getId());
        patientRestoreSO.setActive(userVO.getActive());
        patientRestoreSO.setCreation(userVO.getCreation());
        patientRestoreSO.setFirstActive(userVO.getFirstActive());
        patientRestoreSO.setLastActive(userVO.getLastActive());

        UserLoginVO userLoginVO = userLoginCompositeVO.getUserLoginVO();
        patientRestoreSO.setUsername(userLoginVO.getUsername());
        patientRestoreSO.setPasswordHash(userLoginVO.getPasswordHash());
        patientRestoreSO.setEmail(userLoginVO.getEmail());
        patientRestoreSO.setFirstname(userLoginVO.getFirstName());
        patientRestoreSO.setLastname(userLoginVO.getLastName());
        patientRestoreSO.setGender(userLoginVO.getGender());
        patientRestoreSO.setSecondFA(userLoginVO.getSecondFA());
        patientRestoreSO.setEmailConfirmation(userLoginVO.getEmailConfirmation());
        patientRestoreSO.setEmailConfToken(userLoginVO.getEmailConfToken());
        patientRestoreSO.setEmailConfCode(userLoginVO.getEmailConfCode());
        patientRestoreSO.setRenewPasswordRequired(userLoginVO.getRenewPasswordRequired());

        return patientRestoreSO;
    }

}
