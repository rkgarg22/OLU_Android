package ApiObject;

import com.google.gson.annotations.SerializedName;


public class OperatingHour {
    @SerializedName("day")
    String day;

    @SerializedName("time")
    String time;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
