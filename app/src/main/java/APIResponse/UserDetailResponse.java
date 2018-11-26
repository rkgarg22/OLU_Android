package APIResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ApiObject.TrainerDetailObject;
import ApiObject.UserListObject;

/**
 * Created by Ankit Chhabra on 5/27/2018.
 */

public class UserDetailResponse {

    @SerializedName("success")
    int success;

    @SerializedName("result")
    TrainerDetailObject  userListObject;

    @SerializedName("error")
    String error;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public TrainerDetailObject getUserListObject() {
        return userListObject;
    }

    public void setUserListObject(TrainerDetailObject userListObject) {
        this.userListObject = userListObject;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
