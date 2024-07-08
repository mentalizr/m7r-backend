package org.mentalizr.backend.rest.service.assertPrecondition;

import de.arthurpicht.utils.core.strings.Strings;
import org.mentalizr.backend.exceptions.M7rInfrastructureException;
import org.mentalizr.backend.rest.service.ServicePreconditionFailedException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.RolePatientDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RolePatientVO;

public class AssertRolePatient {

    public static void existsAsNotAssigned(String userId) throws M7rInfrastructureException, ServicePreconditionFailedException {
        try {
            RolePatientVO rolePatientVO = RolePatientDAO.load(userId);
            String projectId = rolePatientVO.getProjectId();
            if (Strings.isSpecified(projectId)) throw new ServicePreconditionFailedException(
                    "Patient [" + userId + "] is assigned to project [" + rolePatientVO.getProjectId() + "]."
            );
        } catch (DataSourceException e) {
            throw new M7rInfrastructureException(e.getMessage(), e);
        } catch (EntityNotFoundException e) {
            throw new ServicePreconditionFailedException("Patient [" + userId + "] not found.");
        }
    }

    public static void existsAsAssigned(String userId) throws M7rInfrastructureException, ServicePreconditionFailedException {
        try {
            RolePatientVO rolePatientVO = RolePatientDAO.load(userId);
            String projectId = rolePatientVO.getProjectId();
            if (Strings.isUnspecified(projectId)) throw new ServicePreconditionFailedException(
                    "Patient [" + userId + "] is not assigned to project."
            );
        } catch (DataSourceException e) {
            throw new M7rInfrastructureException(e.getMessage(), e);
        } catch (EntityNotFoundException e) {
            throw new ServicePreconditionFailedException("Patient [" + userId + "] not found.");
        }
    }


}
