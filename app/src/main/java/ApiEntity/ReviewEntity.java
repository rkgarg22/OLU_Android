package ApiEntity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit Chhabra on 9/4/2018.
 */

public class ReviewEntity {

    @SerializedName("userID")
    private int userID;

    @SerializedName("bookingID")
    private String bookingID;

    @SerializedName("rating")
    private float rating;

    @SerializedName("comment")
    private String comment;

    public ReviewEntity(int userID, String bookingID, float rating, String comment) {
        this.userID = userID;
        this.bookingID = bookingID;
        this.rating = rating;
        this.comment = comment;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
