package org.mentalizr.backend.adapter;

import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginPatientCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserLoginVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;
import org.mentalizr.serviceObjects.userManagement.PatientRestoreSO;

import java.util.ArrayList;
import java.util.List;

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

    public static PatientRestoreSO from(UserLoginPatientCompositeVO userLoginPatientCompositeVO) {
        PatientRestoreSO patientRestoreSO = new PatientRestoreSO();

        patientRestoreSO.setUserId(userLoginPatientCompositeVO.getUserId());
        patientRestoreSO.setActive(userLoginPatientCompositeVO.getActive());
        patientRestoreSO.setCreation(userLoginPatientCompositeVO.getCreation());
        patientRestoreSO.setFirstActive(userLoginPatientCompositeVO.getFirstActive());
        patientRestoreSO.setLastActive(userLoginPatientCompositeVO.getLastActive());

        patientRestoreSO.setUsername(userLoginPatientCompositeVO.getUsername());
        patientRestoreSO.setPasswordHash(userLoginPatientCompositeVO.getPasswordHash());
        patientRestoreSO.setEmail(userLoginPatientCompositeVO.getEmail());
        patientRestoreSO.setFirstname(userLoginPatientCompositeVO.getFirstName());
        patientRestoreSO.setLastname(userLoginPatientCompositeVO.getLastName());
        patientRestoreSO.setGender(userLoginPatientCompositeVO.getGender());
        patientRestoreSO.setSecondFA(userLoginPatientCompositeVO.getSecondFA());
        patientRestoreSO.setEmailConfirmation(userLoginPatientCompositeVO.getEmailConfirmation());
        patientRestoreSO.setEmailConfToken(userLoginPatientCompositeVO.getEmailConfToken());
        patientRestoreSO.setEmailConfCode(userLoginPatientCompositeVO.getEmailConfCode());
        patientRestoreSO.setRenewPasswordRequired(userLoginPatientCompositeVO.getRenewPasswordRequired());

        patientRestoreSO.setProgramId(userLoginPatientCompositeVO.getProgramId());
        patientRestoreSO.setBlocking(userLoginPatientCompositeVO.getBlocking());

        return patientRestoreSO;
    }

    public static List<PatientRestoreSO> from(List<UserLoginPatientCompositeVO> userLoginPatientCompositeVOs) {
        List<PatientRestoreSO> patientRestoreSOs = new ArrayList<>();

        for (UserLoginPatientCompositeVO userLoginPatientCompositeVO : userLoginPatientCompositeVOs) {
            PatientRestoreSO patientRestoreSO = from(userLoginPatientCompositeVO);
            patientRestoreSOs.add(patientRestoreSO);
        }

        return patientRestoreSOs;
    }
}
