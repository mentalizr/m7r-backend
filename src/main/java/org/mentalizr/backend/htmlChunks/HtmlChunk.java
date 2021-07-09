package org.mentalizr.backend.htmlChunks;

public enum HtmlChunk {

    INIT("/WEB-INF/init.html"),
    LOGIN("/WEB-INF/login.chunk.html"),
    LOGIN_VOUCHER("WEB-INF/loginVoucher.chunk.html"),
    PATIENT("/WEB-INF/patient.chunk.html"),
    THERAPIST("/WEB-INF/therapist.chunk.html");

    private final String fileName;

    HtmlChunk(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public static HtmlChunk getHtmlChunkByName(String htmlChunkName) {

        HtmlChunk htmlChunk;
        try {
            htmlChunk = HtmlChunk.valueOf(htmlChunkName);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("No HtmlChunk found for name: " + htmlChunkName);
        }

        return htmlChunk;
    }

}
