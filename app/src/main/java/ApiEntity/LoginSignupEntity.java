package ApiEntity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit Chhabra on 5/5/2018.
 */

public class LoginSignupEntity {

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("facebookId")
    private String facebookId;

    @SerializedName("emailAddress")
    private String emailAddress;

    @SerializedName("password")
    private String password;

    @SerializedName("userImageUrl")
    private String userImageUrl;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("userType")
    private int userType;

    @SerializedName("firebaseTokenId")
    private String firebaseTokenId;

    @SerializedName("deviceType")
    private String deviceType;

    @SerializedName("gender")
    private String gender;

    @SerializedName("age")
    private int age;

    @SerializedName("phone")
    private String phone;

    @SerializedName("dob")
    private String dob;


    public LoginSignupEntity(String emailAddress, String password, String firebaseTokenId, String deviceType,int userType) {
        this.emailAddress = emailAddress;
        this.password = password;
        this.firebaseTokenId = firebaseTokenId;
        this.deviceType = deviceType;
        this.userType = userType;
    }

    public LoginSignupEntity(String firstName, String lastName, String facebookId, String emailAddress, String password, String userImageUrl, String latitude, String longitude, int userType, String firebaseTokenId, String deviceType, String gender, int age, String phone, String dob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.facebookId = facebookId;
        this.emailAddress = emailAddress;
        this.password = password;
        this.userImageUrl = userImageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userType = userType;
        this.firebaseTokenId = firebaseTokenId;
        this.deviceType = deviceType;
        this.gender = gender;
        this.age = age;
        this.phone = phone;
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getFirebaseTokenId() {
        return firebaseTokenId;
    }

    public void setFirebaseTokenId(String firebaseTokenId) {
        this.firebaseTokenId = firebaseTokenId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
