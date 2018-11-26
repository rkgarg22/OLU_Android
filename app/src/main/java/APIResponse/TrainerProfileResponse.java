package APIResponse;

import com.elisa.olu.TrainerProfileActivity;
import com.google.gson.annotations.SerializedName;

import ApiObject.TrainerDetailObject;
import ApiObject.TrainerProfileObject;

public class TrainerProfileResponse {
    @SerializedName("success")
    int success;

    @SerializedName("result")
    TrainerProfileObject trainerProfileObject;

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

    public TrainerProfileObject getTrainerProfileObject() {
        return trainerProfileObject;
    }

    public void setTrainerProfileObject(TrainerProfileObject trainerProfileObject) {
        this.trainerProfileObject = trainerProfileObject;
    }
}
