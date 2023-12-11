package org.mentalizr.backend.activity;

import org.mentalizr.persistence.mongo.DocumentNotFoundException;
import org.mentalizr.persistence.mongo.patientStatus.PatientStatusDAO;
import org.mentalizr.serviceObjects.frontend.patient.PatientStatusSO;

public class PatientStatus {

    public static void update(String userId, String contentId) {
        // TODO better solution? Check if info page by requesting content manager here?
        if (!contentId.contains("_info_")) {
            PatientStatusDAO.updateLastContentId(userId, contentId);
        }
    }

    public static PatientStatusSO obtain(String userId) {
        PatientStatusSO patientStatusSO = null;
        try {
            patientStatusSO = PatientStatusDAO.fetch(userId);
        } catch (DocumentNotFoundException e) {
            patientStatusSO = new PatientStatusSO();
            patientStatusSO.setUserId(userId);
            patientStatusSO.setLastContentId("");
            return patientStatusSO;
        }
        // TODO Check for consistency. Existing in program? If not, return fist contentId
        return patientStatusSO;
    }

}
