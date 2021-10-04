package org.mentalizr.backend.adapter;

import org.mentalizr.persistence.mongo.DocumentNotFoundException;
import org.mentalizr.persistence.mongo.formData.FormDataDAO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;

public class FormDataFetcherMongo implements FormDataFetcher {

    @Override
    public FormDataSO fetch(String userId, String contentId) {
        try {
            return FormDataDAO.fetch(userId, contentId);
        } catch (DocumentNotFoundException e) {
            return null;
        }
    }

}
