package org.mentalizr.backend.patientsOverviewSOCreator;

import org.mentalizr.backend.security.helper.DisplayName;
import org.mentalizr.backend.security.helper.Initials;
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
        patientOverviewSO.setHasUpdate(currentActivity.hasUpdate());
        patientOverviewSO.setHasReceiveStatus(currentActivity.hasReceiveStatus());
        patientOverviewSO.setReceived(currentActivity.isReceived());
        patientOverviewSO.setLastActiveDate(currentActivity.getLastActiveAsGermanDateTime());
        patientOverviewSO.setOverviewMessage(currentActivity.getOverviewMessage());

        return patientOverviewSO;
    }

}
