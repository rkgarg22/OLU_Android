package Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elisa.olu.CategoriesActivity;
import com.elisa.olu.R;
import com.elisa.olu.TrainerCategoryActivity;

import java.util.List;

import ApiObject.CategoriesListObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {
    private Activity context;
    List<CategoriesListObject> categoryList;

    public CategoryListAdapter(Activity context, List<CategoriesListObject> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public CategoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_row, parent, false);
        return new CategoryListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CategoryListAdapter.ViewHolder holder, int position) {
        holder.catergoryName.setText(categoryList.get(position).getCatergoryName());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.catergoryName)
        TextView catergoryName;

        @BindView(R.id.deleteCatergory)
        ImageView deleteCatergory;

        @BindView(R.id.parentRow)
        RelativeLayout parentRow;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.deleteCatergory)
        void setDeleteCatergory() {
            categoryList.remove(getAdapterPosition());
            notifyDataSetChanged();
        }

        @OnClick(R.id.parentRow)
        void rowClick() {
            ((TrainerCategoryActivity) context).editCategoryInfo(getAdapterPosition());
        }
    }
}
