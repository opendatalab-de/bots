package de.grundid.api.telegram.drinker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PagedResponse {

    private List<DrinkWithLocation> content;

    public List<DrinkWithLocation> getContent() {
        return content;
    }

    public void setContent(List<DrinkWithLocation> content) {
        this.content = content;
    }
}
