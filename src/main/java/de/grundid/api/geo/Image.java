package de.grundid.api.geo;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Image {

    private String url;
    private String source;

    public Image(String url) {
        this.url = url;
    }

    public Image(String url, String source) {
        this.url = url;
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
