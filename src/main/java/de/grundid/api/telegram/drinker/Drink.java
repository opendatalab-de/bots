package de.grundid.api.telegram.drinker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Drink {

    private String id;
    private String name;
    private String brand;
    private String category;
    private String description;
    private List<VolumePrice> volumePrices;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<VolumePrice> getVolumePrices() {
        return volumePrices;
    }

    public void setVolumePrices(List<VolumePrice> volumePrices) {
        this.volumePrices = volumePrices;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
