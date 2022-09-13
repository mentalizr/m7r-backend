package org.mentalizr.backend.accessControl.roles;

import de.arthurpicht.webAccessControl.session.attributes.User;
import org.mentalizr.backend.exceptions.InfrastructureException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserAccessKeyCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;

public class UserFactory {

    public static User createUserForRole(UserLoginCompositeVO userLoginCompositeVO) {
        try {
            if (userLoginCompositeVO.isInRolePatient()) {
                return new PatientLogin(userLoginCompositeVO);
            } else if (userLoginCompositeVO.isInRoleTherapist()) {
                return new Therapist(userLoginCompositeVO);
            } else if (userLoginCompositeVO.isInRoleAdmin()) {
                return new Admin(userLoginCompositeVO);
            } else {
                throw new IllegalStateException("[" + userLoginCompositeVO.getUserLoginVO().getUsername() + "] Unknown role.");
            }
        } catch (DataSourceException e) {
            throw new RuntimeException(e);
        }
    }

    public static User createUserSessionForAccessKeyUser(UserAccessKeyCompositeVO userAccessKeyCompositeVO) {
        try {
            return new PatientAnonymous(userAccessKeyCompositeVO);
        } catch (DataSourceException e) {
            throw new RuntimeException(e);
        }
    }

}
