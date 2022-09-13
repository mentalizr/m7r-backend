package org.mentalizr.backend.accessControl.helper;

import de.arthurpicht.utils.core.strings.Strings;
import org.mentalizr.persistence.rdbms.barnacle.vo.RoleTherapistVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserLoginVO;

public class DisplayName {

    public static String obtain(UserLoginVO userLoginVO) {
        String displayName = "";
        if (Strings.isSpecified(userLoginVO.getFirstName())) {
            displayName += userLoginVO.getFirstName();
        }
        if (Strings.isSpecified(userLoginVO.getLastName())) {
            if (displayName.length() > 0) displayName += " ";
            displayName += userLoginVO.getLastName();
        }
        if (displayName.isBlank()) {
            displayName = userLoginVO.getUsername();
        }
        return displayName;
    }

    public static String obtain(UserLoginVO userLoginVO, RoleTherapistVO roleTherapistVO) {
        String displayName = "";
        if (Strings.isSpecified(roleTherapistVO.getTitle())) {
            displayName = roleTherapistVO.getTitle() + " ";
        }
        displayName += obtain(userLoginVO);
        return displayName;
    }

}
