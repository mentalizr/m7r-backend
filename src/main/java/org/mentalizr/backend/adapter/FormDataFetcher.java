package org.mentalizr.backend.adapter;

import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;

public interface FormDataFetcher {

    public FormDataSO fetch(String userId, String contentId);

}
