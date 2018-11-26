package ApiObject;

import com.google.gson.annotations.SerializedName;

public class ChatObject {

    @SerializedName("messageID")
    private String messageID;

    @SerializedName("messageFromID")
    private String messageFromID;

    @SerializedName("messageToID")
    private String messageToID;

    @SerializedName("messageFrom")
    private String messageFrom;

    @SerializedName("messageTo")
    private String messageTo;

    @SerializedName("message")
    private String message;

    @SerializedName("messageTime")
    private String messageTime;

    String isHeader;

    String date;

    String time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ChatObject(String messageID, String messageFrom, String messageTo, String message) {
        this.messageID = messageID;
        this.messageFrom = messageFrom;
        this.messageTo = messageTo;
        this.message = message;
    }

    public String getMessageFromID() {
        return messageFromID;
    }

    public void setMessageFromID(String messageFromID) {
        this.messageFromID = messageFromID;
    }

    public String getMessageToID() {
        return messageToID;
    }

    public void setMessageToID(String messageToID) {
        this.messageToID = messageToID;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getIsHeader() {
        return isHeader;
    }

    public void setIsHeader(String isHeader) {
        this.isHeader = isHeader;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getMessageFrom() {
        return messageFrom;
    }

    public void setMessageFrom(String messageFrom) {
        this.messageFrom = messageFrom;
    }

    public String getMessageTo() {
        return messageTo;
    }

    public void setMessageTo(String messageTo) {
        this.messageTo = messageTo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
