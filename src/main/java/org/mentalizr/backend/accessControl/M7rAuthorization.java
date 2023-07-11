package org.mentalizr.backend.accessControl;

import de.arthurpicht.webAccessControl.auth.Authorization;
import org.mentalizr.backend.accessControl.roles.*;

public class M7rAuthorization {

    private final Authorization authorization;

    public M7rAuthorization(Authorization authorization) {
        this.authorization = authorization;
    }

    public Admin getUserAsAdmin() {
        if (!this.authorization.isInRole(Admin.ROLE_NAME))
            throw new IllegalStateException("User is not in role [" + Admin.ROLE_NAME + "].");
        return (Admin) this.authorization.getUser();
    }

    public Therapist getUserAsTherapist() {
        if (!this.authorization.isInRole(Therapist.ROLE_NAME))
            throw new IllegalStateException("User is not in role [" + Therapist.ROLE_NAME + "].");
        return (Therapist) this.authorization.getUser();
    }

    public PatientAbstract getUserAsPatientAbstract() {
        if (!isPatientAbstract())
            throw new IllegalStateException(
                    "User is not in role [" + PatientLogin.ROLE_NAME + "] or [" + PatientAnonymous.ROLE_NAME + "].");
        return (PatientAbstract) this.authorization.getUser();
    }

    public PatientLogin getUserAsPatientLogin() {
        if (!this.authorization.isInRole(PatientLogin.ROLE_NAME))
            throw new IllegalStateException("User is not in role [" + PatientLogin.ROLE_NAME + "].");
        return (PatientLogin) this.authorization.getUser();
    }

    public PatientAnonymous getUserAsPatientAnonymous() {
        if (!this.authorization.isInRole(PatientAnonymous.ROLE_NAME))
            throw new IllegalStateException("User is not in role [" + PatientAnonymous.ROLE_NAME + "].");
        return (PatientAnonymous) this.authorization.getUser();
    }

    public M7rUser getUserAsM7rUser() {
        return (M7rUser) this.authorization.getUser();
    }

    public boolean isPatientAbstract() {
        return (this.authorization.isInRole(PatientLogin.ROLE_NAME)
                || this.authorization.isInRole(PatientAnonymous.ROLE_NAME));
    }

}
