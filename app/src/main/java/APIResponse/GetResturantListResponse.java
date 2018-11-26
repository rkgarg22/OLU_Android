package APIResponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ApiObject.ResturantObject;

public class GetResturantListResponse {
    @SerializedName("success")
    String success;

    @SerializedName("result")
    ArrayList<ResturantObject> resturantObjectList;

    @SerializedName("error")
    String error;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public ArrayList<ResturantObject> getResturantObjectList() {
        return resturantObjectList;
    }

    public void setResturantObjectList(ArrayList<ResturantObject> resturantObjectList) {
        this.resturantObjectList = resturantObjectList;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
