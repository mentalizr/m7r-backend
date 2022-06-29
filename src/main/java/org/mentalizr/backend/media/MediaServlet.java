package org.mentalizr.backend.media;

import de.arthurpicht.utils.core.strings.Strings;
import org.mentalizr.backend.media.exception.BadRequestException;
import org.mentalizr.backend.media.exception.ProcessException;
import org.mentalizr.backend.media.range.Range;
import org.mentalizr.backend.media.range.RangeParser;
import org.mentalizr.backend.media.range.RangeParserException;
import org.mentalizr.backend.media.range.Ranges;
import org.mentalizr.backend.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class MediaServlet extends HttpServlet {

    // Possible extensions:
    // * If-Match, If-Unmodified-Since, ETag, Last-Modified, If-Range headers

    public static final String MULTIPART_BOUNDARY = "fRWi7Qa9XoEZMGNtft0q";

    private static final Logger logger = LoggerFactory.getLogger(MediaServlet.class);

    public abstract Path getMediaPath(HttpServletRequest httpServletRequest) throws ProcessException;

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
            Path mediaPath = getMediaPath(httpServletRequest);

            logger.info("Requested media: [" + mediaPath.getFileName().toString() + "].");
            logRequestHeaders(httpServletRequest);

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
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String mimeType = fileNameMap.getContentTypeFor(fileName);
        if (Strings.isNullOrEmpty(mimeType)) mimeType = "application/octet-stream";
        return mimeType;
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
