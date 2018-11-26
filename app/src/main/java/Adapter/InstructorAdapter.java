package Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elisa.olu.R;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit Chhabra on 4/25/2018.
 */

public class InstructorAdapter extends RecyclerView.Adapter<InstructorAdapter.ViewHolder> {
    private Activity context;

    public InstructorAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public InstructorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row, parent, false);
        return new InstructorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final InstructorAdapter.ViewHolder holder, int position) {
        holder.catImage.setImageResource(R.drawable.cardio_cat_icon_selected);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.catImage)
        SimpleDraweeView catImage;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
