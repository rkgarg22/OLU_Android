package Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tucan.olu.AcceptedSessionDetailActivity;
import com.tucan.olu.Firebase.NotificationPopupActivity;
import com.tucan.olu.HistoricalActivity;
import com.tucan.olu.R;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ApiObject.TodayBookingObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import infrastructure.AppCommon;

/**
 * Created by Ankit Chhabra on 4/29/2018.
 */

public class SesionerAdapter extends RecyclerView.Adapter<SesionerAdapter.ViewHolder> {
    private Activity context;
    List<TodayBookingObject> todayBookingObjectList;
    String bookingType;
    boolean isComingFromHome;

    public SesionerAdapter(Activity context, List<TodayBookingObject> todayBookingObjectList, String bookingType, boolean isComingFromHome) {
        this.context = context;
        this.todayBookingObjectList = todayBookingObjectList;
        this.bookingType = bookingType;
        this.isComingFromHome =isComingFromHome;
    }

    @Override
    public SesionerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sesioner_row, parent, false);
        View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_row, parent, false);
        if (bookingType.equals("3") && !isComingFromHome) {
            return new SesionerAdapter.ViewHolder(view2);
        } else {
            return new SesionerAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final SesionerAdapter.ViewHolder holder, int position) {
        TodayBookingObject todayBookingObject = todayBookingObjectList.get(position);
        holder.date.setText(todayBookingObject.getDate());
        holder.categoryName.setText(todayBookingObject.getCategory());
        holder.name.setText(todayBookingObject.getFirstName() + " " + todayBookingObject.getLastName());
        holder.bookingTime.setText(getTimeInForamt(todayBookingObject.getBookingStart()) + " - " + getTimeInForamt(todayBookingObject.getBookingEnd()));
        setupCatergoryImage(todayBookingObject.getCategoryID(), holder.categoryImage);
    }

    public String getDateInFormat(String date) {
        String formatterString = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date formatDate = formatter.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(formatDate);
            formatter = new SimpleDateFormat("dd/MMMM");
            formatterString = formatter.format(formatDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatterString;
    }

    public String getTimeInForamt(String time) {
        String formatterTimeString = "";
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        try {
            Date formatDate = formatter.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(formatDate);
            formatter = new SimpleDateFormat("hh:mm a", Locale.US);
            formatterTimeString = formatter.format(formatDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatterTimeString;
    }

    private void setupCatergoryImage(String categoryID, ImageView categoryImage) {
        switch (categoryID) {
            case "1":
                categoryImage.setImageResource(R.drawable.kick_boxing);
                break;
            case "2":
                categoryImage.setImageResource(R.drawable.cardio_crossfit);
                break;
            case "3":
                categoryImage.setImageResource(R.drawable.stretching);
                break;
            case "4":
                categoryImage.setImageResource(R.drawable.yoga);
                break;
            case "5":
                categoryImage.setImageResource(R.drawable.pilates);
                break;
            case "8":
                categoryImage.setImageResource(R.drawable.masajes_deportivos);
                break;
            case "9":
                categoryImage.setImageResource(R.drawable.fisioterapia);
                break;
            case "11":
                categoryImage.setImageResource(R.drawable.danza_fit);
                break;
            case "10":
                categoryImage.setImageResource(R.drawable.gimnasia);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return todayBookingObjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date)
        TextView date;

        @BindView(R.id.bookingTime)
        TextView bookingTime;

        @BindView(R.id.categoryImage)
        ImageView categoryImage;

        @BindView(R.id.categoryName)
        TextView categoryName;

        @BindView(R.id.name)
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Nullable
        @Optional
        @OnClick(R.id.parentRow)
        void parentRow() {
            if (AppCommon.getInstance(context).getCurrentUser() == 2) {
            } else {
                Intent intent = new Intent(context, NotificationPopupActivity.class);
                intent.putExtra("bookingType",bookingType);
                intent.putExtra("bookingFor", todayBookingObjectList.get(getAdapterPosition()).getBookingFor());
                intent.putExtra("pendingObject", new Gson().toJson(todayBookingObjectList.get(getAdapterPosition())));
                context.startActivity(intent);
            }
        }

        @Nullable
        @Optional
        @OnClick({R.id.parentRowAccepted,R.id.acceptParentClick})
        void parentRowAccepted() {
            if (AppCommon.getInstance(context).getCurrentUser() == 2) {
            } else {
                if(isComingFromHome){
                    Intent intent = new Intent(context, AcceptedSessionDetailActivity.class);
                    intent.putExtra("userObject", new Gson().toJson(todayBookingObjectList.get(getAdapterPosition())));
                    context.startActivity(intent);
                }else {
                    if(bookingType.equals("0")) {
                        Intent intent = new Intent(context, NotificationPopupActivity.class);
                        intent.putExtra("bookingType", bookingType);
                        intent.putExtra("bookingFor", todayBookingObjectList.get(getAdapterPosition()).getBookingFor());
                        intent.putExtra("pendingObject", new Gson().toJson(todayBookingObjectList.get(getAdapterPosition())));
                        context.startActivity(intent);
                    }else if(bookingType.equals("3")){
                        Intent intent = new Intent(context, AcceptedSessionDetailActivity.class);
                        intent.putExtra("userObject", new Gson().toJson(todayBookingObjectList.get(getAdapterPosition())));
                        context.startActivity(intent);
                    }
                }
            }
        }

        @Nullable
        @Optional
        @OnClick({R.id.cancelText, R.id.cancel})
        void cancel() {
            ((HistoricalActivity) context).setCanclerBooking(getAdapterPosition());
        }

        @Nullable
        @Optional
        @OnClick({R.id.phone, R.id.phoneText})
        void phone() {
            TodayBookingObject todayBookingObject = todayBookingObjectList.get(getAdapterPosition());
            ((HistoricalActivity) context).requestPermissionForCall(todayBookingObject.getPhoneNumber());
        }

        @Nullable
        @Optional
        @OnClick({R.id.chatIcon, R.id.chat})
        void chatClick() {
            ((HistoricalActivity) context).openChatScreen(getAdapterPosition());
        }


        @Nullable
        @Optional
        @OnClick(R.id.directionLayout)
        public void directionClick(View v){
            ((HistoricalActivity) context).directionClick(getAdapterPosition());
        }
    }
}

