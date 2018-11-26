package Adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.elisa.olu.R;
import com.elisa.olu.SearchActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import ApiObject.UserListObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;


public class TrainersAdapter extends RecyclerView.Adapter<TrainersAdapter.ViewHolder> {
    private Activity context;
    List<UserListObject> userListObjectList;
    String bookingType;

    public TrainersAdapter(Activity context, List<UserListObject> userListObjectList, String bookingType) {
        this.context = context;
        this.userListObjectList = userListObjectList;
        this.bookingType = bookingType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.instructor_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        UserListObject userListObject = userListObjectList.get(position);
        holder.userImage.setImageURI(Uri.parse(userListObject.getUserImageUrl()));
        holder.name.setText(userListObject.getFirstName() + " " + userListObject.getLastName());
        holder.userInfo.setText(userListObject.getCategoryName());
        if(userListObject.getPrice().isEmpty()){
            userListObject.setPrice("0");
        }
        holder.priceTextView.setText(AppCommon.getInstance(context).getPriceInFormat(userListObject.getPrice()));
        try {
            holder.ratingBar.setRating(Float.parseFloat(userListObject.getReviews()));
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return userListObjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.userImage)
        SimpleDraweeView userImage;

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.userInfo)
        TextView userInfo;

        @BindView(R.id.priceTextView)
        TextView priceTextView;

        @BindView(R.id.ratingBar)
        RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.onDemand)
        void parentRow() {
            ((SearchActivity) context).setOnClick(getAdapterPosition());
        }

        @OnClick(R.id.plusIcon)
        void plusIcon() {
            ((SearchActivity) context).setOnClickPlusIcon(getAdapterPosition());
        }
    }
}
