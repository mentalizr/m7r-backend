package org.mentalizr.backend.rest.serviceWorkload.base;

import de.arthurpicht.utils.core.strings.Strings;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.dao.*;
import org.mentalizr.persistence.rdbms.barnacle.vo.*;
import org.mentalizr.serviceObjects.base.ConsistencyCheckResultSO;

import java.util.ArrayList;
import java.util.List;

public class ConsistencyCheck {

    private final List<UserVO> userVOList;
    private final List<UserLoginVO> userLoginVOList;
    private final List<UserAccessKeyVO> userAccessKeyVOList;
    private final List<RoleAdminVO> roleAdminVOList;
    private final List<RolePatientVO> rolePatientVOList;
    private final List<RoleTherapistVO> roleTherapistVOList;
    private final ConsistencyCheckResultSO consistencyCheckResultSO;

    public ConsistencyCheck() throws DataSourceException {
        this.userVOList = UserDAO.findAll();
        this.userLoginVOList = UserLoginDAO.findAll();
        this.userAccessKeyVOList = UserAccessKeyDAO.findAll();
        this.roleAdminVOList = RoleAdminDAO.findAll();
        this.rolePatientVOList = RolePatientDAO.findAll();
        this.roleTherapistVOList = RoleTherapistDAO.findAll();
        this.consistencyCheckResultSO = new ConsistencyCheckResultSO();

        performChecks();
        setCounts();
    }

    public ConsistencyCheckResultSO getConsistencyCheckResultSO() {
        return this.consistencyCheckResultSO;
    }

    private void performChecks() {
        this.consistencyCheckResultSO.setConsistent(true);

        for (UserVO userVO : this.userVOList) {
            String userId = userVO.getId();

            checkRoleConsistency(userId);
            checkPatientConsistency(userId);
            checkPatientConsistencyReverse(userId);
        }
    }

    private void setCounts() {
        this.consistencyCheckResultSO.setNrOfUsers(this.userVOList.size());
        this.consistencyCheckResultSO.setNrOfRolePatients(this.rolePatientVOList.size());
        this.consistencyCheckResultSO.setNrOfUserLogin(this.userLoginVOList.size());
        this.consistencyCheckResultSO.setNrOfUserAccessKeys(this.userAccessKeyVOList.size());
        this.consistencyCheckResultSO.setNrOfRoleAdmins(this.roleAdminVOList.size());
        this.consistencyCheckResultSO.setNrOfRoleTherapists(this.roleTherapistVOList.size());
    }

    private void checkRoleConsistency(String userId) {
        boolean rolePatient = isRolePatient(userId);
        boolean roleAdmin = isRoleAdmin(userId);
        boolean roleTherapist = isRoleTherapist(userId);

        if (isExactlyOneTrue(
                rolePatient,
                roleAdmin,
                roleTherapist
        )) return;

        this.consistencyCheckResultSO.setConsistent(false);

        if (isAllFalse(
                rolePatient,
                roleAdmin,
                roleTherapist
        )) {
            this.consistencyCheckResultSO.addMessage("[" + userId + "] has no role.");
        } else {
            List<String> roles = new ArrayList<>();
            if (rolePatient) roles.add("Patient");
            if (roleAdmin) roles.add("Admin");
            if (roleTherapist) roles.add("Therapist");

            this.consistencyCheckResultSO.addMessage("[" + userId + "] has multiple roles: "
                    + Strings.listing(roles, ",", "", "", "[", "]"));
        }
    }

    private void checkPatientConsistency(String userId) {
        if (!isRolePatient(userId)) return;

        boolean userLogin = isUserLogin(userId);
        boolean userAccessKey = isAccessKey(userId);

        if (isExactlyOneTrue(
                userLogin,
                userAccessKey
        )) return;

        this.consistencyCheckResultSO.setConsistent(false);

        if (isAllFalse(
                userLogin,
                userAccessKey
        )) {
            this.consistencyCheckResultSO.addMessage("[" + userId + "] is in role [Patient] but in nor UserLogin nor AccessKey.");
        } else {
            List<String> patientType = new ArrayList<>();
            if (userLogin) patientType.add("UserLogin");
            if (userAccessKey) patientType.add("AccessKey");

            this.consistencyCheckResultSO.addMessage("[" + userId + "] has multiple patient types: "
                    + Strings.listing(patientType, ",", "", "", "[", "]"));
        }
    }

    private void checkPatientConsistencyReverse(String userId) {
        boolean userLogin = isUserLogin(userId);
        boolean userAccessKey = isAccessKey(userId);

        if (userLogin || userAccessKey) {
            // TODO userLogin kann auch admin oder therapist sein ...
            if (!isRolePatient(userId)) {
                this.consistencyCheckResultSO.setConsistent(false);

                String userType = userLogin ? "UserLogin" : "AccessKey";
                this.consistencyCheckResultSO.addMessage("[" + userId + "] is " + userType + " but has no role patient.");
            }
        }
    }

    private boolean isUserLogin(String userId) {
        return this.userLoginVOList.stream()
                .anyMatch(userLoginVO -> userLoginVO.getUserId().equals(userId));
    }

    private boolean isAccessKey(String userId) {
        return this.userAccessKeyVOList.stream()
                .anyMatch(userAccessKeyVO -> userAccessKeyVO.getUserId().equals(userId));
    }

    private boolean isRoleAdmin(String userId) {
        return this.roleAdminVOList.stream()
                .anyMatch(roleAdminVO -> roleAdminVO.getUserId().equals(userId));
    }

    private boolean isRolePatient(String userId) {
        return this.rolePatientVOList.stream()
                .anyMatch(rolePatientVO -> rolePatientVO.getUserId().equals(userId));
    }

    private boolean isRoleTherapist(String userId) {
        return this.roleTherapistVOList.stream()
                .anyMatch(roleTherapistVO -> roleTherapistVO.getUserId().equals(userId));
    }

    private static boolean isExactlyOneTrue(boolean... values) {
        int count = 0;
        for (boolean value : values) {
            if (value) {
                count++;
                if (count > 1) return false;
            }
        }
        return count == 1;
    }

    private static boolean isAllFalse(boolean... values) {
        for (boolean value : values) {
            if (value) return false;
        }
        return true;
    }

}
