package org.mentalizr.backend.security.session.attributes.staging;

public abstract class StagingAttribute {

    public static final String ATTRIBUTE_NAME = "STAGING";

    public boolean isValid() {
        return this instanceof StagingValid;
    }

    public boolean isIntermediate() {
        return this instanceof StagingIntermediate;
    }

}
