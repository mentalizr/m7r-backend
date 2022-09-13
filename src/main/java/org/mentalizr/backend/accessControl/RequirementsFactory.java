package org.mentalizr.backend.accessControl;

import de.arthurpicht.webAccessControl.session.attributes.staging.requirements.Requirements;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserAccessKeyCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserLoginVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;

public class RequirementsFactory {

    public static Requirements createRequirements(UserLoginCompositeVO userLoginCompositeVO) {
        UserLoginVO userLoginVO = userLoginCompositeVO.getUserLoginVO();
        UserVO userVO = userLoginCompositeVO.getUserVO();
        Requirements requirements = new Requirements();

        if (userLoginVO.getSecondFA()) requirements.add2FARequirement();
        if (userVO.getPolicyConsent() == null) requirements.addPolicyConsentRequirement();
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

        if (userVO.getPolicyConsent() == null) requirements.addPolicyConsentRequirement();

        return requirements;
    }

}
