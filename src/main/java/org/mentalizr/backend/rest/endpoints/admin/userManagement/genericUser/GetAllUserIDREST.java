package org.mentalizr.backend.rest.endpoints.admin.userManagement.genericUser;

import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.Admin;
import org.mentalizr.backend.exceptions.M7rIllegalServiceInputException;
import org.mentalizr.backend.exceptions.M7rInfrastructureException;
import org.mentalizr.backend.exceptions.M7rUnknownEntityException;
import org.mentalizr.backend.rest.RESTException;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.contentManager.exceptions.ContentManagerException;
import org.mentalizr.persistence.mongo.DocumentNotFoundException;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageConverter;
import org.mentalizr.persistence.mongo.activityStatus.ActivityStatusMessageMongoHandler;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.manual.dao.UserLoginCompositeDAO;
import org.mentalizr.persistence.rdbms.barnacle.manual.vo.UserLoginCompositeVO;
import org.mentalizr.serviceObjects.userManagement.UserIDCollectionSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("v1")
public class GetAllUserIDREST {

    private static final String SERVICE_ID = "admin/user/getAll";

    @GET
    @Path(SERVICE_ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@Context HttpServletRequest httpServletRequest) {
        return new Service(httpServletRequest) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected Authorization checkSecurityConstraints() throws UnauthorizedException {
                return AccessControl.assertValidSession(Admin.ROLE_NAME, this.httpServletRequest);
            }

            @Override
            protected UserIDCollectionSO workLoad() throws RESTException, ContentManagerException, M7rInfrastructureException, IOException, DataSourceException, EntityNotFoundException, M7rIllegalServiceInputException, M7rUnknownEntityException, DocumentNotFoundException {
                UserIDCollectionSO finalCollection = new UserIDCollectionSO();
                List<UserLoginCompositeVO> userLoginCompositeVOs = UserLoginCompositeDAO.findAll();

                userLoginCompositeVOs.forEach(userLoginCompositeVO ->
                        finalCollection.getCollection().add(userLoginCompositeVO.getUserId()));

                return finalCollection;
            }

            @Override
            protected void updateActivityStatus() {

            }
        }.call();
    }
}
