package APIResponse;

import com.google.gson.annotations.SerializedName;


public class ForgotPasswordResponse {
    @SerializedName("success")
    String success;

    @SerializedName("result")
    String mResult;

    @SerializedName("error")
    String error;

    public ForgotPasswordResponse(String success, String mResult, String error) {
        this.success = success;
        this.mResult = mResult;
        this.error = error;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getmResult() {
        return mResult;
    }

    public void setmResult(String mResult) {
        this.mResult = mResult;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
