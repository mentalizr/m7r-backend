package org.mentalizr.backend.patientsOverviewSOCreator;

import org.mentalizr.backend.auth.DisplayName;
import org.mentalizr.backend.auth.Initials;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserLoginVO;
import org.mentalizr.serviceObjects.frontend.therapist.PatientOverviewSO;

public class PatientOverviewSOCreatorUserLogin implements PatientOverviewSOCreator {

    private final UserLoginVO userLoginVO;

    public PatientOverviewSOCreatorUserLogin(UserLoginVO userLoginVO) {
        this.userLoginVO = userLoginVO;
    }

    @Override
    public PatientOverviewSO create() {
        PatientOverviewSO patientOverviewSO = new PatientOverviewSO();

        patientOverviewSO.setUserId(this.userLoginVO.getUserId());
        patientOverviewSO.setDisplayName(DisplayName.obtain(this.userLoginVO));
        patientOverviewSO.setInitials(Initials.obtain(this.userLoginVO));

        // TODO
        patientOverviewSO.setHasUpdate(false);
        patientOverviewSO.setHasReceiveStatus(false);
        patientOverviewSO.setLastActiveDate("01.01.2020");
        patientOverviewSO.setOverviewMessage("This is a dummy overview message.");

        return patientOverviewSO;
    }

}
