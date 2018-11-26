package APIResponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ApiObject.PaymentHistoryObject;
import ApiObject.UserPaymentHistoryObject;

/**
 * Created by Ankit Chhabra on 6/3/2018.
 */

public class UserPaymentHistoryResponse {

    @SerializedName("success")
    int success;

    @SerializedName("result")
    ArrayList<UserPaymentHistoryObject> paymentHistoryObjectList;

    @SerializedName("error")
    String error;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public ArrayList<UserPaymentHistoryObject> getPaymentHistoryObjectList() {
        return paymentHistoryObjectList;
    }

    public void setPaymentHistoryObjectList(ArrayList<UserPaymentHistoryObject> paymentHistoryObjectList) {
        this.paymentHistoryObjectList = paymentHistoryObjectList;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
