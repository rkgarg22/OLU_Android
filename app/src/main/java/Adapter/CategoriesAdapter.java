package Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tucan.olu.CategoriesActivity;
import com.tucan.olu.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    private Activity context;
    String[] categoryList;

    public CategoriesAdapter(Activity context, String[] categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (categoryList[position].equals("0")) {
            holder.catImage.setBackgroundResource(R.drawable.grey_circle);
        } else {
            holder.catImage.setBackgroundResource(R.drawable.blue_circle);
        }
        switch (position) {
            case 0:
                holder.catImage.setImageResource(R.drawable.kickboxing);
                break;
            case 1:
                //holder.catImage.setBackgroundResource(R.drawable.grey_circle);
                holder.catImage.setImageResource(R.drawable.cardio_crossfit);
                break;
            case 2:
                //    holder.catImage.setBackgroundResource(R.drawable.grey_circle);
                holder.catImage.setImageResource(R.drawable.stretching);
                break;
            case 3:
                //  holder.catImage.setBackgroundResource(R.drawable.grey_circle);
                holder.catImage.setImageResource(R.drawable.yoga);
                break;
            case 4:
                //holder.catImage.setBackgroundResource(R.drawable.grey_circle);
                holder.catImage.setImageResource(R.drawable.pilates);
                break;
            case 5:
                //holder.catImage.setBackgroundResource(R.drawable.grey_circle);
                holder.catImage.setImageResource(R.drawable.danza_fit);
                break;
            case 6:
                //holder.catImage.setBackgroundResource(R.drawable.grey_circle);
                holder.catImage.setImageResource(R.drawable.gimnasia);
                break;
            case 7:
                //holder.catImage.setBackgroundResource(R.drawable.grey_circle);
                holder.catImage.setImageResource(R.drawable.fisioterapia);
                break;
            case 8:
                //holder.catImage.setBackgroundResource(R.drawable.grey_circle);
                holder.catImage.setImageResource(R.drawable.masajes_deportivos);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 9;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.catImage)
        ImageView catImage;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.catImage)
        void setCatImage() {
            ((CategoriesActivity) context).setOnselected(getAdapterPosition());
        }
    }
}
