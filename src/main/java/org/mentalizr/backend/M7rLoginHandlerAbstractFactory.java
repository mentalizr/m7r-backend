package org.mentalizr.backend;

import de.arthurpicht.webAccessControl.handler.LoginHandler;
import de.arthurpicht.webAccessControl.handler.LoginHandlerAbstractFactory;
import org.mentalizr.backend.accessControl.M7rLoginHandler;

public class M7rLoginHandlerAbstractFactory extends LoginHandlerAbstractFactory {

    @Override
    public LoginHandler create() {
        return new M7rLoginHandler();
    }

}
