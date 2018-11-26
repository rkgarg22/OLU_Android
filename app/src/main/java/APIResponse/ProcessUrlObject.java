package APIResponse;

import com.google.gson.annotations.SerializedName;

public class ProcessUrlObject {

    @SerializedName("requestId")
    String requestId;

    @SerializedName("processURL")
    String processURL;

    @SerializedName("message")
    String message;


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getProcessURL() {
        return processURL;
    }

    public void setProcessURL(String processURL) {
        this.processURL = processURL;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
