package API;

import APIResponse.BookingResponse;
import APIResponse.CardListResponse;
import APIResponse.CommonIntResponse;
import APIResponse.CommonStringResponse;
import APIResponse.MyProfileResponse;
import APIResponse.OnGoingBookingResponse;
import APIResponse.PaymentCollectResponse;
import APIResponse.PaymentHistoryResponse;
import APIResponse.PaymentIntiateResponse;
import APIResponse.ProcessUrlResponse;
import APIResponse.RegistrationResponse;
import APIResponse.TodayBookingResponse;
import APIResponse.TrainerProfileResponse;
import APIResponse.UserDetailResponse;
import APIResponse.UserListingResponse;
import APIResponse.UserPaymentHistoryResponse;
import APIResponse.CommonResponse;
import ApiEntity.ChangePasswordEntity;
import ApiEntity.CreateAgendaEntity;
import ApiEntity.LoginSignupEntity;
import ApiEntity.ReviewEntity;
import ApiEntity.SendMessageEntity;
import ApiObject.ChatResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface PretoAppService {

    @POST("users/login/index.php")
    Call<RegistrationResponse> login(
            @Body LoginSignupEntity loginSignupEntity
    );

    @GET("users/forgotPassword/")
    Call<CommonIntResponse> forgotPassword(
            @Query("emailAddress") String emailAddress
    );

    @POST("trainingComplete/index.php")
    Call<CommonIntResponse> trainingComplete(
            @Body ReviewEntity reviewEntity
    );

    @POST("users/registration/index.php")
    Call<RegistrationResponse> registration(
            @Body LoginSignupEntity loginSignupEntity
    );

    @Multipart
    @POST("users/trainerSignup/index.php")
    Call<RegistrationResponse> registrationTrainer(
            @Part("firstName") RequestBody firstName,
            @Part("lastName") RequestBody lastName,
            @Part("emailAddress") RequestBody emailAddress,
            @Part("password") RequestBody password,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("firebaseTokenId") RequestBody firebaseTokenId,
            @Part("deviceType") RequestBody deviceType,
            @Part("gender") RequestBody gender,
            @Part("age") RequestBody age,
            @Part("phone") RequestBody phone,
            @Part("dob") RequestBody dob,
            @Part("description") RequestBody description,
            @Part("categories") RequestBody category,
            @Part MultipartBody.Part imageUrl
    );

//    @POST("users/resetPassword/index.php")
//    Call<RegistrationResponse> resetPassword(
//            @Body LoginSignupEntity loginSignupEntity
//    );


    @Multipart
    @POST("users/userUpdateProfile/")
    Call<CommonIntResponse> updateUserProfile(
            @Part("userID") RequestBody userID,
            @Part("firstName") RequestBody firstName,
            @Part("lastName") RequestBody lastName,
            @Part("gender") RequestBody gender,
            @Part("phone") RequestBody phone,
            @Part("dob") RequestBody dob,
            @Part MultipartBody.Part imageUrl
    );

    @Multipart
    @POST("users/updateUserImage/")
    Call<CommonIntResponse> updateUserImage(
            @Part("userID") RequestBody userID,
            @Part MultipartBody.Part imageUrl
    );


    @GET("users/logout/index.php/")
    Call<CommonIntResponse> logout(
            @Query("userID") int userID
    );

    @GET("users/isAvailable/")
    Call<CommonIntResponse> isAvailable(
            @Query("userID") int userID,
            @Query("isAvailable") int isAvailable
    );

    @GET("userDetails/")
    Call<UserDetailResponse> userDetails(
            @Query("userID") int userID,
            @Query("trainerUserID") String trainerUserID,
            @Query("category") String category,
            @Query("language") String language
    );

    @GET("userListing/")
    Call<UserListingResponse> userListing(
            @Query("userID") int userID,
            @Query("searchText") String searchText,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("offSet") int offSet,
            @Query("time") String time,
            @Query("date") String date,
            @Query("category") String category,
            @Query("gender") String gender,
            @Query("rating") String rating,
            @Query("selectGroup") String selectGroup,
            @Query("lang") String lang
    );


    @GET("users/updateUserLocation/")
    Call<CommonStringResponse> updateUserLocation(
            @Query("userID") int userID,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("lang") String lang
    );

    @GET("booking/")
    Call<BookingResponse> booking(
            @Query("userID") int userID,
            @Query("bookinguserID") String trainerUserID,
            @Query("categoryID") String categoryID,
            @Query("bookingDate") String bookingDate,
            @Query("bookingTime") String bookingTime,
            @Query("bookingType") String bookingType,
            @Query("longitude") String longitude,
            @Query("latitude") String latitude,
            @Query("address") String address
    );

    @GET("users/trainerAvailable/")
    Call<CommonIntResponse> trainerAvailable(
            @Query("userID") int userID,
            @Query("trainerUserID") String trainerUserID,
            @Query("categoryID") String categoryID,
            @Query("date") String bookingDate,
            @Query("time") String bookingTime
    );

    @GET("bookingConfirm/")
    Call<CommonIntResponse> bookingConfirm(
            @Query("userID") int userID,
            @Query("bookingID") String bookingID,
            @Query("state") String state,
            @Query("isPaymentRequire") String isPaymentRequire
    );

    @GET("bookingDetails/")
    Call<CommonIntResponse> bookingDetails(
            @Query("userID") int userID,
            @Query("bookingID") String bookingID,
            @Query("language") String language
    );

    @GET("bookingStateChange/")
    Call<CommonIntResponse> bookingStateChange(
            @Query("userID") int userID,
            @Query("bookingID") String bookingID,
            @Query("state") String state
    );

    @GET("bookingHistory/")
    Call<TodayBookingResponse> bookingHistory(
            @Query("userID") int userID,
            @Query("status") String status,
            @Query("lang") String lang
    );

    @GET("todayBooking/")
    Call<TodayBookingResponse> todayBooking(
            @Query("userID") int userID,
            @Query("language") String language,
            @Query("date") String date
    );

    @GET("users/myProfile/")
    Call<MyProfileResponse> myProfile(
            @Query("userID") int userID,
            @Query("language") String language
    );

    @GET("messages/list/index.php/")
    Call<ChatResponse> messagesList(
            @Query("userID") int userID,
            @Query("userIDTo") String userIDTo
    );

    @POST("messages/send/index.php/")
    Call<CommonIntResponse> sendMessage(
            @Body SendMessageEntity sendMessageEntity
    );

    // Payment API

    @GET("payment/initiate/")
    Call<PaymentIntiateResponse> paymentIntiate(
            @Query("userID") int userID,
            @Query("lang") String lang
    );

    @GET("payment/paymentProcessUrl/")
    Call<ProcessUrlResponse> paymentProcessUrl(
            @Query("userID") int userID,
            @Query("lang") String lang
    );

    @GET("payment/paymentCollectReq/")
    Call<CommonIntResponse> paymentCollectReq(
            @Query("userID") int userID,
            @Query("requestId") String requestId,
            @Query("lang") String lang
    );

    @GET("payment/deletePayment/")
    Call<CommonIntResponse> deletePayment(
            @Query("userID") int userID,
            @Query("requestId") String requestId,
            @Query("lang") String lang
    );

    @GET("payment/collect/")
    Call<PaymentCollectResponse> paymentCollect(
            @Query("userID") int userID,
            @Query("token") String token,
            @Query("planID") String planID

    );

    @GET("payment/paymentHistory/")
    Call<PaymentHistoryResponse> getPaymentHistory(
            @Query("userID") int userID,
            @Query("order") String order,
            @Query("isPaid") String isPaid
    );

    @GET("users/trainerProfile/")
    Call<TrainerProfileResponse> getTrainerProfile(
            @Query("userID") String userID
    );


    @GET("payment/paymentHistoryUser/")
    Call<UserPaymentHistoryResponse> getPaymentHistoryForUser(
            @Query("userID") int userID,
            @Query("order") String order
    );

    @GET("promoCode/")
    Call<CommonIntResponse> promoCode(
            @Query("userID") int userID,
            @Query("promoCode") String promoCode
    );

    @Multipart
    @POST("users/trainerUpdate/")
    Call<CommonIntResponse> updateTrainer(
            @Part("userID") RequestBody userID,
            @Part("firstName") RequestBody firstName,
            @Part("lastName") RequestBody lastName,
            @Part("gender") RequestBody gender,
            @Part("phone") RequestBody phone,
            @Part("dob") RequestBody dob,
            @Part("description") RequestBody description,
            @Part("categories1") RequestBody category,
            @Part MultipartBody.Part imageUrl
    );

    @GET("wallet/")
    Call<CommonResponse> vaultBalance(
            @Query("userID") int userID,
            @Query("lang") String lang
    );

    @POST("agenda/create/")
    Call<CommonResponse> createAgenda(
            @Body CreateAgendaEntity createAgendaEntity
    );

    @GET("agenda/delete/")
    Call<CommonResponse> deleteAgenda(
            @Query("userID") int userID,
            @Query("agendaID") int agendaId,
            @Query("lang") String lang
    );

    @POST("agenda/update/")
    Call<CommonResponse> updateAgenda(@Body CreateAgendaEntity createAgendaEntity);

    @GET("trainerAvailable")
    Call<CommonResponse> checkLockButtonStatus(
            @Query("userID") int userID,
            @Query("lang") String lang
    );

    @GET("bookingPending")
    Call<OnGoingBookingResponse> getOnGoingSession(
            @Query("userID") int userID,
            @Query("lang") String lang
    );

    @GET("payment/selectCard")
    Call<CommonResponse> setCardSelection(
            @Query("userID") int userID,
            @Query("requestId") String requestID,
            @Query("lang") String lang
    );

    @GET("payment/cardList")
    Call<CardListResponse> getCardList(
            @Query("userID") int userID,
            @Query("lang") String lang
    );

    @POST("users/resetPassword/")
    Call<CommonResponse> resetPassword(
            @Body ChangePasswordEntity changePasswordEntity
    );

    @GET("users/UpdateDeviceToken")
    Call<CommonStringResponse> updateDeviceToken(
            @Query("userID") int userID,
            @Query("firebaseTokenId") String firebaseTokenId
    );
}
