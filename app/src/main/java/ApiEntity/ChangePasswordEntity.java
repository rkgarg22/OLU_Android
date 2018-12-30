package ApiEntity;

import com.google.gson.annotations.SerializedName;


public class ChangePasswordEntity {

    @SerializedName("userID")
    private int userID;

    @SerializedName("newPassword")
    private String newPassword;

    @SerializedName("oldPassword")
    private String oldPassword;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public ChangePasswordEntity(int userID, String newPassword, String oldPassword) {
        this.userID = userID;
        this.newPassword = newPassword;
        this.oldPassword = oldPassword;
    }
}
