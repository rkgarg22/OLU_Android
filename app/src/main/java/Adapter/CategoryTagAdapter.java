package Adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elisa.olu.InstructorProfileActivity;
import com.elisa.olu.R;

import java.util.List;

import ApiObject.CategoriesListObject;
import ApiObject.CategoriesObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryTagAdapter extends RecyclerView.Adapter<CategoryTagAdapter.ViewHolder> {
    private Activity context;
    List<CategoriesObject> categoryList;
    int lastPosition = 0;

    public CategoryTagAdapter(Activity context, List<CategoriesObject> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public CategoryTagAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_tag_row, parent, false);
        return new CategoryTagAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CategoryTagAdapter.ViewHolder holder, int position) {
        CategoriesObject categoriesObject = categoryList.get(position);
        holder.catergoryName.setText(categoriesObject.getCategoryName());
        if (categoriesObject.getIsSelected() != null) {
            if (categoriesObject.getIsSelected().equals("1")) {
                holder.catergoryName.setBackground(ContextCompat.getDrawable(context, R.drawable.circle_dark_grey));
            } else {
                holder.catergoryName.setBackground(ContextCompat.getDrawable(context, R.drawable.circle_grey));
            }
        } else {
            holder.catergoryName.setBackground(ContextCompat.getDrawable(context, R.drawable.circle_grey));
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.categoryName)
        TextView catergoryName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.categoryName)
        void setCatergoryName() {
            int pos = getAdapterPosition();
            categoryList.get(lastPosition).setIsSelected("0");
            categoryList.get(pos).setIsSelected("1");
            ((InstructorProfileActivity)context).setCategory(categoryList.get(pos));
            notifyDataSetChanged();
            lastPosition = pos;
        }
    }
}
