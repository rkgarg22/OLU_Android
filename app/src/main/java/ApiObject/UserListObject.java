package ApiObject;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UserListObject {

    @SerializedName("userID")
    private int userID;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("category")
    private ArrayList<CategoriesObject> categoriesObjectArrayList;

    @SerializedName("categoryName")
    private String categoryName;

    @SerializedName("categoryID")
    private String categoryID;

    @SerializedName("userImageUrl")
    private String userImageUrl;

    @SerializedName("price")
    private String price;

    @SerializedName("reviews")
    private String reviews;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("singlePrice")
    private String singlePrice;

    @SerializedName("groupPrice2")
    private String groupPrice2;

    @SerializedName("groupPrice3")
    private String groupPrice3;

    @SerializedName("groupPrice4")
    private String groupPrice4;

    @SerializedName("companyPrice")
    private String companyPrice;

    @SerializedName("description")
    private String description;

    @SerializedName("isCurrentlyBooked")
    private String isCurrentlyBooked;

    @SerializedName("bookingDetails")
    private List<TodayBookingObject> bookingDetailObjectList;

    public List<TodayBookingObject> getBookingDetailObjectList() {
        return bookingDetailObjectList;
    }

    public void setBookingDetailObjectList(List<TodayBookingObject> bookingDetailObjectList) {
        this.bookingDetailObjectList = bookingDetailObjectList;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(String singlePrice) {
        this.singlePrice = singlePrice;
    }

    public String getGroupPrice2() {
        return groupPrice2;
    }

    public void setGroupPrice2(String groupPrice2) {
        this.groupPrice2 = groupPrice2;
    }

    public String getGroupPrice3() {
        return groupPrice3;
    }

    public void setGroupPrice3(String groupPrice3) {
        this.groupPrice3 = groupPrice3;
    }

    public String getGroupPrice4() {
        return groupPrice4;
    }

    public void setGroupPrice4(String groupPrice4) {
        this.groupPrice4 = groupPrice4;
    }

    public String getCompanyPrice() {
        return companyPrice;
    }

    public void setCompanyPrice(String companyPrice) {
        this.companyPrice = companyPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsCurrentlyBooked() {
        return isCurrentlyBooked;
    }

    public void setIsCurrentlyBooked(String isCurrentlyBooked) {
        this.isCurrentlyBooked = isCurrentlyBooked;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
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

    public ArrayList<CategoriesObject> getCategoriesObjectArrayList() {
        return categoriesObjectArrayList;
    }

    public void setCategoriesObjectArrayList(ArrayList<CategoriesObject> categoriesObjectArrayList) {
        this.categoriesObjectArrayList = categoriesObjectArrayList;
    }
}
