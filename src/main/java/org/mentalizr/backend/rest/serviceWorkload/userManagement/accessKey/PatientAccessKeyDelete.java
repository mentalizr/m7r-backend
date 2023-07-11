package org.mentalizr.backend.rest.serviceWorkload.userManagement.accessKey;

import org.mentalizr.backend.exceptions.M7rInfrastructureException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserAccessKeyDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserAccessKeyPatientCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserAccessKeyVO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyDeleteSO;

@Deprecated
public class PatientAccessKeyDelete {

    @Deprecated
    public static void delete(AccessKeyDeleteSO accessKeyDeleteSO) throws M7rInfrastructureException {
        try {
            String accessKey = accessKeyDeleteSO.getAccessKey();
            UserAccessKeyVO userAccessKeyVO = UserAccessKeyDAO.findByUk_accessKey(accessKey);
            UserAccessKeyPatientCompositeDAO.delete(userAccessKeyVO.getUserId());
        } catch (EntityNotFoundException | DataSourceException e) {
            throw new M7rInfrastructureException(e.getMessage(), e);
        }
    }

}
