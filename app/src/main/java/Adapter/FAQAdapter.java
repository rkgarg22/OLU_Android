package Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elisa.olu.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.ViewHolder> {
    private Activity context;

    public FAQAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public FAQAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_row, parent, false);
        return new FAQAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FAQAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textLayout)
        LinearLayout textLayout;

        @BindView(R.id.fullDescription)
        TextView fullDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.textLayout)
        void setTextLayout() {
            if (fullDescription.getVisibility() == View.VISIBLE) {
                fullDescription.setVisibility(View.GONE);
            } else {
                fullDescription.setVisibility(View.VISIBLE);
            }
        }
    }
}
