package org.mentalizr.backend.rest.serviceWorkload.base;

import de.arthurpicht.utils.core.math.Booleans;
import de.arthurpicht.utils.core.strings.Strings;
import org.mentalizr.persistence.mongo.activityStatus.ActivityMessageMongoHandler;
import org.mentalizr.persistence.mongo.formData.FormDataMongoHandler;
import org.mentalizr.persistence.mongo.patientStatus.PatientStatusMongoHandler;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.edao.*;
import org.mentalizr.serviceObjects.base.ConsistencyCheckResultSO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConsistencyCheck {

    private final List<String> userIdList;
    private final List<String> userLoginList;
    private final List<String> userAccessKeyList;
    private final List<String> roleAdminList;
    private final List<String> rolePatientList;
    private final List<String> roleTherapistList;
    private final Set<String> distinctActivityList;
    private final Set<String> distinctFormDataList;
    private final Set<String> distinctPatientStatusList;

    private final ConsistencyCheckResultSO consistencyCheckResultSO;

    public ConsistencyCheck() throws DataSourceException {
        this.userIdList = UserEDAO.findAllIds();
        this.userLoginList = UserLoginEDAO.findAllIds();
        this.userAccessKeyList = UserAccessKeyEDAO.findAllIds();
        this.roleAdminList = RoleAdminEDAO.findAllIds();
        this.rolePatientList = RolePatientEDAO.findAllIds();
        this.roleTherapistList = RoleTherapistEDAO.findAllIds();
        this.distinctActivityList = ActivityMessageMongoHandler.getDistinctUserIds();
        this.distinctFormDataList = FormDataMongoHandler.getDistinctUserIds();
        this.distinctPatientStatusList = PatientStatusMongoHandler.getDistinctUserIds();

        this.consistencyCheckResultSO = new ConsistencyCheckResultSO();

        this.consistencyCheckResultSO.setConsistent(true);
        performChecks();
        performChecksMongo();
        setCounts();
    }

    public ConsistencyCheckResultSO getConsistencyCheckResultSO() {
        return this.consistencyCheckResultSO;
    }

    private void performChecks() {
        for (String userId : this.userIdList) {
            checkForExactlyOneLoginType(userId);
            checkForExactlyOneRole(userId);
            checkForAccessKeyMatchingPatientRole(userId);
        }
    }

    private void performChecksMongo() {

        for (String userId : this.distinctActivityList) {
            if (!userIdList.contains(userId)) {
                this.consistencyCheckResultSO.setConsistent(false);
                this.consistencyCheckResultSO.addMessage(
                        "Found orphaned activity record related to non existing user [" + userId + "]."
                );
            }
        }

        for (String userId : this.distinctFormDataList) {
            if (!userIdList.contains(userId)) {
                this.consistencyCheckResultSO.setConsistent(false);
                this.consistencyCheckResultSO.addMessage(
                        "Found orphaned form data document related to non existing user [" + userId + "]."
                );
            }
        }

        for (String userId : this.distinctPatientStatusList) {
            if (!userIdList.contains(userId)) {
                this.consistencyCheckResultSO.setConsistent(false);
                this.consistencyCheckResultSO.addMessage(
                        "Found orphaned patient status document related to non existing user [" + userId + "]."
                );
            }
        }

        Set<String> patientStatusListDuplicates = PatientStatusMongoHandler.getDuplicates();
        if (!patientStatusListDuplicates.isEmpty()) {
            this.consistencyCheckResultSO.setConsistent(false);
            for (String patientId : patientStatusListDuplicates) {
                this.consistencyCheckResultSO.addMessage(
                        "Found duplicate patient status documents for user [" + patientId + "].");
            }
        }

    }

    private void setCounts() {
        this.consistencyCheckResultSO.setNrOfUsers(this.userIdList.size());
        this.consistencyCheckResultSO.setNrOfRolePatients(this.rolePatientList.size());
        this.consistencyCheckResultSO.setNrOfUserLogin(this.userLoginList.size());
        this.consistencyCheckResultSO.setNrOfUserAccessKeys(this.userAccessKeyList.size());
        this.consistencyCheckResultSO.setNrOfRoleAdmins(this.roleAdminList.size());
        this.consistencyCheckResultSO.setNrOfRoleTherapists(this.roleTherapistList.size());
        this.consistencyCheckResultSO.setNrOfUsersWithActivityRecords(this.distinctActivityList.size());
        this.consistencyCheckResultSO.setNrOfUsersWithFormDataDocuments(this.distinctFormDataList.size());
        this.consistencyCheckResultSO.setNrOfUsersWithPatientStatusDocuments(this.distinctPatientStatusList.size());
        this.consistencyCheckResultSO.setNrOfActivityRecords(ActivityMessageMongoHandler.getNrOfDocuments());
        this.consistencyCheckResultSO.setNrOfFormDataDocuments(FormDataMongoHandler.getNrOfDocuments());
        this.consistencyCheckResultSO.setNrOfPatientStatusDocuments(PatientStatusMongoHandler.getNrOfDocuments());
    }

    private void checkForExactlyOneLoginType(String userId) {
        boolean userLogin = isUserLogin(userId);
        boolean userAccessKey = isAccessKey(userId);

        if (Booleans.isExactlyOneTrue(
                userLogin,
                userAccessKey
        )) return;

        this.consistencyCheckResultSO.setConsistent(false);

        if (Booleans.isAllFalse(
                userLogin,
                userAccessKey
        )) {
            this.consistencyCheckResultSO.addMessage(
                    "[" + userId + "] is neither UserLogin nor AccessKey.");
        } else {
            this.consistencyCheckResultSO.addMessage(
                    "[" + userId + "] is both UserLogin and AccessKey."
            );
        }
    }

    private void checkForExactlyOneRole(String userId) {
        boolean rolePatient = isRolePatient(userId);
        boolean roleAdmin = isRoleAdmin(userId);
        boolean roleTherapist = isRoleTherapist(userId);

        if (Booleans.isExactlyOneTrue(
                rolePatient,
                roleAdmin,
                roleTherapist
        )) return;

        this.consistencyCheckResultSO.setConsistent(false);

        if (Booleans.isAllFalse(
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

    private void checkForAccessKeyMatchingPatientRole(String userId) {
        boolean userAccessKey = isAccessKey(userId);
        boolean isRoleAdmin = isRoleAdmin(userId);
        boolean isRolePatient = isRolePatient(userId);
        boolean isRoleTherapist = isRoleTherapist(userId);

        if (userAccessKey && !isRolePatient) {
            this.consistencyCheckResultSO.setConsistent(false);
            this.consistencyCheckResultSO.addMessage(
                    "[" + userId + "] is accessKey but has no role patient but " + (isRoleAdmin ? "admin" : "")
                            + (isRoleTherapist ? "Therapist" : "") + ".");
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

}
