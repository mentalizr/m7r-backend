package org.mentalizr.backend.rest.serviceWorkload.userManagement.accessKey;

import org.mentalizr.backend.exceptions.M7rInfrastructureException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.serviceObjects.userManagement.AccessKeyRestoreSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatientAccessKeyRestore {

    private static final Logger logger = LoggerFactory.getLogger(PatientAccessKeyRestore.class);

    private static final int ACCESS_KEY_LENGTH = 12;

    public static void restore(AccessKeyRestoreSO accessKeyRestoreSO) throws M7rInfrastructureException {

        try {
            PatientAccessKeyCreate.createUserAccessKeyPatientComposite(
                    accessKeyRestoreSO.getUserId(),
                    accessKeyRestoreSO.isActive(),
                    accessKeyRestoreSO.getAccessKey(),
                    accessKeyRestoreSO.getProgramId(),
                    accessKeyRestoreSO.getTherapistId());
        } catch (DataSourceException e) {
            throw new M7rInfrastructureException(e.getMessage(), e);
        }
    }

}
