package APIResponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PaymentIntiateResponse {
    @SerializedName("success")
    int success;

    @SerializedName("result")
    PaymentInfo paymentInfoObject;


    @SerializedName("error")
    String error;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public PaymentInfo getPaymentInfoObject() {
        return paymentInfoObject;
    }

    public void setPaymentInfoObject(PaymentInfo paymentInfoObject) {
        this.paymentInfoObject = paymentInfoObject;
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
