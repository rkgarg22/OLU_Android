package APIResponse;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit Chhabra on 9/13/2018.
 */

public class ProcessUrlResponse {
    @SerializedName("success")
    int success;

    @SerializedName("result")
    ProcessUrlObject processUrlObject;

    @SerializedName("error")
    String error;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public ProcessUrlObject getProcessUrlObject() {
        return processUrlObject;
    }

    public void setProcessUrlObject(ProcessUrlObject processUrlObject) {
        this.processUrlObject = processUrlObject;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
