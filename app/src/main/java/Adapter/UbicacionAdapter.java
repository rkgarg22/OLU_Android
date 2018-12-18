package Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tucan.olu.R;
import com.tucan.olu.UbicacionActivity;

import java.util.List;

import ApiObject.UserListObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ankit Chhabra on 4/29/2018.
 */

public class UbicacionAdapter extends RecyclerView.Adapter<UbicacionAdapter.ViewHolder> {
    private Activity context;
    List<UserListObject> userListObjectList;

    public UbicacionAdapter(Activity context, List<UserListObject> userListObjectList) {
        this.context = context;
        this.userListObjectList = userListObjectList;
    }

    @Override
    public UbicacionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ubicacion_row, parent, false);
        return new UbicacionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UbicacionAdapter.ViewHolder holder, int position) {
        UserListObject userListObject = userListObjectList.get(position);
        holder.name.setText(userListObject.getFirstName() + " " + userListObject.getLastName());
        holder.userInfo.setText(userListObject.getCategoryName());
        holder.count.setText(String.valueOf(position + 1));
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

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.count)
        TextView count;

        @BindView(R.id.userInfo)
        TextView userInfo;

        @BindView(R.id.ratingBar)
        RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.parentRow,R.id.onDemand})
        void parentRow() {
            ((UbicacionActivity) context).setOnClick(getAdapterPosition());

        }
    }
}
