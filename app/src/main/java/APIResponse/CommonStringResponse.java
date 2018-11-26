package APIResponse;

import com.google.gson.annotations.SerializedName;

public class CommonStringResponse {
    @SerializedName("success")
    String success;

    @SerializedName("result")
    String result;

    @SerializedName("error")
    String error;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
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
}
