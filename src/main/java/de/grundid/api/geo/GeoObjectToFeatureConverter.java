package de.grundid.api.geo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.Feature;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GeoObjectToFeatureConverter implements Converter<GeoObject, Feature> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override public Feature convert(GeoObject source) {
        Feature feature = new Feature();
        feature.setGeometry(source.getPosition());
        Map<String, Object> props = objectMapper.convertValue(source, Map.class);
        feature.setProperties(props);
        return feature;
    }
}
