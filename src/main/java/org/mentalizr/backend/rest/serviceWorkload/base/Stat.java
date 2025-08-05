package org.mentalizr.backend.rest.serviceWorkload.base;

import org.mentalizr.persistence.mongo.activityStatus.ActivityMessageMongoHandler;
import org.mentalizr.persistence.mongo.formData.FormDataMongoHandler;
import org.mentalizr.persistence.mongo.patientStatus.PatientStatusMongoHandler;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.edao.*;
import org.mentalizr.serviceObjects.base.StatSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Stat {

    private final StatSO statSO;

    private static final Logger log = LoggerFactory.getLogger(Stat.class);

    public Stat() throws DataSourceException {
        this.statSO = new StatSO();
        this.statSO.setNrOfUsers(UserEDAO.findAllIds().size());
        this.statSO.setNrOfRolePatients(RolePatientEDAO.findAllIds().size());
        this.statSO.setNrOfUserLogin(UserLoginEDAO.findAllIds().size());
        this.statSO.setNrOfUserAccessKeys(UserAccessKeyEDAO.findAllIds().size());
        this.statSO.setNrOfRoleAdmins(RoleAdminEDAO.findAllIds().size());
        this.statSO.setNrOfRoleTherapists(RoleTherapistEDAO.findAllIds().size());
        this.statSO.setNrOfUsersWithActivityRecords(
                ActivityMessageMongoHandler.getDistinctUserIds().size()
        );
        this.statSO.setNrOfUsersWithFormDataDocuments(
                FormDataMongoHandler.getDistinctUserIds().size()
        );
        this.statSO.setNrOfUsersWithPatientStatusDocuments(
                PatientStatusMongoHandler.getDistinctUserIds().size()
        );
        this.statSO.setNrOfActivityRecords(ActivityMessageMongoHandler.getNrOfDocuments());
        this.statSO.setNrOfFormDataDocuments(FormDataMongoHandler.getNrOfDocuments());
        this.statSO.setNrOfPatientStatusDocuments(PatientStatusMongoHandler.getNrOfDocuments());
        this.statSO.setNrOfProjects(ProjectEDAO.getProjectCount());
        this.statSO.setNrOfPrograms(ProgramEDAO.getProgramCount());
    }

    public StatSO getStatSO() {
        return statSO;
    }

    public boolean isEmptyExceptAdmins() {
        return this.statSO.getNrOfRolePatients() == 0
                && this.statSO.getNrOfRoleTherapists() == 0
                && this.statSO.getNrOfPrograms() == 0
                && this.statSO.getNrOfProjects() == 0
                && this.statSO.getNrOfActivityRecords() == 0
                && this.statSO.getNrOfFormDataDocuments() == 0
                && this.statSO.getNrOfPatientStatusDocuments() == 0;
    }

}
