package APIResponse;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PaymentInfo {


    @SerializedName("processUrl")
    ProcessUrlObject processUrlObject;

    @SerializedName("isTokenAvailable")
    int isTokenAvailable;

    @SerializedName("cardDetails")
    ArrayList<CardInfo> cardInfoArrayList;



    public int getIsTokenAvailable() {
        return isTokenAvailable;
    }

    public void setIsTokenAvailable(int isTokenAvailable) {
        this.isTokenAvailable = isTokenAvailable;
    }

    public ProcessUrlObject getProcessUrlObject() {
        return processUrlObject;
    }

    public void setProcessUrlObject(ProcessUrlObject processUrlObject) {
        this.processUrlObject = processUrlObject;
    }

    public ArrayList<CardInfo> getCardInfoArrayList() {
        return cardInfoArrayList;
    }

    public void setCardInfoArrayList(ArrayList<CardInfo> cardInfoArrayList) {
        this.cardInfoArrayList = cardInfoArrayList;
    }
}
