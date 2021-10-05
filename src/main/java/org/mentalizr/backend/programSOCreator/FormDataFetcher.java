package org.mentalizr.backend.programSOCreator;

import org.mentalizr.persistence.mongo.DocumentNotFoundException;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;

public interface FormDataFetcher {

    FormDataSO fetch(String userId, String contentId) throws DocumentNotFoundException;

}
