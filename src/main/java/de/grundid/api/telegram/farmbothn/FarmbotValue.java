package de.grundid.api.telegram.farmbothn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FarmbotValue {
    private String percent, time, temperature, humidity;

    public String getPercent() {
        return percent;
    }


    public String getTime() {
        return time;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getHumidity() {
        return humidity;
    }
}
