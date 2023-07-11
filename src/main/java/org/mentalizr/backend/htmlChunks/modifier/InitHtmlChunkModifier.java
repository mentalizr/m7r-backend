package org.mentalizr.backend.htmlChunks.modifier;

import org.mentalizr.serviceObjects.frontend.application.ApplicationConfigGenericSO;

public abstract class InitHtmlChunkModifier implements HtmlChunkModifier {

    @Override
    public String modify(String htmlChunkString, ApplicationConfigGenericSO applicationConfigGenericSO) {
        htmlChunkString = addTitle(htmlChunkString, applicationConfigGenericSO.getTitle());
        return htmlChunkString;
    }

    protected String addEntry(String htmlChunkString, String entryChunkName) {
        return htmlChunkString.replace(
                "<meta name=\"entry\" content=\"\">",
                "<meta name=\"entry\" content=\"" + entryChunkName + "\">"
        );
    }

    private String addTitle(String htmlChunkString, String title) {
        return htmlChunkString.replace(
                "<title></title>",
                "<title>" + title + "</title>"
        );
    }

}
