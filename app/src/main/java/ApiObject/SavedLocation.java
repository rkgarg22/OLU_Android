package ApiObject;

/**
 * Created by Ankit Chhabra on 9/5/2018.
 */

public class SavedLocation {

    String location;

    String latitude;

    String longitude;

    public SavedLocation(String location, String latitude, String longitude) {
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
