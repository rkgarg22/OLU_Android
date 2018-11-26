package APIResponse;

import com.google.gson.annotations.SerializedName;

public class CardInfo {
    @SerializedName("token")
    String token;

    @SerializedName("cardNumber")
    String cardNumber;

    @SerializedName("cardType")
    String cardType;

    @SerializedName("cardExpire")
    String cardExpire;

    @SerializedName("requestId")
    String requestId;

    @SerializedName("message")
    String message;

    @SerializedName("defaultCard")
    int defaultCard;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardExpire() {
        return cardExpire;
    }

    public void setCardExpire(String cardExpire) {
        this.cardExpire = cardExpire;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getDefaultCard() {
        return defaultCard;
    }

    public void setDefaultCard(int defaultCard) {
        this.defaultCard = defaultCard;
    }
}
