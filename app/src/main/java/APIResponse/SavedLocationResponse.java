package APIResponse;

import java.util.List;

import ApiObject.SavedLocation;

/**
 * Created by Ankit Chhabra on 9/5/2018.
 */

public class SavedLocationResponse {

    List<SavedLocation> savedLocationResponseList;

    public SavedLocationResponse(List<SavedLocation> savedLocationResponseList) {
        this.savedLocationResponseList = savedLocationResponseList;
    }

    public List<SavedLocation> getSavedLocationResponseList() {
        return savedLocationResponseList;
    }

    public void setSavedLocationResponseList(List<SavedLocation> savedLocationResponseList) {
        this.savedLocationResponseList = savedLocationResponseList;
    }
}
