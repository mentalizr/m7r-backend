package org.mentalizr.backend.rest.endpoints.patient.formData;

import de.arthurpicht.utils.core.collection.Sets;
import de.arthurpicht.webAccessControl.auth.AccessControl;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.roles.PatientAbstract;
import org.mentalizr.backend.accessControl.roles.PatientAnonymous;
import org.mentalizr.backend.accessControl.roles.PatientLogin;
import org.mentalizr.backend.rest.service.Service;
import org.mentalizr.persistence.mongo.formData.FormDataDAO;
import org.mentalizr.persistence.mongo.formData.FormDataTimestampUpdater;
import org.mentalizr.persistence.rdbms.barnacle.vo.UserVO;
import org.mentalizr.serviceObjects.frontend.patient.formData.FormDataSO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1")
public class GetFormDataREST {

    private static final String SERVICE_ID = "patient/formData";

    @GET
    @Path(SERVICE_ID + "/{contentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFormData(
            @PathParam("contentId") String contentId,
            @Context HttpServletRequest httpServletRequest) {

        return new Service(httpServletRequest) {

            @Override
            protected String getServiceId() {
                return SERVICE_ID;
            }

            @Override
            protected Authorization checkSecurityConstraints() throws UnauthorizedException {
                return AccessControl.assertValidSession(
                        Sets.newHashSet(PatientAnonymous.ROLE_NAME, PatientLogin.ROLE_NAME),
                        httpServletRequest);
            }

            @Override
            protected FormDataSO workLoad() {
                PatientAbstract patientAbstract = (PatientAbstract) this.authorization.getUser();
                UserVO userVO = patientAbstract.getUserVO();

                FormDataSO formDataSO = FormDataDAO.obtain(userVO.getId(), contentId);
                FormDataTimestampUpdater.markFeedbackAsSeenByPatient(formDataSO);

                return formDataSO;
            }

            @Override
            protected void logLeave() {
                String userId = this.authorization.getUserId();
                logger.debug("[" + SERVICE_ID + "][" + userId + "][" + contentId + "] completed.");
            }

        }.call();

    }

}
