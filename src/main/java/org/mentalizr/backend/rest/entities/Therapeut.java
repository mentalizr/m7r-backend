package org.mentalizr.backend.rest.entities;


import org.mentalizr.persistence.rdbms.barnacle.vo.RoleTherapistVO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Therapeut {

    private String name;
    private boolean weiblich;

    public Therapeut() {}

    public Therapeut(UserLoginCompositeVO userLoginCompositVO, RoleTherapistVO roleTherapistVO) {
        this.name = roleTherapistVO.getTitle() + " " + userLoginCompositVO.getFirstName() + " " + userLoginCompositVO.getLastName();
        this.weiblich = userLoginCompositVO.getGender() == 0;
    }

    public Therapeut(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isWeiblich() {
        return weiblich;
    }

    public void setWeiblich(boolean weiblich) {
        this.weiblich = weiblich;
    }
}
