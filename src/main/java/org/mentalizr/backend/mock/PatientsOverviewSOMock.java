package org.mentalizr.backend.mock;

import org.mentalizr.serviceObjects.frontend.therapist.PatientOverviewSO;
import org.mentalizr.serviceObjects.frontend.therapist.PatientsOverviewSO;

import java.util.ArrayList;
import java.util.List;

public class PatientsOverviewSOMock {

    public static PatientsOverviewSO createPatientsOverviewSO() {

        List<PatientOverviewSO> patientsOverviewSOList = new ArrayList<>();

        PatientOverviewSO patientOverviewSO
                = new PatientOverviewSO(
                "mock-id-1",
                "Sonja Sonnenschein",
                "SS",
                true,
                "Eine Nachrichtenzeile",
                "2021-07-08"
        );
        patientsOverviewSOList.add(patientOverviewSO);

        patientOverviewSO
                = new PatientOverviewSO(
                "mock-id-2",
                "Max Mustermann",
                "MM",
                false,
                "Die erste Zeile einer Nachricht",
                "2021-07-07",
                false
        );
        patientsOverviewSOList.add(patientOverviewSO);

        patientOverviewSO
                = new PatientOverviewSO(
                "mock-id-3",
                "Mina Musterfrau",
                "MM",
                false,
                "Lorem ipsum dolor sit amet",
                "2021-07-07",
                true
        );
        patientsOverviewSOList.add(patientOverviewSO);

        return new PatientsOverviewSO(patientsOverviewSOList);
    }

}
