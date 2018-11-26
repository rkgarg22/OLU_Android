package APIResponse;

import com.google.gson.annotations.SerializedName;

public class BookingResponse {
    @SerializedName("success")
    int success;

    @SerializedName("result")
    String result;

    @SerializedName("error")
    String error;

    @SerializedName("isTokenSaved")
    int isTokenSaved;

    @SerializedName("paymentRequire")
    int paymentRequire;


    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getIsTokenSaved() {
        return isTokenSaved;
    }

    public void setIsTokenSaved(int isTokenSaved) {
        this.isTokenSaved = isTokenSaved;
    }

    public int getPaymentRequire() {
        return paymentRequire;
    }

    public void setPaymentRequire(int paymentRequire) {
        this.paymentRequire = paymentRequire;
    }
}
