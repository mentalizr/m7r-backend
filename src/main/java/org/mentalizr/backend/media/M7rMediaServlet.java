package org.mentalizr.backend.media;

import de.arthurpicht.utils.core.strings.Strings;
import de.arthurpicht.webAccessControl.auth.Authorization;
import de.arthurpicht.webAccessControl.auth.UnauthorizedException;
import org.mentalizr.backend.accessControl.M7rAccessControl;
import org.mentalizr.backend.accessControl.M7rAuthorization;
import org.mentalizr.backend.accessControl.roles.PatientAbstract;
import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.media.exception.BadRequestException;
import org.mentalizr.backend.media.exception.IllegalMediaSpecificationException;
import org.mentalizr.backend.media.exception.ProcessException;
import org.mentalizr.contentManager.ContentManager;
import org.mentalizr.contentManager.exceptions.ContentManagerException;
import org.mentalizr.persistence.rdbms.barnacle.vo.PatientProgramVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serial;
import java.nio.file.Path;

public class M7rMediaServlet extends MediaServlet {

    @Serial
    private static final long serialVersionUID = 8738566128735601223L;

    @Override
    public Path getMediaPath(HttpServletRequest httpServletRequest) throws ProcessException {
        Authorization authorization;
        try {
            authorization = M7rAccessControl.assertValidSessionAsPatientAbstract(httpServletRequest);
        } catch (UnauthorizedException e) {
            throw new ProcessException(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access.");
        }

        PatientAbstract patientAbstract = new M7rAuthorization(authorization).getUserAsPatientAbstract();
        PatientProgramVO patientProgramVO = patientAbstract.getPatientProgramVO();
        String programId = patientProgramVO.getProgramId();

        String mediaName = getMediaName(httpServletRequest);
        ContentManager contentManager = ApplicationContext.getContentManager();
        try {
            return contentManager.getMediaResource(programId, mediaName);
        } catch (ContentManagerException e) {
            throw new ProcessException(404, "Media not found: [" + mediaName + "].", e);
        }
    }

    private String getMediaName(HttpServletRequest request) throws BadRequestException {
        String pathInfo = request.getPathInfo();

        if (Strings.isNullOrEmpty(pathInfo)) throw new IllegalMediaSpecificationException();

        if (pathInfo.startsWith("/")) {
            if (pathInfo.length() > 1) {
                pathInfo = pathInfo.substring(1);
            } else {
                throw new IllegalMediaSpecificationException();
            }
        }

        if (pathInfo.contains("/")) throw new IllegalMediaSpecificationException();

        return pathInfo;
    }

}
