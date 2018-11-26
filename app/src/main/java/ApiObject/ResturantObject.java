package ApiObject;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class ResturantObject {
    @SerializedName("restID")
    String restID;

    @SerializedName("restName")
    String restName;

    @SerializedName("typeOfFood")
    ArrayList<String> typeOfFood;

    @SerializedName("description")
    String description;

    @SerializedName("isHomeDeliveryAvailable")
    String isHomeDeliveryAvailable;

    @SerializedName("paymentMethod")
    String paymentMethod;

    @SerializedName("phoneNumber")
    String phoneNumber;

    @SerializedName("address")
    String address;

    @SerializedName("category")
    String category;

    @SerializedName("images")
    String images;

    @SerializedName("isFavourite")
    String isFavourite;

    @SerializedName("likesCount")
    String likesCount;

    @SerializedName("other")
    ArrayList<String> other;

    @SerializedName("menu")
    String menu;

    @SerializedName("lattitude")
    String lattitude;

    @SerializedName("longitude")
    String longitude;

    @SerializedName("isActive")
    String isActive;

    @SerializedName("distance")
    String distance;

    @SerializedName("histroy")
    String histroy;

    @SerializedName("priceFrom")
    String priceFrom;

    @SerializedName("priceTo")
    String priceTo;

    @SerializedName("registered_date")
    String registered_date;

    @SerializedName("favCount")
    String favCount;

    @SerializedName("isLiked")
    String isLiked;

    @SerializedName("color")
    String color;

    @SerializedName("web_url")
    String webUrl;

    @SerializedName("instagram_account")
    String instagramAccount;

    @SerializedName("operatingHours")
    ArrayList<OperatingHour> operatingHourArrayList;

    @SerializedName("servicePhone")
    String servicePhone;

    @SerializedName("serviceStatus")
    String serviceStatus;

    public ResturantObject() {
    }

    public String getRestID() {
        return restID;
    }

    public void setRestID(String restID) {
        this.restID = restID;
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public ArrayList<String> getTypeOfFood() {
        return typeOfFood;
    }

    public void setTypeOfFood(ArrayList<String> typeOfFood) {
        this.typeOfFood = typeOfFood;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsHomeDeliveryAvailable() {
        return isHomeDeliveryAvailable;
    }

    public void setIsHomeDeliveryAvailable(String isHomeDeliveryAvailable) {
        this.isHomeDeliveryAvailable = isHomeDeliveryAvailable;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(String isFavourite) {
        this.isFavourite = isFavourite;
    }

    public String getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(String likesCount) {
        this.likesCount = likesCount;
    }

    public ArrayList<String> getOther() {
        return other;
    }

    public void setOther(ArrayList<String> other) {
        this.other = other;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getHistroy() {
        return histroy;
    }

    public void setHistroy(String histroy) {
        this.histroy = histroy;
    }

    public String getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(String priceFrom) {
        this.priceFrom = priceFrom;
    }

    public String getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(String priceTo) {
        this.priceTo = priceTo;
    }

    public String getRegistered_date() {
        return registered_date;
    }

    public void setRegistered_date(String registered_date) {
        this.registered_date = registered_date;
    }

    public String getFavCount() {
        return favCount;
    }

    public void setFavCount(String favCount) {
        this.favCount = favCount;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getInstagramAccount() {
        return instagramAccount;
    }

    public void setInstagramAccount(String instagramAccount) {
        this.instagramAccount = instagramAccount;
    }

    public ArrayList<OperatingHour> getOperatingHourArrayList() {
        return operatingHourArrayList;
    }

    public void setOperatingHourArrayList(ArrayList<OperatingHour> operatingHourArrayList) {
        this.operatingHourArrayList = operatingHourArrayList;
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }
}
