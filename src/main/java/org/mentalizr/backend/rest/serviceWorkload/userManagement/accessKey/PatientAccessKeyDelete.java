package org.mentalizr.backend.rest.serviceWorkload.userManagement.accessKey;

import org.mentalizr.backend.exceptions.InfrastructureException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserAccessKeyDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserAccessKeyPatientCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserAccessKeyVO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyDeleteSO;

public class PatientAccessKeyDelete {

    public static void delete(AccessKeyDeleteSO accessKeyDeleteSO) throws InfrastructureException {
        try {
            String accessKey = accessKeyDeleteSO.getAccessKey();
            UserAccessKeyVO userAccessKeyVO = UserAccessKeyDAO.findByUk_accessKey(accessKey);
            UserAccessKeyPatientCompositeDAO.delete(userAccessKeyVO.getUserId());
        } catch (EntityNotFoundException | DataSourceException e) {
            throw new InfrastructureException(e.getMessage(), e);
        }
    }

}
