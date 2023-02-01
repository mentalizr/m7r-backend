package org.mentalizr.backend.accessControl;

import de.arthurpicht.webAccessControl.securityAttribute.requirements.Requirements;
import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserAccessKeyCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserLoginVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;
import org.mentalizr.persistence.rdbms.barnacle.vob.UserVOB;

public class RequirementsFactory {

    public static Requirements createRequirements(UserLoginCompositeVO userLoginCompositeVO) {
        UserLoginVO userLoginVO = userLoginCompositeVO.getUserLoginVO();
        UserVO userVO = userLoginCompositeVO.getUserVO();

        Requirements requirements = new Requirements();

        if (userLoginVO.getSecondFA()) requirements.add2FARequirement();
        if (isInRolePatientOrTherapist(userLoginCompositeVO) && isPolicyNotConsented(userVO.getId()))
            requirements.addPolicyConsentRequirement();
        if (userLoginVO.getEmailConfirmation() == null) requirements.addEmailConfirmationRequirement(
                userLoginVO.getEmailConfToken(),
                userLoginVO.getEmailConfCode()
        );
        if (userLoginVO.getRenewPasswordRequired()) requirements.addRenewPasswordRequirement();

        return requirements;
    }

    public static Requirements createRequirements(UserAccessKeyCompositeVO userAccessKeyCompositeVO) {
        UserVO userVO = userAccessKeyCompositeVO.getUserVO();
        Requirements requirements = new Requirements();

        if (isPolicyNotConsented(userVO.getId())) requirements.addPolicyConsentRequirement();

        return requirements;
    }

    private static boolean isInRolePatientOrTherapist(UserLoginCompositeVO userLoginCompositeVO) {
        try {
            return userLoginCompositeVO.isInRolePatient() || userLoginCompositeVO.isInRoleTherapist();
        } catch (DataSourceException e) {
            throw new RuntimeException(e);
        }
    }


    private static boolean isPolicyNotConsented(String userId) {
        String currentPolicyVersion = ApplicationContext.getCurrentPolicyVersion();
        try {
            return !(new UserVOB(userId).hasPolicy(currentPolicyVersion));
        } catch (DataSourceException e) {
            throw new RuntimeException(e);
        }
    }

}
