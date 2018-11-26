package ApiEntity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit Chhabra on 6/9/2018.
 */

public class SendMessageEntity {

    @SerializedName("userID")
    private String userID;

    @SerializedName("userIDTo")
    private String userIDTo;

    @SerializedName("message")
    private String message;

    public SendMessageEntity(String userID, String userIDTo, String message) {
        this.userID = userID;
        this.userIDTo = userIDTo;
        this.message = message;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserIDTo() {
        return userIDTo;
    }

    public void setUserIDTo(String userIDTo) {
        this.userIDTo = userIDTo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
