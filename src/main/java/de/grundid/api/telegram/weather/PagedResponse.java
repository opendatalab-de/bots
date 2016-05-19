package de.grundid.api.telegram.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by lukas on 19.05.16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PagedResponse {

    private List<SensorValue> content;

    public List<SensorValue> getContent() {
        return content;
    }

    public void setContent(List<SensorValue> content) {
        this.content = content;
    }
}
