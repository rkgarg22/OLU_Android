package Adapter;

import android.app.Activity;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elisa.olu.MetadosPagosActivity;
import com.elisa.olu.R;

import java.util.ArrayList;

import APIResponse.CardInfo;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private Activity context;
    ArrayList<CardInfo> cardInfoArrayList;

    public CardAdapter(Activity context, ArrayList<CardInfo> cardInfoArrayList) {
        this.context = context;
        this.cardInfoArrayList = cardInfoArrayList;
    }

    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row, parent, false);
        return new CardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CardAdapter.ViewHolder holder, int position) {
        CardInfo cardInfoObj = cardInfoArrayList.get(position);
        holder.card.setText("XXXX XXXX XXXX " + cardInfoArrayList.get(position).getCardNumber());
        if (cardInfoObj.getDefaultCard() == 1) {
            holder.selectedImageView.setImageResource(R.drawable.checked);
        } else {
            holder.selectedImageView.setImageResource(R.drawable.unchecked);
        }
    }

    @Override
    public int getItemCount() {
        return cardInfoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card)
        TextView card;

        @BindView(R.id.delete)
        ImageView delete;

        @BindView(R.id.selectedImageView)
        ImageView selectedImageView;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.delete)
        void setDelete() {
            ((MetadosPagosActivity) context).setOnClick(getAdapterPosition());
        }

        @OnClick(R.id.parentRow)
        void parentSelected(){
            CardInfo cardInfoObj = cardInfoArrayList.get(getAdapterPosition());
            if(cardInfoObj.getDefaultCard()!=1){
                ((MetadosPagosActivity) context).setSelectedCard(getAdapterPosition());
            }
        }
    }
}
