package org.mentalizr.backend.security.session.attributes.staging;

import java.io.Serializable;

public abstract class StagingAttribute implements Serializable {

    public static final String STAGING = "STAGING";

    public boolean isValid() {
        return this instanceof StagingValid;
    }

    public boolean isIntermediate() {
        return this instanceof StagingIntermediate;
    }

}
