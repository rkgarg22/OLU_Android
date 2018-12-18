package Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tucan.olu.IngresaActivity;
import com.tucan.olu.R;

import java.util.List;

import ApiObject.SavedLocation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ankit Chhabra on 9/6/2018.
 */

public class SavedLocationAdapter extends RecyclerView.Adapter<SavedLocationAdapter.ViewHolder> {
    private Activity context;
    List<SavedLocation> savedLocationList;

    public SavedLocationAdapter(Activity context, List<SavedLocation> savedLocationList) {
        this.context = context;
        this.savedLocationList = savedLocationList;
    }

    @Override
    public SavedLocationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_row, parent, false);
        return new SavedLocationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SavedLocationAdapter.ViewHolder holder, int position) {
        holder.address.setText(savedLocationList.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return savedLocationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textLayout)
        LinearLayout textLayout;

        @BindView(R.id.address)
        TextView address;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.textLayout)
        void setTextLayout() {
            ((IngresaActivity)context).setOnClick(getAdapterPosition());
        }
    }
}