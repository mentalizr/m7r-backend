package org.mentalizr.backend.rest.service.assertPrecondition;

import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.ProgramDAO;

public class AssertProgram {

    public static void notExisting(String programId) throws DataSourceException, ServicePreconditionFailedException {

        try {
            ProgramDAO.load(programId);
            throw new ServicePreconditionFailedException("Program [" + programId + "] is existing.");
        } catch (EntityNotFoundException e) {
            // DIN
        }

    }

    public static void exists(String programId) throws DataSourceException, ServicePreconditionFailedException {
        exists(programId, "Program [%s] not existing.");
    }

    public static void exists(String programId, String messageTemplate) throws DataSourceException, ServicePreconditionFailedException {
        try {
            ProgramDAO.load(programId);
        } catch (EntityNotFoundException e) {
            throw new ServicePreconditionFailedException(String.format(messageTemplate, programId));
        }
    }


}
