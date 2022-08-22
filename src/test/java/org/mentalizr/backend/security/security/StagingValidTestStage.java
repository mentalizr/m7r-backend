package org.mentalizr.backend.security.security;

import org.junit.jupiter.api.Test;
import org.mentalizr.backend.security.session.attributes.staging.StagingAttribute;
import org.mentalizr.backend.security.session.attributes.staging.StagingValid;

import static org.junit.jupiter.api.Assertions.*;

class StagingValidTestStage {

    @Test
    public void simpleTest() {
        StagingAttribute validSession = new StagingValid();
        assertTrue(validSession.isValid());
        assertFalse(validSession.isIntermediate());
    }


}