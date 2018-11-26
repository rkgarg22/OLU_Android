package Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elisa.olu.ComprarPlanActivity;
import com.elisa.olu.R;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ankit Chhabra on 4/26/2018.
 */

public class ComprarAdapter extends RecyclerView.Adapter<ComprarAdapter.ViewHolder> {
    private Activity context;

    public ComprarAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public ComprarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comprar_row, parent, false);
        return new ComprarAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ComprarAdapter.ViewHolder holder, int position) {
        switch (position) {
            case 0:
                holder.firstText.setText("Paga $ 300.000 y disfruta");
                holder.endText.setText("$315.000");
                holder.moneyTextView.setText("300");
                holder.messesTextView.setText("OLU. Basic");
                break;
            case 1:
                holder.firstText.setText("Paga $ 600.000 y disfruta");
                holder.endText.setText("$645.000");
                holder.moneyTextView.setText("600");
                holder.messesTextView.setText("OLU. Standard");
                break;
            case 2:
                holder.firstText.setText("Paga $ 900.000 y disfruta");
                holder.endText.setText("$990.000");
                holder.moneyTextView.setText("900");
                holder.messesTextView.setText("OLU. Premium");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.btn)
        LinearLayout btn;

        @BindView(R.id.firstText)
        TextView firstText;

        @BindView(R.id.endText)
        TextView endText;

        @BindView(R.id.moneyTextView)
        TextView moneyTextView;

        @BindView(R.id.messesTextView)
        TextView messesTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.parentRow, R.id.btn})
        void parentRow() {
            ((ComprarPlanActivity) context).setOnClick(getAdapterPosition() + 1);
        }
    }
}

