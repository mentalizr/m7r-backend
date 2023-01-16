package org.mentalizr.backend.rest.endpoints.admin.userManagement.policy;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.dao.PolicyDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.PolicyVO;
import org.mentalizr.serviceObjects.userManagement.PolicyCollectionSO;
import org.mentalizr.serviceObjects.userManagement.PolicySO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("v1")
public class GetAllPolicyREST {

    private static final String SERVICE_ID = "/admin/user/policy/getAll";

    @GET
    @Path(SERVICE_ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(
            @Context HttpServletRequest httpServletRequest
    ) {

        return new Service(httpServletRequest) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected Authorization checkSecurityConstraints() throws UnauthorizedException {
                return AccessControl.assertValidSession(Admin.ROLE_NAME, httpServletRequest);
            }

            @Override
            protected PolicyCollectionSO workLoad() throws DataSourceException {
                List<PolicyVO> policyVOList = PolicyDAO.findAll();

                List<PolicySO> collection = new ArrayList<>();
                for (PolicyVO policyVO : policyVOList) {
                    PolicySO policySO = new PolicySO();
                    policySO.setUserId(policyVO.getUserId());
                    policySO.setVersion(policyVO.getVersion());
                    policySO.setConsent(policyVO.getConsent());

                    collection.add(policySO);
                }

                PolicyCollectionSO policyCollectionSO = new PolicyCollectionSO();
                policyCollectionSO.setCollection(collection);

                return policyCollectionSO;
            }

        }.call();

    }

}
