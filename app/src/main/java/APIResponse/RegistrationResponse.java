package APIResponse;

import com.google.gson.annotations.SerializedName;

import ApiObject.User;

public class RegistrationResponse {
    @SerializedName("success")
    private int success;

    @SerializedName("result")
    private User userEntity;

    @SerializedName("error")
    private String error;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public User getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(User userEntity) {
        this.userEntity = userEntity;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


}
