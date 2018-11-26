package ApiObject;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatResponse {

    @SerializedName("success")
    private int success;

    @SerializedName("result")
    private List<ChatObject> chatObjectList;

    @SerializedName("error")
    private String error;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<ChatObject> getChatObjectList() {
        return chatObjectList;
    }

    public void setChatObjectList(List<ChatObject> chatObjectList) {
        this.chatObjectList = chatObjectList;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
