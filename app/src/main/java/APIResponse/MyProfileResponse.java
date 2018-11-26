package APIResponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ApiObject.BannerObject;
import ApiObject.MyProfileObject;

/**
 * Created by Ankit Chhabra on 7/18/2018.
 */

public class MyProfileResponse {

    @SerializedName("success")
    String success;

    @SerializedName("result")
    MyProfileObject myProfileObject;

    @SerializedName("error")
    String error;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public MyProfileObject getMyProfileObject() {
        return myProfileObject;
    }

    public void setMyProfileObject(MyProfileObject myProfileObject) {
        this.myProfileObject = myProfileObject;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
