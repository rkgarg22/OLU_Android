package ApiObject;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit Chhabra on 6/3/2018.
 */

public class UserPaymentHistoryObject {

    @SerializedName("date")
    String date;

    @SerializedName("categoryName")
    String category;

    @SerializedName("amount")
    String amount;

    @SerializedName("time")
    String time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
