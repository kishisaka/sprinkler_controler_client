package us.ttyl.sprinklercontroller;

/**
 * Created by test on 12/18/2016.
 */

public class CurrentTime {
    String time;
    String weather_condition;
    String weather_temp;
    String zipcode;
    long weather_updated_on;

    public long getWeather_updated_on() {
        return weather_updated_on;
    }

    public void setWeathter_updated_on(long weahter_updated_on) {
        this.weather_updated_on = weahter_updated_on;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeather_condition() {
        return weather_condition;
    }

    public void setWeather_condition(String weather_condition) {
        this.weather_condition = weather_condition;
    }

    public String getWeather_temp() {
        return weather_temp;
    }

    public void setWeather_temp(String weather_temp) {
        this.weather_temp = weather_temp;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
