package APIResponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;




public class CardListResponse {

    @SerializedName("success")
    int success;

    @SerializedName("result")
    ArrayList<CardInfo> cardInfoArrayList;

    @SerializedName("error")
    String error;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ArrayList<CardInfo> getCardInfoArrayList() {
        return cardInfoArrayList;
    }

    public void setCardInfoArrayList(ArrayList<CardInfo> cardInfoArrayList) {
        this.cardInfoArrayList = cardInfoArrayList;
    }
}
