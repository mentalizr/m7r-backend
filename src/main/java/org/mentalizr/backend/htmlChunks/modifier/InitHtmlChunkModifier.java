package org.mentalizr.backend.htmlChunks.modifier;

public class InitHtmlChunkModifier extends HtmlChunkModifier{

    public void addEntry(String chunkName) {
        this.chunk = this.chunk.replace(
                "<meta name=\"entry\" content=\"\">",
                "<meta name=\"entry\" content=\"" + chunkName + "\">"
        );
    }

    public void addTitle(String title) {
        this.chunk = this.chunk.replace(
                "<title></title>",
                "<title>" + title + "</title>"
        );

    }

}
