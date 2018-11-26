package ApiObject;

import com.google.gson.annotations.SerializedName;

public class InboxObject {
    @SerializedName("anotherUserID")
    String anotherUserID;
    @SerializedName("profileImgUrl")
    private String profileUrl;
    @SerializedName("name")
    private String name;
    @SerializedName("lastMessage")
    private String messages;
    @SerializedName("lastMessageTime")
    private String date;

    public InboxObject(String profileUrl, String name, String messages, String date) {
        this.profileUrl = profileUrl;
        this.name = name;
        this.messages = messages;
        this.date = date;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAnotherUserID() {
        return anotherUserID;
    }

    public void setAnotherUserID(String anotherUserID) {
        this.anotherUserID = anotherUserID;
    }
}
