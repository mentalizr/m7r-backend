package org.mentalizr.backend.security.auth;

import org.mentalizr.backend.security.session.attributes.SecurityAttribute;
import org.mentalizr.backend.security.session.attributes.user.AdminHttpSessionAttribute;
import org.mentalizr.backend.security.session.attributes.user.PatientHttpSessionAttribute;
import org.mentalizr.backend.security.session.attributes.user.TherapistHttpSessionAttribute;
import org.mentalizr.backend.security.session.attributes.user.UserHttpSessionAttribute;
import org.mentalizr.backend.rest.entities.UserRole;

import javax.servlet.http.HttpServletRequest;

public class Authorization {

    private final Authentication authentication;

    public Authorization(Authentication authentication) {
        this.authentication = authentication;
    }

    public Authorization(HttpServletRequest httpServletRequest) throws UnauthorizedException {
        this.authentication = new Authentication(httpServletRequest);
    }

    public boolean isPatient() {
        return this.authentication.getUserHttpSessionAttribute() instanceof PatientHttpSessionAttribute;
    }

    public boolean isTherapist() {
        return this.authentication.getUserHttpSessionAttribute() instanceof TherapistHttpSessionAttribute;
    }

    public boolean isAdmin() {
        return this.authentication.getUserHttpSessionAttribute() instanceof AdminHttpSessionAttribute;
    }

    public SecurityAttribute getSecurityAttribute() {
        return this.authentication.getSecurityAttribute();
    }

    public UserHttpSessionAttribute getUserHttpSessionAttribute() {
        return (UserHttpSessionAttribute) this.authentication.getUserHttpSessionAttribute();
    }

    public PatientHttpSessionAttribute getPatientHttpSessionAttribute() {
        if (!isPatient()) throw new IllegalStateException("UserLogin not in role PATIENT.");
        return (PatientHttpSessionAttribute) this.authentication.getUserHttpSessionAttribute();
    }

    public TherapistHttpSessionAttribute getTherapistHttpSessionAttribute() {
        if (!isTherapist()) throw new IllegalStateException("UserLogin not in role THERAPIST.");
        return (TherapistHttpSessionAttribute) this.authentication.getUserHttpSessionAttribute();
    }

    public AdminHttpSessionAttribute getAdminHttpSessionAttribute() {
        if (!isAdmin()) throw new IllegalStateException("UserLogin not in role ADMIN.");
        return (AdminHttpSessionAttribute) this.authentication.getUserHttpSessionAttribute();
    }

    public UserRole getUserRole() {
        return this.authentication.getUserHttpSessionAttribute().getUserRole();
    }

}
