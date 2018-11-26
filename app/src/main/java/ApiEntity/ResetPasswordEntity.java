package ApiEntity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit Chhabra on 5/7/2018.
 */

public class ResetPasswordEntity {

    @SerializedName("userID")
    private String userID;

    @SerializedName("oldPassword")
    private String oldPassword;

    @SerializedName("newPassword")
    private String newPassword;

    public ResetPasswordEntity(String userID, String oldPassword, String newPassword) {
        this.userID = userID;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
