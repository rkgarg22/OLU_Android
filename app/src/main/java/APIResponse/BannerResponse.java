package APIResponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ApiObject.BannerObject;

public class BannerResponse {
    @SerializedName("success")
    String success;

    @SerializedName("result")
    ArrayList<BannerObject> bannerObjectsArrayList;

    @SerializedName("error")
    String error;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public ArrayList<BannerObject> getBannerObjectsArrayList() {
        return bannerObjectsArrayList;
    }

    public void setBannerObjectsArrayList(ArrayList<BannerObject> bannerObjectsArrayList) {
        this.bannerObjectsArrayList = bannerObjectsArrayList;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
