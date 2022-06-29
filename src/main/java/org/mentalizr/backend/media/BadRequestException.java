package org.mentalizr.backend.media;

import javax.servlet.http.HttpServletResponse;

public class BadRequestException extends ProcessException {

    public BadRequestException(String message) {
        super(HttpServletResponse.SC_BAD_REQUEST, message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(HttpServletResponse.SC_BAD_REQUEST, message, cause);
    }

}
