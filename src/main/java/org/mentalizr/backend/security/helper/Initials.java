package org.mentalizr.backend.security.helper;

import de.arthurpicht.utils.core.strings.Strings;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserLoginVO;

public class Initials {

    public static String obtain(UserLoginVO userLoginVO) {
        String initials = "";
        if (Strings.isSpecified(userLoginVO.getFirstName())) {
            initials += userLoginVO.getFirstName().substring(0, 1);
        }
        if (Strings.isSpecified(userLoginVO.getLastName())) {
            initials += userLoginVO.getLastName().substring(0, 1);
        }
        if (initials.isBlank()) {
            initials = "NN";
        } else if (initials.length() == 1) {
            initials += " ";
        }
        return initials;
    }

}
