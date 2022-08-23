package org.mentalizr.backend.security.auth;

import org.mentalizr.backend.security.session.attributes.staging.StagingIntermediate;

import javax.servlet.http.HttpServletRequest;

public class IntermediateAuthentication extends AbstractAuthentication {

    public IntermediateAuthentication(HttpServletRequest httpServletRequest) throws UnauthorizedException {

        super(httpServletRequest);

        if (this.stagingAttribute.isValid())
            throw new UnauthorizedException("[Authentication] failed: Session is in state [valid] but is expected to be in state [intermediate].");
    }

    public String getNextRequirement() {
        StagingIntermediate stagingIntermediate = (StagingIntermediate) this.stagingAttribute;
        return stagingIntermediate.getNextRequirementAsString();
    }

}
