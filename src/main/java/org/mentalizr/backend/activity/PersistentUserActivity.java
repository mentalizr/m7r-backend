package org.mentalizr.backend.activity;

import de.arthurpicht.webAccessControl.auth.Authorization;
import org.mentalizr.backend.accessControl.roles.M7rUser;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserAccessKeyCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserLoginCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserAccessKeyCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;

public class PersistentUserActivity {

    public static void update(UserVO userVO) {
        Long currentTimestamp = System.currentTimeMillis();
        if (userVO.getFirstActive() == null)
            userVO.setFirstActive(currentTimestamp);
        userVO.setLastActive(currentTimestamp);
        try {
            UserDAO.update(userVO);
        } catch (DataSourceException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateByUsername(String username) {
        try {
            UserLoginCompositeVO userLoginCompositeVO = UserLoginCompositeDAO.findByUk_username(username);
            UserVO userVO = userLoginCompositeVO.getUserVO();
            update(userVO);
        } catch (DataSourceException | EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateByAccessKey(String accessKey) {
        try {
            UserAccessKeyCompositeVO userAccessKeyCompositeVO = UserAccessKeyCompositeDAO.findByAccessKey(accessKey);
            UserVO userVO = userAccessKeyCompositeVO.getUserVO();
            update(userVO);
        } catch (DataSourceException | EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void update(Authorization authorization) {
        M7rUser m7rUser = (M7rUser) authorization.getUser();
        UserVO userVO = m7rUser.getUserVO();
        update(userVO);
    }

}
