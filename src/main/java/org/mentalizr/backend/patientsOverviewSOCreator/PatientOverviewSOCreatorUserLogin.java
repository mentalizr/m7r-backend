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
        CurrentActivity currentActivity = new CurrentActivity(this.userLoginVO.getUserId());
        PatientOverviewSO patientOverviewSO = new PatientOverviewSO();

        patientOverviewSO.setUserId(this.userLoginVO.getUserId());
        patientOverviewSO.setDisplayName(DisplayName.obtain(this.userLoginVO));
        patientOverviewSO.setInitials(Initials.obtain(this.userLoginVO));

        // TODO
        patientOverviewSO.setHasUpdate(false);
        patientOverviewSO.setHasReceiveStatus(false);
        patientOverviewSO.setLastActiveDate(currentActivity.getLastActiveAsGermanDate());
        patientOverviewSO.setOverviewMessage(currentActivity.getOverviewMessage());

        return patientOverviewSO;
    }

}
