package org.mentalizr.backend.rest.serviceWorkload.base;

import de.arthurpicht.utils.core.strings.Strings;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.edao.*;
import org.mentalizr.serviceObjects.base.ConsistencyCheckResultSO;

import java.util.ArrayList;
import java.util.List;

public class ConsistencyCheck {

    private final List<String> userIdList;
    private final List<String> userLoginList;
    private final List<String> userAccessKeyList;
    private final List<String> roleAdminList;
    private final List<String> rolePatientList;
    private final List<String> roleTherapistList;

    private final ConsistencyCheckResultSO consistencyCheckResultSO;

    public ConsistencyCheck() throws DataSourceException {
        this.userIdList = UserEDAO.findAllIds();
        this.userLoginList = UserLoginEDAO.findAllIds();
        this.userAccessKeyList = UserAccessKeyEDAO.findAllIds();
        this.roleAdminList = RoleAdminEDAO.findAllIds();
        this.rolePatientList = RolePatientEDAO.findAllIds();
        this.roleTherapistList = RoleTherapistEDAO.findAllIds();

        this.consistencyCheckResultSO = new ConsistencyCheckResultSO();

        performChecks();
        setCounts();
    }

    public ConsistencyCheckResultSO getConsistencyCheckResultSO() {
        return this.consistencyCheckResultSO;
    }

    private void performChecks() {
        this.consistencyCheckResultSO.setConsistent(true);

        for (String userId : this.userIdList) {
            checkRoleConsistency(userId);
            checkPatientConsistency(userId);
            checkPatientConsistencyReverse(userId);
        }
    }

    private void setCounts() {
        this.consistencyCheckResultSO.setNrOfUsers(this.userIdList.size());
        this.consistencyCheckResultSO.setNrOfRolePatients(this.rolePatientList.size());
        this.consistencyCheckResultSO.setNrOfUserLogin(this.userLoginList.size());
        this.consistencyCheckResultSO.setNrOfUserAccessKeys(this.userAccessKeyList.size());
        this.consistencyCheckResultSO.setNrOfRoleAdmins(this.roleAdminList.size());
        this.consistencyCheckResultSO.setNrOfRoleTherapists(this.roleTherapistList.size());
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
        boolean isRoleAdmin = isRoleAdmin(userId);
        boolean isRolePatient = isRolePatient(userId);
        boolean isRoleTherapist = isRoleTherapist(userId);

        if (userAccessKey && !isRolePatient) {
            this.consistencyCheckResultSO.setConsistent(false);
            this.consistencyCheckResultSO.addMessage("[" + userId + "] is accessKey but has no role patient.");
        }

        if (userLogin && !isRoleAdmin && !isRolePatient && !isRoleTherapist) {
            this.consistencyCheckResultSO.setConsistent(false);
            this.consistencyCheckResultSO.addMessage("[" + userId + "] is UserLogin but has no role.");
        }
    }

    private boolean isUserLogin(String userId) {
        return this.userLoginList.contains(userId);
    }

    private boolean isAccessKey(String userId) {
        return this.userAccessKeyList.contains(userId);
    }

    private boolean isRoleAdmin(String userId) {
        return this.roleAdminList.contains(userId);
    }

    private boolean isRolePatient(String userId) {
        return this.rolePatientList.contains(userId);
    }

    private boolean isRoleTherapist(String userId) {
        return this.roleTherapistList.contains(userId);
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
