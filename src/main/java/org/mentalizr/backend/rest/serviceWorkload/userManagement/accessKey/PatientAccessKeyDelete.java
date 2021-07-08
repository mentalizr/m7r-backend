package org.mentalizr.backend.rest.serviceWorkload.userManagement.accessKey;

import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.RolePatientDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserAccessKeyDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserAccessKeyPatientCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserAccessKeyVO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyDeleteSO;

public class PatientAccessKeyDelete {

    public static void delete(AccessKeyDeleteSO accessKeyDeleteSO) throws DataSourceException, EntityNotFoundException {

        String accessKey = accessKeyDeleteSO.getAccessKey();
        UserAccessKeyVO userAccessKeyVO = UserAccessKeyDAO.findByUk_accessKey(accessKey);

        UserAccessKeyPatientCompositeDAO.delete(userAccessKeyVO.getUserId());
    }

}
