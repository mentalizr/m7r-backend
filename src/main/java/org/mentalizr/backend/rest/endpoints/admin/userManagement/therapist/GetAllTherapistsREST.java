package org.mentalizr.backend.rest.endpoints.admin.userManagement.therapist;

import org.mentalizr.backend.adapter.TherapistRestoreSOAdapter;
import org.mentalizr.backend.security.auth.AuthorizationService;
import org.mentalizr.backend.security.auth.UnauthorizedException;
import org.mentalizr.backend.security.session.attributes.user.UserHttpSessionAttribute;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.DataSourceException;
import org.mentalizr.persistence.rdbms.barnacle.connectionManager.EntityNotFoundException;
import org.mentalizr.persistence.rdbms.barnacle.dao.RoleTherapistDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserDAO;
import org.mentalizr.persistence.rdbms.barnacle.dao.UserLoginDAO;
import org.mentalizr.persistence.rdbms.barnacle.vo.RoleTherapistVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserLoginVO;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;
import org.mentalizr.serviceObjects.userManagement.TherapistRestoreCollectionSO;
import org.mentalizr.serviceObjects.userManagement.TherapistRestoreSO;

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
public class GetAllTherapistsREST {

    private static final String SERVICE_ID = "admin/user/therapist/getAll";

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
            protected UserHttpSessionAttribute checkSecurityConstraints() throws UnauthorizedException {
                return AuthorizationService.assertIsLoggedInAsAdmin(httpServletRequest);
            }

            @Override
            protected TherapistRestoreCollectionSO workLoad() throws DataSourceException, EntityNotFoundException {
                List<RoleTherapistVO> roleTherapistVOList = RoleTherapistDAO.findAll();
                List<TherapistRestoreSO> collection = new ArrayList<>();

                for (RoleTherapistVO roleTherapistVO : roleTherapistVOList) {
                    TherapistRestoreSO therapistRestoreSO = createTherapistRestoreSO(roleTherapistVO);
                    collection.add(therapistRestoreSO);
                }

                TherapistRestoreCollectionSO therapistRestoreCollectionSO = new TherapistRestoreCollectionSO();
                therapistRestoreCollectionSO.setCollection(collection);

                return therapistRestoreCollectionSO;
            }

            private TherapistRestoreSO createTherapistRestoreSO(RoleTherapistVO roleTherapistVO)
                    throws DataSourceException, EntityNotFoundException {

                UserVO userVO = UserDAO.load(roleTherapistVO.getUserId());
                UserLoginVO userLoginVO = UserLoginDAO.load(roleTherapistVO.getUserId());

                return TherapistRestoreSOAdapter.from(userVO, userLoginVO, roleTherapistVO);
            }

        }.call();

    }

}
