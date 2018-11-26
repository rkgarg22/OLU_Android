package APIResponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import ApiObject.PaymentHistoryObject;
import ApiObject.TodayBookingObject;

/**
 * Created by Ankit Chhabra on 6/3/2018.
 */

public class PaymentHistoryResponse {

    @SerializedName("success")
    int success;

    @SerializedName("result")
    ArrayList<PaymentHistoryObject> paymentHistoryObjectList;

    @SerializedName("error")
    String error;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public ArrayList<PaymentHistoryObject> getPaymentHistoryObjectList() {
        return paymentHistoryObjectList;
    }

    public void setPaymentHistoryObjectList(ArrayList<PaymentHistoryObject> paymentHistoryObjectList) {
        this.paymentHistoryObjectList = paymentHistoryObjectList;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
