package ApiObject;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit Chhabra on 6/3/2018.
 */

public class PaymentHistoryObject {

    @SerializedName("date")
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

    @SerializedName("isPaid")
    String isPaid;

    @SerializedName("amount")
    String amount;

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

    public PaymentHistoryObject(String date, String category, String firstName, String lastName, String bookingStart, String bookingEnd, String bookingId, String categoryID) {
        this.date = date;
        this.category = category;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bookingStart = bookingStart;
        this.bookingEnd = bookingEnd;
        this.bookingId = bookingId;
        this.categoryID = categoryID;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
