package APIResponse;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kartikeya on 20/10/2018.
 */

public class CommonResponse {
    @SerializedName("success")
    int success;

    @SerializedName("result")
    int result;

    @SerializedName("error")
    String error;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
