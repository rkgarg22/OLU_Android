package ApiObject;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Imark on 7/24/2017.
 */

public class InboxResponse {

    @SerializedName("success")
    private int success;

    @SerializedName("result")
    private List<InboxObject> inboxObjectList;

    @SerializedName("error")
    private String error;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<InboxObject> getInboxObjectList() {
        return inboxObjectList;
    }

    public void setInboxObjectList(List<InboxObject> inboxObjectList) {
        this.inboxObjectList = inboxObjectList;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
