package APIResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ApiObject.UserListObject;

public class UserListingResponse {

    @SerializedName("success")
    int success;

    @SerializedName("result")
    List<UserListObject> userListObjectList;

    @SerializedName("error")
    String error;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<UserListObject> getUserListObjectList() {
        return userListObjectList;
    }

    public void setUserListObjectList(List<UserListObject> userListObjectList) {
        this.userListObjectList = userListObjectList;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
