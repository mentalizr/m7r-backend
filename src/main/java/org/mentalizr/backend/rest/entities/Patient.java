package org.mentalizr.backend.rest.entities;

import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;
import de.arthurpicht.utils.core.strings.Strings;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Patient {

    private String name;
    private String userId;

    public Patient() {}

    public Patient(UserLoginCompositeVO userLoginCompositeVO) {
        this.userId = userLoginCompositeVO.getUserId();

        String name = "";
        if (!Strings.isNullOrEmpty(userLoginCompositeVO.getFirstName())) {
            name += userLoginCompositeVO.getFirstName();
        }
        if (!Strings.isNullOrEmpty(userLoginCompositeVO.getLastName())) {
            if (name.length() > 0) name += " ";
            name += userLoginCompositeVO.getLastName();
        }
        if (name.length() > 0) {
            this.name = name;
        } else {
            this.name = userLoginCompositeVO.getUsername();
        }
    }

    public Patient(UserVO userVO) {
        this.userId = userVO.getId();
        this.name = "N.N.";
    }

    public Patient(String name, String userId) {
        this.name = name;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
