package Adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tucan.olu.AcceptedSessionDetailActivity;
import com.tucan.olu.R;
import com.tucan.olu.TrainerHomeActivity;
import com.facebook.drawee.view.SimpleDraweeView;
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

public class TodayBookingAdapter extends RecyclerView.Adapter<TodayBookingAdapter.ViewHolder> {
    private Activity context;
    List<TodayBookingObject> todayBookingObjectList;


    public TodayBookingAdapter(Activity context, List<TodayBookingObject> todayBookingObjectList) {
        this.context = context;
        this.todayBookingObjectList = todayBookingObjectList;
    }

    @Override
    public TodayBookingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sesion_accepted_row, parent, false);
        return new TodayBookingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TodayBookingAdapter.ViewHolder holder, int position) {
        TodayBookingObject todayBookingObject = todayBookingObjectList.get(position);
        holder.categoryName.setText(todayBookingObject.getCategory());
        holder.bookingTime.setText(getTimeInForamt(todayBookingObject.getBookingStart()));
        if (todayBookingObject.getIsAgenda()==0) {
            holder.userImage.setVisibility(View.VISIBLE);
            holder.userImage.setImageURI(Uri.parse(todayBookingObject.getUserImageUrl()));
            holder.name.setText(todayBookingObject.getFirstName() + " " + todayBookingObject.getLastName());
        }else {
            holder.userImage.setVisibility(View.INVISIBLE);
            holder.name.setText(todayBookingObject.getAgendaText());
        }
        if (context instanceof TrainerHomeActivity) {
            if (todayBookingObject.getStatus().equals("1")) {
                holder.parentRowAccepted.setBackgroundColor(ContextCompat.getColor(context, R.color.greyColor));
            } else {
                holder.parentRowAccepted.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
            }
        }
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

        @BindView(R.id.bookingTime)
        TextView bookingTime;

        @BindView(R.id.userImage)
        SimpleDraweeView userImage;

        @BindView(R.id.categoryName)
        TextView categoryName;

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.parentRowAccepted)
        LinearLayout parentRowAccepted;

        @BindView(R.id.textLayout)
        LinearLayout textLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.parentRowAccepted})
        void parentRowAccepted() {
            if (context instanceof TrainerHomeActivity) {
                TodayBookingObject obj = todayBookingObjectList.get(getAdapterPosition());
                if(obj.getIsAgenda()==0) {
                    if (!todayBookingObjectList.get(getAdapterPosition()).getStatus().equals("1")) {
                        Intent intent = new Intent(context, AcceptedSessionDetailActivity.class);
                        intent.putExtra("userObject", new Gson().toJson(todayBookingObjectList.get(getAdapterPosition())));
                        context.startActivity(intent);
                    }
                }else{
                    ((TrainerHomeActivity)context).setUpdatePopUpValue(getAdapterPosition());
                }
            } else {
                TodayBookingObject obj = todayBookingObjectList.get(getAdapterPosition());
                if(obj.getIsAgenda()==0) {
                    Intent intent = new Intent(context, AcceptedSessionDetailActivity.class);
                    intent.putExtra("userObject", new Gson().toJson(todayBookingObjectList.get(getAdapterPosition())));
                    context.startActivity(intent);
                }else if(obj.getIsAgenda()==1){
                    ((TrainerHomeActivity)context).setUpdatePopUpValue(getAdapterPosition());
                }
            }
        }
    }

    public void setData(List<TodayBookingObject> todayBookingObjectList) {
        this.todayBookingObjectList = todayBookingObjectList;
        notifyDataSetChanged();
    }
}

