package APIResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ApiObject.TodayBookingObject;

/**
 * Created by Ankit Chhabra on 6/3/2018.
 */

public class OnGoingBookingResponse {

    @SerializedName("success")
    int success;

    @SerializedName("result")
    TodayBookingObject todayBookingObject;

    @SerializedName("error")
    String error;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public TodayBookingObject getTodayBookingObject() {
        return todayBookingObject;
    }

    public void setTodayBookingObject(TodayBookingObject todayBookingObject) {
        this.todayBookingObject = todayBookingObject;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
