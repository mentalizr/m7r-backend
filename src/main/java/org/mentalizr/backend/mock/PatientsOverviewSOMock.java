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
                "Sonja Sonnenschein", true,
                "Eine Nachrichtenzeile",
                "2021-07-08T06:30:00"
        );
        patientsOverviewSOList.add(patientOverviewSO);

        patientOverviewSO
                = new PatientOverviewSO(
                "mock-id-1",
                "Max Mustermann", false,
                "Die erste Zeile einer Nachricht",
                "2021-07-07T20:30:00",
                true
        );
        patientsOverviewSOList.add(patientOverviewSO);

        patientOverviewSO
                = new PatientOverviewSO(
                "mock-id-3",
                "Mina Musterfrau", false,
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.",
                "2021-07-07T20:30:00",
                true
        );
        patientsOverviewSOList.add(patientOverviewSO);

        return new PatientsOverviewSO(patientsOverviewSOList);
    }

}
