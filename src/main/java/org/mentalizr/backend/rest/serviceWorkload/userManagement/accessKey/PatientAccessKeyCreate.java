package org.mentalizr.backend.rest.serviceWorkload.userManagement.accessKey;

import org.mentalizr.backend.exceptions.M7rInfrastructureException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserAccessKeyDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserAccessKeyPatientCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserAccessKeyPatientCompositeVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.*;
import org.mentalizr.persistence.rdbms.utils.PasswordGenerator;
import org.mentalizr.serviceObjects.userManagement.AccessKeyCollectionSO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyCreateSO;
import org.mentalizr.serviceObjects.userManagement.AccessKeyRestoreSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PatientAccessKeyCreate {

    private static final Logger logger = LoggerFactory.getLogger(PatientAccessKeyCreate.class);

    private static final int ACCESS_KEY_LENGTH = 12;

    public static AccessKeyCollectionSO create(AccessKeyCreateSO accessKeyCreateSO) throws M7rInfrastructureException {
        try {
            return createInternal(accessKeyCreateSO);
        } catch (DataSourceException e) {
            throw new M7rInfrastructureException(e.getMessage(), e);
        }
    }

    private static AccessKeyCollectionSO createInternal(AccessKeyCreateSO accessKeyCreateSO) throws DataSourceException {
        Set<String> accessKeys = generateAccessKeys(
                accessKeyCreateSO.getStartWith(),
                accessKeyCreateSO.getNrOfKeys());

        AccessKeyCollectionSO accessKeyCollectionSO = new AccessKeyCollectionSO();
        long creationTimestamp = System.currentTimeMillis();
        for (String accessKey : accessKeys) {
            String uuid = UUID.randomUUID().toString();
            createUserAccessKeyPatientComposite(
                    uuid,
                    accessKeyCreateSO.isActive(),
                    creationTimestamp,
                    null,
                    null,
                    accessKey,
                    accessKeyCreateSO.getProgramId(),
                    accessKeyCreateSO.getTherapistId());

            AccessKeyRestoreSO accessKeyRestoreSO = new AccessKeyRestoreSO();
            accessKeyRestoreSO.setUserId(uuid);
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
            String userId,
            boolean active,
            Long creationTimestamp,
            Long firstActive,
            Long lastActive,
            String accessKey,
            String programId,
            String therapistId
    ) throws DataSourceException {

        UserVO userVO = new UserVO(userId);
        userVO.setActive(active);
        userVO.setCreation(creationTimestamp);
        userVO.setFirstActive(firstActive);
        userVO.setLastActive(lastActive);

        UserAccessKeyVO userAccessKeyVO = new UserAccessKeyVO(userId);
        userAccessKeyVO.setAccessKey(accessKey);

        RolePatientVO rolePatientVO = new RolePatientVO(userId);
        rolePatientVO.setTherapistId(therapistId);

        PatientProgramVO patientProgramVO = new PatientProgramVO(new PatientProgramPK(userId, programId));

        UserAccessKeyPatientCompositeVO userAccessKeyPatientCompositeVO =
                new UserAccessKeyPatientCompositeVO(userVO, userAccessKeyVO, rolePatientVO, patientProgramVO);
        UserAccessKeyPatientCompositeDAO.create(userAccessKeyPatientCompositeVO);
    }

}
