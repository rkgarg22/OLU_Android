package infrastructure;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Base64;
import android.util.Patterns;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.elisa.olu.MyReceiver;
import com.elisa.olu.MyReceiverForSession;
import com.elisa.olu.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import APIResponse.SavedLocationResponse;
import ApiObject.TodayBookingObject;

public class AppCommon {

    public static AppCommon mInstance = null;
    static Context mContext;

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1100;

    public static String latitudeValue = "";
    public static String longitudeValue = "";
    public static String address = "";


    public static AppCommon getInstance(Context _Context) {
        if (mInstance == null) {
            mInstance = new AppCommon();
        }
        mContext = _Context;
        return mInstance;
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public boolean isPasswordValid(String password) {
        return password.length() > 6;
    }

    public boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public void showDialog(Activity mactivity, String title) {
        if (!mactivity.isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mactivity);
            builder.setCancelable(false);
            builder.setMessage(title);
            builder.setCancelable(false);
            builder.setNegativeButton(mactivity.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }
    }

    public boolean isUserLogIn() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean(MYPerference.IS_USER_LOGIN, false);
    }

    public void setIsUserLogIn(boolean isUserLogIn) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(MYPerference.IS_USER_LOGIN, isUserLogIn);
        mEditor.apply();
    }

    public void setUserLatitude(double latitude) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putFloat(MYPerference.USER_LATITUDE, (float) latitude);
        mEditor.apply();
    }

    public void setUserLongitude(double longitude) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putFloat(MYPerference.USER_LONGITUDE, (float) longitude);
        mEditor.apply();
    }


    public float getUserLatitude() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        return mSharedPreferences.getFloat(MYPerference.USER_LATITUDE, 0.0f);
    }

    public float getUserLongitude() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        return mSharedPreferences.getFloat(MYPerference.USER_LONGITUDE, 0.0f);
    }

    public void setUserID(int userID) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt(MYPerference.USER_ID, userID);
        mEditor.apply();
    }

    public int getUserID() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        return mSharedPreferences.getInt(MYPerference.USER_ID, 0);
    }


    public void setName(String name) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(MYPerference.NAME, name);
        mEditor.apply();
    }

    public String getName() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.NAME, "");
    }

    public void setLastName(String lastName) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(MYPerference.LAST_NAME, lastName);
        mEditor.apply();
    }

    public String getLastName() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.LAST_NAME, "");
    }

    public void setUserEmail(String userEmail) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(MYPerference.EMAIL_ID, userEmail);
        mEditor.apply();
    }

    public String getUserEmail() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.EMAIL_ID, "");
    }

    public void setProfilePicUrl(String profilePicUrl) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(MYPerference.PROFILE_PIC_URL, profilePicUrl);
        mEditor.apply();
    }

    public String getProfilePicUrl() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.PROFILE_PIC_URL, "");
    }

    public void setPhone(String phoneNumber) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(MYPerference.phoneNumber, phoneNumber);
        mEditor.apply();
    }

    public String getPhone() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.phoneNumber, "");
    }


    public void setGender(String gender) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(MYPerference.gender, gender);
        mEditor.apply();
    }

    public String getGender() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.gender, "");
    }


    public void setDOB(String dob) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(MYPerference.dob, dob);
        mEditor.apply();
    }

    public String getDOb() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.dob, "");
    }


    public void setLanguage(String language) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(MYPerference.LANGUGAGE_SELECTION, language);
        mEditor.apply();
    }

    public String getSelectedLanguage() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.LANGUGAGE_SELECTION, "es");
    }

    public String getTokenId() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.tokenId, "");
    }


    public void setTokenId(String tokenId) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(MYPerference.tokenId, tokenId);
        mEditor.apply();
    }

    public String getBase64ImageString(Bitmap photo) {
        String imgString;
        if (photo != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] profileImage = outputStream.toByteArray();
            imgString = Base64.encodeToString(profileImage, Base64.NO_WRAP);
        } else {
            imgString = "";
        }
        return imgString;
    }


    public void setNonTouchableFlags(Activity activity) {
        if (activity != null) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    public void clearNonTouchableFlags(Activity mActivity) {

        if (mActivity != null) {
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    private void uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    mContext.getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);


            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void clearSharedPreference() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.clear();
        mEditor.apply();
        mEditor.apply();
    }


    public void setCurrentUser(int userType) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt(MYPerference.userType, userType);
        mEditor.apply();
    }

    public int getCurrentUser() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        return mSharedPreferences.getInt(MYPerference.userType, 0);
    }

    public void setIsAvailable(int userType) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt(MYPerference.isAvaialble, userType);
        mEditor.apply();
    }

    public int getIsAvailable() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        return mSharedPreferences.getInt(MYPerference.isAvaialble, 1);
    }

    public void setStartBookingID(String bookingID) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(MYPerference.bookingID, bookingID);
        mEditor.apply();
    }

    public void setStartTrainingObject(String trainerObject) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(MYPerference.TrainerObject, trainerObject);
        mEditor.apply();
    }

    public String getTrainerObject() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.TrainerObject, "");
    }

    public String getBookingID() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.bookingID, "");
    }


    public boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }


    public void setUpdate(String update) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(MYPerference.update, update);
        mEditor.apply();
    }

    public String getUpdate() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.update, "");
    }


    public String getPriceInFormat(String price) {
        //Double d = Double.parseDouble(price);
        //d = d * .001;
        // NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
        String priceFormat = "$" + price;

        return priceFormat;
    }

    public String getPriceInUSFormat(String price) {
        String priceFormat = "";
        if (!price.equals("")) {
            Double d = Double.parseDouble(price);
            d = d * .001;
            // NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
            priceFormat = String.format("%.3f", d);
            priceFormat = priceFormat.replace(",", ".");
        }
        return priceFormat;
    }

    public void setLocalLocation(String savedLocation) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(MYPerference.savedLocation, savedLocation);
        mEditor.apply();
    }

    public String getLocalLocation() {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MYPerference.mPREFS_NAME, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(MYPerference.savedLocation, "");

    }

    public boolean isTimeMoreThan30(String bookingDate) {
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// for booking date
        long bookingDateTimeInMill = 0;
        try {
            bookingDateTimeInMill = sdf2.parse(bookingDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        long different = bookingDateTimeInMill - c.getTimeInMillis();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;

        long minDiff = different / minutesInMilli;
        if (minDiff > 30) {
            return true;
        } else {
            return false;
        }
    }

    public void setAlaramForSession(TodayBookingObject todayBookingObject) {
        String bookDate = todayBookingObject.getDate() + " " + todayBookingObject.getBookingStart();
        if (isTimeMoreThan30(bookDate)) {
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// for booking date
            long bookingDateTime = 0;
            try {
                bookingDateTime = sdf2.parse(bookDate).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(bookingDateTime);
            c.add(Calendar.MINUTE, -30);
            long timeInMills = c.getTimeInMillis();

            Intent myIntent = new Intent(mContext, MyReceiverForSession.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, Integer.parseInt(todayBookingObject.getBookingId()), myIntent, 0);
            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMills, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMills, pendingIntent);
            }
        }
    }

    public void unRegisterAlarm(int bookingID) {
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(mContext, MyReceiverForSession.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, bookingID, myIntent, 0);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public void updateAnalytics(String message){
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, message);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

}



