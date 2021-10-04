package org.mentalizr.backend.adapter;

public class FormDataFetcherRegistry {

    private static FormDataFetcher formDataFetcher = null;

    public static FormDataFetcher getInstance() {
        if (formDataFetcher == null) formDataFetcher = new FormDataFetcherMongo();
        return formDataFetcher;
    }

}
