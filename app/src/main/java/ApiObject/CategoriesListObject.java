package ApiObject;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit Chhabra on 7/26/2018.
 */

public class CategoriesListObject {

    @SerializedName("categoryName")
    private String catergoryName;

    @SerializedName("CategryID")
    private String CategryID;

    @SerializedName("singlePrice")
    private String singlePrice;

    @SerializedName("groupPrice2")
    private String groupPrice2;

    @SerializedName("groupPrice3")
    private String groupPrice3;

    @SerializedName("groupPrice4")
    private String groupPrice4;

    @SerializedName("companyPrice")
    private String companyPrice;

    public CategoriesListObject(String categryID, String singlePrice, String groupPrice2, String groupPrice3, String groupPrice4, String companyPrice) {
        CategryID = categryID;
        this.singlePrice = singlePrice;
        this.groupPrice2 = groupPrice2;
        this.groupPrice3 = groupPrice3;
        this.groupPrice4 = groupPrice4;
        this.companyPrice = companyPrice;
    }

    public CategoriesListObject(String catergoryName, String categryID, String singlePrice, String groupPrice2, String groupPrice3, String groupPrice4, String companyPrice) {
        this.catergoryName = catergoryName;
        CategryID = categryID;
        this.singlePrice = singlePrice;
        this.groupPrice2 = groupPrice2;
        this.groupPrice3 = groupPrice3;
        this.groupPrice4 = groupPrice4;
        this.companyPrice = companyPrice;
    }

    public String getCategryID() {
        return CategryID;
    }

    public void setCategryID(String categryID) {
        CategryID = categryID;
    }

    public String getCatergoryName() {
        return catergoryName;
    }

    public void setCatergoryName(String catergoryName) {
        this.catergoryName = catergoryName;
    }

    public String getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(String singlePrice) {
        this.singlePrice = singlePrice;
    }

    public String getGroupPrice2() {
        return groupPrice2;
    }

    public void setGroupPrice2(String groupPrice2) {
        this.groupPrice2 = groupPrice2;
    }

    public String getGroupPrice3() {
        return groupPrice3;
    }

    public void setGroupPrice3(String groupPrice3) {
        this.groupPrice3 = groupPrice3;
    }

    public String getGroupPrice4() {
        return groupPrice4;
    }

    public void setGroupPrice4(String groupPrice4) {
        this.groupPrice4 = groupPrice4;
    }

    public String getCompanyPrice() {
        return companyPrice;
    }

    public void setCompanyPrice(String companyPrice) {
        this.companyPrice = companyPrice;
    }
}
