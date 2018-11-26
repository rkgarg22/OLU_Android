package ApiObject;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit Chhabra on 7/19/2018.
 */

public class CategoriesObject {
    @SerializedName("categoryID")
    String categoryID;

    @SerializedName("name")
    String name;

    @SerializedName("categoryName")
    String categoryName;

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

    String isSelected;

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
