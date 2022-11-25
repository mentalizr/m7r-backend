package org.mentalizr.backend.accessControl;

import org.mentalizr.backend.accessControl.roles.M7rUser;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;

public class RequirementsFulfill {

    public static void policyConsent(M7rUser m7rUser) throws DataSourceException {
        UserVO userVO = m7rUser.getUserVO();
        userVO.setPolicyConsent(System.currentTimeMillis());
        UserDAO.update(userVO);
    }

}
