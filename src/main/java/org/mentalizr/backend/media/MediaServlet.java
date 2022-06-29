package org.mentalizr.backend.media;

import de.arthurpicht.utils.core.strings.Strings;
import org.mentalizr.backend.applicationContext.ApplicationContext;
import org.mentalizr.backend.media.range.Range;
import org.mentalizr.backend.media.range.RangeParser;
import org.mentalizr.backend.media.range.RangeParserException;
import org.mentalizr.backend.media.range.Ranges;
import org.mentalizr.backend.utils.IOUtils;
import org.mentalizr.contentManager.ContentManager;
import org.mentalizr.contentManager.exceptions.ContentManagerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MediaServlet extends HttpServlet {

    public static final String MULTIPART_BOUNDARY = "fRWi7Qa9XoEZMGNtft0q";

    private static final Logger logger = LoggerFactory.getLogger(MediaServlet.class);

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        logger.debug("MediaServlet called by GET method.");
        processRequest(httpServletRequest, httpServletResponse, false);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        logger.debug("MediaServlet called by POST method.");
        httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "HTTP POST method not supported.");
    }

    @Override
    protected void doHead(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        logger.debug("MediaServlet called by HEAD method.");
        processRequest(httpServletRequest, httpServletResponse, true);
    }

    private void processRequest(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            boolean headOnly) throws IOException {

        try {
            String mediaName = getMediaName(httpServletRequest);
            logger.info("Requested media: [" + mediaName + "].");
            logRequestHeaders(httpServletRequest);

            Path mediaPath = getMediaPath(mediaName);

            Ranges ranges = getRanges(mediaPath, httpServletRequest);
            if (ranges.hasRange()) {
                logger.debug("Ranges requested. Number of ranges: " + ranges.getRanges().size());
            } else {
                logger.debug("No Ranges requested.");
            }

            setContentHeaders(httpServletResponse, mediaPath, ranges);

            setResponseStatus(httpServletResponse, ranges);

            logResponseHeaders(httpServletResponse);

            if (headOnly) return;

            writeContent(httpServletResponse, mediaPath, ranges);

        } catch (ProcessException e) {
            logger.info("Bad request. " + e.getStatusCode() + " " + e.getMessage());
            httpServletResponse.sendError(e.getStatusCode(), e.getMessage());
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

    private void logRequestHeaders(HttpServletRequest httpServletRequest) {
        List<String> headerNames = Collections.list(httpServletRequest.getHeaderNames());
        logger.debug("Request headers:");
        for (String headerName : headerNames) {
            logger.debug(headerName + ": " + httpServletRequest.getHeader(headerName));
        }
    }

    private void logResponseHeaders(HttpServletResponse httpServletResponse) {
        List<String> headerNames = new ArrayList<>(httpServletResponse.getHeaderNames());
        logger.debug("Response headers:");
        for (String headerName : headerNames) {
            logger.debug(headerName + ": " + httpServletResponse.getHeader(headerName));
        }
    }

    private Path getMediaPath(String mediaName) throws ProcessException {
        ContentManager contentManager = ApplicationContext.getContentManager();
        try {
            return contentManager.getMediaResource("sport", mediaName);
        } catch (ContentManagerException e) {
            throw new ProcessException(404, "Media not found: [" + mediaName + "].", e);
        }
    }

    private Ranges getRanges(Path mediaPath, HttpServletRequest httpServletRequest) throws BadRequestException {
        long fileSize;
        try {
            fileSize = Files.size(mediaPath);
        } catch (IOException e) {
            throw new BadRequestException("Internal error.", e);
        }
        try {
            return RangeParser.parse(httpServletRequest, fileSize);
        } catch (RangeParserException e) {
            throw new BadRequestException("Illegal http range header.", e);
        }
    }

    private void setContentHeaders(HttpServletResponse response, Path path, Ranges ranges) throws IOException {
        String contentType = getContentType(path);
        String totalSize = String.valueOf(Files.size(path));
        response.setHeader("Accept-Ranges", "bytes");

        if (!ranges.hasRange()) {
            response.setContentType(contentType);
            response.setHeader("Content-Length", totalSize);
        } else if (ranges.hasSingleRange()) {
            Range range = ranges.getRanges().get(0);
            response.setContentType(contentType);
            response.setHeader("Content-Length", String.valueOf(range.getLength()));
            response.setHeader("Content-Range", "bytes " + range.getBegin() + "-" + range.getEnd() + "/" + totalSize);
        } else if (ranges.hasMultiRange()) {
            response.setContentType("multipart/byteranges; boundary=" + MULTIPART_BOUNDARY);
        } else {
            throw new IllegalStateException();
        }
    }

    private String getContentType(Path path) {
        String fileName = path.getFileName().toString().toLowerCase();
        if (fileName.endsWith(".mp4")) {
            return "video/mp4";
        } else if (fileName.endsWith(".mp3")) {
            return "audio/mp3";
        } else if (fileName.endsWith(".pdf")) {
            return "application/pdf";
        } else if (fileName.endsWith(".txt")) {
            return "text/plain";
        } else {
            return "application/octet-stream";
        }
    }

    private void writeContent(HttpServletResponse response, Path path, Ranges ranges) throws IOException {
        ServletOutputStream output = response.getOutputStream();

        if (!ranges.hasRange()) {
            IOUtils.pump(new FileInputStream(path.toFile()), output);
        } else if (ranges.hasSingleRange()) {
            Range range = ranges.getRanges().get(0);
            IOUtils.pump(path.toFile(), output, range.getBegin(), range.getLength());
        } else if (ranges.hasMultiRange()){
            for (Range range : ranges.getRanges()) {
                output.println();
                output.println("--" + MULTIPART_BOUNDARY);
                output.println("Content-Type: " + getContentType(path));
                output.println("Content-Range: bytes " + range.getBegin() + "-" + range.getEnd() + "/" + Files.size(path));
                IOUtils.pump(path.toFile(), output, range.getBegin(), range.getLength());
            }
            output.println();
            output.println("--" + MULTIPART_BOUNDARY + "--");
        } else {
            throw new IllegalStateException();
        }
    }

    private void setResponseStatus(HttpServletResponse response, Ranges ranges) {
        logger.debug("ResponseStatus 206 is set.");
        if (ranges.hasRange()) response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
    }

}
