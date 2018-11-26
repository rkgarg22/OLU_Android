package Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elisa.olu.R;

import butterknife.ButterKnife;

public class ConfimarAdapter extends RecyclerView.Adapter<ConfimarAdapter.ViewHolder> {
    private Activity context;

    public ConfimarAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public ConfimarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirm_row, parent, false);
        return new ConfimarAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ConfimarAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
