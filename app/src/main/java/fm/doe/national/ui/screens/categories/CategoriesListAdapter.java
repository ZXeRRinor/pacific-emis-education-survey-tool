package fm.doe.national.ui.screens.categories;

import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.core.utils.ViewUtils;
import fm.doe.national.core.data.model.Category;
import fm.doe.national.core.ui.screens.base.BaseAdapter;

public class CategoriesListAdapter
        extends BaseAdapter<Category> {

    public CategoriesListAdapter(OnItemClickListener<Category> clickListener) {
        super(clickListener);
    }

    @Override
    protected CategoryViewHolder provideViewHolder(ViewGroup parent) {
        return new CategoryViewHolder(parent);
    }

    protected class CategoryViewHolder extends ViewHolder {

        @BindView(R.id.textview_category_name)
        TextView categoryNameTextView;

        @BindView(R.id.textview_progress)
        TextView progressTextView;

        @BindView(R.id.progressbar)
        ProgressBar progressBar;

        CategoryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_category);
        }

        @Override
        public void onBind(Category item) {
            categoryNameTextView.setText(item.getTitle());
            ViewUtils.rebindProgress(item.getProgress(), progressTextView, progressBar);
            itemView.setOnClickListener(this);
        }
    }
}