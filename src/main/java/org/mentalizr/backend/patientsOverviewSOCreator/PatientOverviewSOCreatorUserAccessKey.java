package org.mentalizr.backend.patientsOverviewSOCreator;

import org.mentalizr.persistence.rdbms.barnacle.vo.UserAccessKeyVO;
import org.mentalizr.serviceObjects.frontend.therapist.PatientOverviewSO;

public class PatientOverviewSOCreatorUserAccessKey implements PatientOverviewSOCreator {

    private final UserAccessKeyVO userAccessKeyVO;

    public PatientOverviewSOCreatorUserAccessKey(UserAccessKeyVO userAccessKeyVO) {
        this.userAccessKeyVO = userAccessKeyVO;
    }


    @Override
    public PatientOverviewSO create() {
        PatientOverviewSO patientOverviewSO = new PatientOverviewSO();

        patientOverviewSO.setUserId(this.userAccessKeyVO.getUserId());
        patientOverviewSO.setDisplayName("N.N.");
        patientOverviewSO.setInitials("NN");

        // TODO
        patientOverviewSO.setHasUpdate(false);
        patientOverviewSO.setHasReceiveStatus(false);
        patientOverviewSO.setLastActiveDate("01.01.2020");
        patientOverviewSO.setOverviewMessage("This is a dummy overview message.");

        return patientOverviewSO;
    }
}
