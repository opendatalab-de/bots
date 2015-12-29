package org.telegram.api.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.json.JSONObject;
import org.telegram.api.interfaces.BotApiObject;

import java.io.IOException;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * @brief This object represents a point on the map.
 * @date 20 of June of 2015
 */
public class Location implements BotApiObject {

    public static final String LONGITUDE_FIELD = "longitude";
    @JsonProperty(LONGITUDE_FIELD)
    private Double longitude; ///< Longitude as defined by sender
    public static final String LATITUDE_FIELD = "latitude";
    @JsonProperty(LATITUDE_FIELD)
    private Double latitude; ///< Latitude as defined by sender

    public Location() {
        super();
    }

    public Location(JSONObject jsonObject) {
        super();
        this.longitude = jsonObject.getDouble(LONGITUDE_FIELD);
        this.latitude = jsonObject.getDouble(LATITUDE_FIELD);
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField(LONGITUDE_FIELD, longitude);
        gen.writeNumberField(LATITUDE_FIELD, latitude);
        gen.writeEndObject();
        gen.flush();
    }

    @Override
    public void serializeWithType(JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
            throws IOException {
        gen.writeStartObject();
        gen.writeNumberField(LONGITUDE_FIELD, longitude);
        gen.writeNumberField(LATITUDE_FIELD, latitude);
        gen.writeEndObject();
        gen.flush();
    }
}
