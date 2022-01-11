package org.mentalizr.backend.htmlChunks;

public class HtmlChunkModifierLogin extends HtmlChunkModifier {

    public HtmlChunkModifierLogin(String chunk) {
        super(chunk);
    }

    public void addLogo(String logo) {
        this.chunk = this.chunk.replace(
                "<img class=\"img-fluid\" src=\"../resrc/img/LOGO.png\">",
                "img class=\"img-fluid\" src=\"../resrc/img/" + logo + "\">"
        );
    }

}
