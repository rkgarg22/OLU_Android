package ApiObject;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ankit Chhabra on 7/18/2018.
 */

public class MyProfileObject {

    @SerializedName("userID")
    String userID;

    @SerializedName("firstName")
    String firstName;

    @SerializedName("lastName")
    String lastName;

    @SerializedName("image")
    String image;

    @SerializedName("categories")
    List<CategoriesObject> categoriesObjectList;

    @SerializedName("wallet")
    String wallet;

    @SerializedName("bookingHistory")
    List<TodayBookingObject> todayBookingObjectList;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<CategoriesObject> getCategoriesObjectList() {
        return categoriesObjectList;
    }

    public void setCategoriesObjectList(List<CategoriesObject> categoriesObjectList) {
        this.categoriesObjectList = categoriesObjectList;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public List<TodayBookingObject> getTodayBookingObjectList() {
        return todayBookingObjectList;
    }

    public void setTodayBookingObjectList(List<TodayBookingObject> todayBookingObjectList) {
        this.todayBookingObjectList = todayBookingObjectList;
    }
}
