package de.grundid.api.geo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.geojson.Point;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GeoObject implements Serializable {

    private String id;
    private String name;
    private Set<String> categories;
    @JsonIgnore
    private Point position;
    private Address address;
    private String openingHoursText;
    private Map<String, List<TimeInterval>> openingHours;
    private Contacts contacts;
    private Long fromDate;
    private Long toDate;
    private String description;
    private Image image;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Contacts getContacts() {
        return contacts;
    }

    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getOpeningHoursText() {
        return openingHoursText;
    }

    public void setOpeningHoursText(String openingHoursText) {
        this.openingHoursText = openingHoursText;
    }

    public Map<String, List<TimeInterval>> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(
            Map<String, List<TimeInterval>> openingHours) {
        this.openingHours = openingHours;
    }

    public Long getFromDate() {
        return fromDate;
    }

    public void setFromDate(Long fromDate) {
        this.fromDate = fromDate;
    }

    public Long getToDate() {
        return toDate;
    }

    public void setToDate(Long toDate) {
        this.toDate = toDate;
    }
}
