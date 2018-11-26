package APIResponse;

import com.google.gson.annotations.SerializedName;

import ApiObject.ResturantObject;

public class GetResturantDetailResponse {
    @SerializedName("success")
    String success;

    @SerializedName("result")
    ResturantObject rsturantObject;

    @SerializedName("error")
    String error;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public ResturantObject getRsturantObject() {
        return rsturantObject;
    }

    public void setRsturantObject(ResturantObject rsturantObject) {
        this.rsturantObject = rsturantObject;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
