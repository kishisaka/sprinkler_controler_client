package us.ttyl.sprinklercontroller;

import java.util.List;

/**
 * Created by test on 12/15/2016.
 */

public class SprinklerData {
    String weatherundergroundAPIKey;
    String zipcode;
    String timezone;
    List<Time> times;

    public String getWeatherundergroundAPIKey() {
        return weatherundergroundAPIKey;
    }

    public void setWeatherundergroundAPIKey(String weatherundergroundAPIKey) {
        this.weatherundergroundAPIKey = weatherundergroundAPIKey;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public List<Time> getTimes() {
        return times;
    }

    public void setTimes(List<Time> times) {
        this.times = times;
    }
}
