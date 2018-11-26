package ApiObject;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ankit Chhabra on 6/3/2018.
 */

public class TodayBookingObject {

    @SerializedName("bookingDate")
    String date;

    @SerializedName("category")
    String category;

    @SerializedName("firstName")
    String firstName;

    @SerializedName("lastName")
    String lastName;

    @SerializedName("bookingStart")
    String bookingStart;

    @SerializedName("bookingEnd")
    String bookingEnd;

    @SerializedName("bookingID")
    String bookingId;

    @SerializedName("categoryID")
    String categoryID;

    @SerializedName("phone")
    String phoneNumber;

    @SerializedName("userID")
    String userID;

    @SerializedName("userImageUrl")
    String userImageUrl;

    @SerializedName("bookingLatitude")
    String bookingLatitude;

    @SerializedName("bookingLongitude")
    String bookingLongitude;

    @SerializedName("bookingAddress")
    String bookingAddress;

    @SerializedName("bookingCreated")
    String bookingCreated;

    @SerializedName("bookingAccepted")
    String bookingAccepted;

    @SerializedName("status")
    String status;

    @SerializedName("isAgenda")
    int isAgenda;

    @SerializedName("agendaID")
    String agendaID;

    @SerializedName("agendaText")
    String agendaText;

    @SerializedName("agendaType")
    String agendaType;

    @SerializedName("bookingFor")
    String bookingFor;

    public String getBookingFor() {
        return bookingFor;
    }

    public void setBookingFor(String bookingFor) {
        this.bookingFor = bookingFor;
    }

    public String getBookingCreated() {
        return bookingCreated;
    }

    public void setBookingCreated(String bookingCreated) {
        this.bookingCreated = bookingCreated;
    }

    public String getBookingAccepted() {
        return bookingAccepted;
    }

    public void setBookingAccepted(String bookingAccepted) {
        this.bookingAccepted = bookingAccepted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getBookingStart() {
        return bookingStart;
    }

    public void setBookingStart(String bookingStart) {
        this.bookingStart = bookingStart;
    }

    public String getBookingEnd() {
        return bookingEnd;
    }

    public void setBookingEnd(String bookingEnd) {
        this.bookingEnd = bookingEnd;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public TodayBookingObject(String date, String category, String firstName, String lastName,
                              String bookingStart, String bookingEnd,
                              String bookingId, String categoryID, String latitude, String longitude, String address) {
        this.date = date;
        this.category = category;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bookingStart = bookingStart;
        this.bookingEnd = bookingEnd;
        this.bookingId = bookingId;
        this.categoryID = categoryID;
        this.bookingLatitude = latitude;
        this.bookingLongitude = longitude;
        this.bookingAddress = address;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getBookingLatitude() {
        return bookingLatitude;
    }

    public void setBookingLatitude(String bookingLatitude) {
        this.bookingLatitude = bookingLatitude;
    }

    public String getBookingLongitude() {
        return bookingLongitude;
    }

    public void setBookingLongitude(String bookingLongitude) {
        this.bookingLongitude = bookingLongitude;
    }

    public String getBookingAddress() {
        return bookingAddress;
    }

    public void setBookingAddress(String bookingAddress) {
        this.bookingAddress = bookingAddress;
    }

    public int getIsAgenda() {
        return isAgenda;
    }

    public void setIsAgenda(int isAgenda) {
        this.isAgenda = isAgenda;
    }

    public String getAgendaID() {
        return agendaID;
    }

    public void setAgendaID(String agendaID) {
        this.agendaID = agendaID;
    }

    public String getAgendaText() {
        return agendaText;
    }

    public void setAgendaText(String agendaText) {
        this.agendaText = agendaText;
    }

    public String getAgendaType() {
        return agendaType;
    }

    public void setAgendaType(String agendaType) {
        this.agendaType = agendaType;
    }
}
