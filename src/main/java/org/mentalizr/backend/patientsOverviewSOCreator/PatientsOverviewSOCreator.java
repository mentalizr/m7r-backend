package org.mentalizr.backend.patientsOverviewSOCreator;

import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.vo.RolePatientVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RoleTherapistVO;
import org.mentalizr.serviceObjects.frontend.therapist.PatientOverviewSO;
import org.mentalizr.serviceObjects.frontend.therapist.PatientsOverviewSO;

import java.util.ArrayList;
import java.util.List;

public class PatientsOverviewSOCreator {

    private final RoleTherapistVO roleTherapistVO;

    public PatientsOverviewSOCreator(RoleTherapistVO roleTherapistVO) {
        this.roleTherapistVO = roleTherapistVO;
    }

    public PatientsOverviewSO create() {
        List<RolePatientVO> rolePatientVOList = obtainRelatedPatients(roleTherapistVO);
        List<PatientOverviewSO> patientOverviewSOList = new ArrayList<>();
        for (RolePatientVO rolePatientVO : rolePatientVOList) {
            PatientOverviewSOCreator patientOverviewSOCreator = PatientOverviewSOCreatorFactory.getInstance(rolePatientVO);
            PatientOverviewSO patientOverviewSO = patientOverviewSOCreator.create();
            patientOverviewSOList.add(patientOverviewSO);
        }
        return new PatientsOverviewSO(patientOverviewSOList);
    }

    private List<RolePatientVO> obtainRelatedPatients(RoleTherapistVO roleTherapistVO) {
        try {
            return roleTherapistVO.getRolePatientVOByFk_therapist_id();
        } catch (DataSourceException e) {
            throw new RuntimeException(e);
        }
    }

}
