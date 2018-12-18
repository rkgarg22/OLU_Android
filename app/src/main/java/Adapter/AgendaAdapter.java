package Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tucan.olu.R;

import butterknife.ButterKnife;

/**
 * Created by Ankit Chhabra on 4/29/2018.
 */

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.ViewHolder> {
    private Activity context;

    public AgendaAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public AgendaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.agenda_row, parent, false);
        return new AgendaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AgendaAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
