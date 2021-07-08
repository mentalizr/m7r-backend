package org.mentalizr.backend.rest.serviceWorkload.userManagement.accessKey;

import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserAccessKeyDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserAccessKeyPatientCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserAccessKeyPatientCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RolePatientVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserAccessKeyVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;
import org.mentalizr.persistence.rdbms.utils.PasswordGenerator;
import org.mentalizr.serviceObjects.userManagement.AccessKeyCollectionSO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyCreateSO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyRestoreSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PatientAccessKeyCreate {

    private static final Logger logger = LoggerFactory.getLogger(PatientAccessKeyCreate.class);

    private static final int ACCESS_KEY_LENGTH = 12;

    public static AccessKeyCollectionSO create(AccessKeyCreateSO accessKeyCreateSO) throws DataSourceException {

        Set<String> accessKeys = generateAccessKeys(
                accessKeyCreateSO.getStartWith(),
                accessKeyCreateSO.getNrOfKeys());

        AccessKeyCollectionSO accessKeyCollectionSO = new AccessKeyCollectionSO();
        for (String accessKey : accessKeys) {
            String uuid = UUID.randomUUID().toString();
            createUserAccessKeyPatientComposite(
                    uuid,
                    accessKeyCreateSO.isActive(),
                    accessKey,
                    accessKeyCreateSO.getProgramId(),
                    accessKeyCreateSO.getTherapistId());

            AccessKeyRestoreSO accessKeyRestoreSO = new AccessKeyRestoreSO();
            accessKeyRestoreSO.setId(uuid);
            accessKeyRestoreSO.setActive(accessKeyCreateSO.isActive());
            accessKeyRestoreSO.setAccessKey(accessKey);
            accessKeyRestoreSO.setProgramId(accessKeyCreateSO.getProgramId());
            accessKeyRestoreSO.setTherapistId(accessKeyCreateSO.getTherapistId());

            accessKeyCollectionSO.getCollection().add(accessKeyRestoreSO);
        }

        return accessKeyCollectionSO;
    }

    private static Set<String> generateAccessKeys(String startWith, int nrOfKeys) throws DataSourceException {
        int accessKeyLength = ACCESS_KEY_LENGTH - startWith.length();
        Set<String> predefinedAccessKeys = getPredefinedAccessKeys();
        PasswordGenerator passwordGenerator = new PasswordGenerator(
                accessKeyLength,
                predefinedAccessKeys,
                PasswordGenerator.CAP_TYPEWRITE
        );

        return generateAccessKeysAsStrings(startWith, passwordGenerator, nrOfKeys);
    }

    private static Set<String> getPredefinedAccessKeys() throws DataSourceException {
        List<UserAccessKeyVO> allAccessKeys = UserAccessKeyDAO.findAll();
        Set<String> predefinedAccessKeys = new HashSet<>();
        for (UserAccessKeyVO userAccessKeyVO : allAccessKeys) {
            predefinedAccessKeys.add(userAccessKeyVO.getAccessKey());
        }
        return predefinedAccessKeys;
    }

    private static Set<String> generateAccessKeysAsStrings(String startWith, PasswordGenerator passwordGenerator, int nrOfKeys) {
        Set<String> accessKeysRandomPart = passwordGenerator.getDistinctPasswords(nrOfKeys);
        Set<String> accessKeys = new HashSet<>();
        for (String accessKeyRandomPart : accessKeysRandomPart) {
            accessKeys.add(startWith + accessKeyRandomPart);
        }
        return accessKeys;
    }

    public static void createUserAccessKeyPatientComposite(
            String uuid,
            boolean active,
            String accessKey,
            String programId,
            String therapistId
    ) throws DataSourceException {

        UserVO userVO = new UserVO(uuid);
        userVO.setActive(active);

        UserAccessKeyVO userAccessKeyVO = new UserAccessKeyVO(uuid);
        userAccessKeyVO.setAccessKey(accessKey);

        RolePatientVO rolePatientVO = new RolePatientVO(uuid);
        rolePatientVO.setProgramId(programId);
        rolePatientVO.setTherapistId(therapistId);

        UserAccessKeyPatientCompositeVO userAccessKeyPatientCompositeVO =
                new UserAccessKeyPatientCompositeVO(userVO, userAccessKeyVO, rolePatientVO);
        UserAccessKeyPatientCompositeDAO.create(userAccessKeyPatientCompositeVO);
    }

}
