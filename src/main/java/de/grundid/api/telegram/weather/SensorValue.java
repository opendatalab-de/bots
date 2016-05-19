package de.grundid.api.telegram.weather;

/**
 * Created by lukas on 19.05.16.
 */
public class SensorValue {

    private String sensorName;
    private double value;
    private long date;

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
