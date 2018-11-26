package APIResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ApiObject.TodayBookingObject;
import ApiObject.UserListObject;

/**
 * Created by Ankit Chhabra on 6/3/2018.
 */

public class TodayBookingResponse {

    @SerializedName("success")
    int success;

    @SerializedName("result")
    List<TodayBookingObject> todayBookingObjectList;

    @SerializedName("error")
    String error;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<TodayBookingObject> getTodayBookingObjectList() {
        return todayBookingObjectList;
    }

    public void setTodayBookingObjectList(List<TodayBookingObject> todayBookingObjectList) {
        this.todayBookingObjectList = todayBookingObjectList;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
