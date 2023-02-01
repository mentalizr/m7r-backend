package org.mentalizr.backend.accessControl;

import org.mentalizr.backend.accessControl.roles.M7rUser;
import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.PolicyConsentDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.PolicyConsentPK;
import org.mentalizr.persistence.rdbms.barnacle.vo.PolicyConsentVO;

public class RequirementsFulfill {

    public static void policyConsent(M7rUser m7rUser) throws DataSourceException {
        String userId = m7rUser.getUserId();
        String policyVersion = ApplicationContext.getCurrentPolicyVersion();

        PolicyConsentPK policyPK = new PolicyConsentPK(userId, policyVersion);
        try {
            PolicyConsentVO policyVO = PolicyConsentDAO.load(policyPK);
            policyVO.setConsent(System.currentTimeMillis());
            PolicyConsentDAO.update(policyVO);
        } catch (EntityNotFoundException e) {
            PolicyConsentVO policyVO = new PolicyConsentVO(policyPK);
            policyVO.setConsent(System.currentTimeMillis());
            PolicyConsentDAO.create(policyVO);
        }

    }

}
