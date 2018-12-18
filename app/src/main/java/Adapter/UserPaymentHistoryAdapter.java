package Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tucan.olu.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ApiObject.UserPaymentHistoryObject;
import CustomControl.AvenirNextCondensedMediumTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import infrastructure.AppCommon;

/**
 * Created by Ankit Chhabra on 4/29/2018.
 */

public class UserPaymentHistoryAdapter extends RecyclerView.Adapter<UserPaymentHistoryAdapter.ViewHolder> {
    private Activity context;
    List<UserPaymentHistoryObject> paymentHistoryObjectList;


    public UserPaymentHistoryAdapter(Activity context, List<UserPaymentHistoryObject> paymentHistoryObjectList) {
        this.context = context;
        this.paymentHistoryObjectList = paymentHistoryObjectList;

    }

    @Override
    public UserPaymentHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_payment_history_row, parent, false);
        return new UserPaymentHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserPaymentHistoryAdapter.ViewHolder holder, int position) {
        UserPaymentHistoryObject paymentHistoryObject = paymentHistoryObjectList.get(position);
        holder.date.setText(paymentHistoryObject.getDate());
        holder.categoryName.setText(paymentHistoryObject.getCategory());
        holder.amount.setText(AppCommon.getInstance(context).getPriceInFormat(paymentHistoryObject.getAmount()));
        holder.timeTextView.setText(getTimeInForamt(paymentHistoryObject.getTime()));
        holder.referenceIdTextView.setText(paymentHistoryObject.getReference());
        holder.referenceIdTextView.setVisibility(View.VISIBLE);

        if(paymentHistoryObject.getPaymentStatus().equals("1")){
            holder.statusTextView.setText("APROBADO");
        }else if(paymentHistoryObject.getPaymentStatus().equals("0")){
            holder.statusTextView.setText("RECHAZADO");
        }else{
            holder.statusTextView.setText("PENDIENTE");
        }
    }

    public String getDateInFormat(String date) {
        String formatterString = "";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
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
        return paymentHistoryObjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date)
        TextView date;

        @BindView(R.id.categoryName)
        TextView categoryName;

        @BindView(R.id.amount)
        TextView amount;

        @BindView(R.id.timeTextView)
        AvenirNextCondensedMediumTextView timeTextView;

        @BindView(R.id.referenceIdTextView)
        AvenirNextCondensedMediumTextView referenceIdTextView;

        @BindView(R.id.statusTextView)
        TextView statusTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setData(List<UserPaymentHistoryObject> paymentHistoryObjectList) {
        this.paymentHistoryObjectList = paymentHistoryObjectList;
        notifyDataSetChanged();
    }
}

