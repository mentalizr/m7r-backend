package org.mentalizr.backend.activity;

import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.contentManager.ContentManager;
import org.mentalizr.persistence.mongo.DocumentNotFoundException;
import org.mentalizr.persistence.mongo.patientStatus.PatientStatusDAO;
import org.mentalizr.serviceObjects.frontend.patient.PatientStatusSO;

public class PatientStatus {

    public static void update(String userId, String contentId) {
        ContentManager contentManager = ApplicationContext.getContentManager();
        if (!contentManager.isInfoTextContent(contentId)) {
            PatientStatusDAO.updateLastContentId(userId, contentId);
        }
    }

    public static PatientStatusSO obtain(String userId) {
        ContentManager contentManager = ApplicationContext.getContentManager();
        try {
            PatientStatusSO patientStatusSO = PatientStatusDAO.fetch(userId);
            if (!contentManager.hasContent(patientStatusSO.getLastContentId())) {
                patientStatusSO.setLastContentId("");
            }
            return patientStatusSO;
        } catch (DocumentNotFoundException e) {
            PatientStatusSO patientStatusSO = new PatientStatusSO();
            patientStatusSO.setUserId(userId);
            patientStatusSO.setLastContentId("");
            return patientStatusSO;
        }
    }

}
