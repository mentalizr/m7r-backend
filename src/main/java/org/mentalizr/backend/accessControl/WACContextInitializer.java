package org.mentalizr.backend.accessControl;

import de.arthurpicht.webAccessControl.*;
import de.arthurpicht.webAccessControl.handler.LoginHandlerAbstractFactory;
import de.arthurpicht.webAccessControl.handler.RoleRegistry;
import de.arthurpicht.webAccessControl.sessionManager.SessionManagerAbstractFactory;
import de.arthurpicht.webAccessControl.sessionManager.SessionManagerFactoryHttp;
import org.mentalizr.backend.M7rLoginHandlerAbstractFactory;
import org.mentalizr.backend.accessControl.roles.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WACContextInitializer {

    public static void init() {
        Logger logger = LoggerFactory.getLogger("auth");

        RoleRegistry roleRegistry = new RoleRegistry.Builder()
                .add(Admin.ROLE_NAME, Admin.class)
                .add(PatientAnonymous.ROLE_NAME, PatientAnonymous.class)
                .add(PatientLogin.ROLE_NAME, PatientLogin.class)
                .add(Therapist.ROLE_NAME, Therapist.class)
                .build();

        LoginHandlerAbstractFactory loginHandlerAbstractFactory
                = new M7rLoginHandlerAbstractFactory();

        SessionManagerAbstractFactory sessionManagerAbstractFactory
                = new SessionManagerFactoryHttp();

        WACContext WACContext
                = new WACContext(logger, roleRegistry, loginHandlerAbstractFactory, sessionManagerAbstractFactory);

        WACContextRegistry.initialize(WACContext);

    }

}
