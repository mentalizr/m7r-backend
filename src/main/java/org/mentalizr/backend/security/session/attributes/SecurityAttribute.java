package org.mentalizr.backend.security.session.attributes;

import org.mentalizr.backend.security.session.attributes.staging.StagingAttribute;
import org.mentalizr.backend.security.session.attributes.user.UserHttpSessionAttribute;

import static de.arthurpicht.utils.core.assertion.AssertMethodPrecondition.parameterNotNull;

public class SecurityAttribute {

    public static final String NAME = "security";

    private final UserHttpSessionAttribute userHttpSessionAttribute;
    private final StagingAttribute stagingAttribute;

    public SecurityAttribute(
            StagingAttribute stagingAttribute,
            UserHttpSessionAttribute userHttpSessionAttribute
    ) {
        parameterNotNull("stagingAttribute", stagingAttribute);
        parameterNotNull("userHttpSessionAttribute", userHttpSessionAttribute);
        this.stagingAttribute = stagingAttribute;
        this.userHttpSessionAttribute = userHttpSessionAttribute;
    }

    public UserHttpSessionAttribute getUserHttpSessionAttribute() {
        return this.userHttpSessionAttribute;
    }

    public StagingAttribute getStagingAttribute() {
        return this.stagingAttribute;
    }

    public boolean isValid() {
        return this.stagingAttribute.isValid();
    }

    public boolean isIntermediate() {
        return this.stagingAttribute.isIntermediate();
    }

}
