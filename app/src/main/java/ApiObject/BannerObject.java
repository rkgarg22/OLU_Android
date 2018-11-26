package ApiObject;

import com.google.gson.annotations.SerializedName;


public class BannerObject {
    @SerializedName("imgUrl")
    String imgUrl;

    @SerializedName("url")
    String url;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
