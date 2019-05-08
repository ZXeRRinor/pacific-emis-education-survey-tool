package fm.doe.national.ui.screens.standards;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.omegar.mvp.presenter.InjectPresenter;
import com.omegar.mvp.presenter.ProvidePresenter;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.model.Standard;
import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.base.BaseAdapter;
import fm.doe.national.ui.screens.criterias.CriteriasActivity;
import fm.doe.national.utils.ViewUtils;

public class StandardsActivity extends BaseActivity implements StandardsView, BaseAdapter.OnItemClickListener<Standard> {
    private static final String EXTRA_PASSING_ID = "EXTRA_PASSING_ID";
    private static final String EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID";

    private final StandardsListAdapter standardAdapter = new StandardsListAdapter(this);

    @BindView(R.id.recyclerview_standards)
    RecyclerView standardsRecyclerView;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @BindView(R.id.textview_progress)
    TextView progressTextView;

    @InjectPresenter
    StandardsPresenter presenter;

    public static Intent createIntent(Context context, long passingId, long categoryId) {
        return new Intent(context, StandardsActivity.class)
                .putExtra(EXTRA_PASSING_ID, passingId)
                .putExtra(EXTRA_CATEGORY_ID, categoryId);
    }

    @ProvidePresenter
    StandardsPresenter providePresenter() {
        Intent intent = getIntent();
        return new StandardsPresenter(
                intent.getLongExtra(EXTRA_PASSING_ID, -1),
                intent.getLongExtra(EXTRA_CATEGORY_ID, -1));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_standards;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        standardsRecyclerView.setAdapter(standardAdapter);
    }

    @Override
    public void setGlobalProgress(int completed, int total) {
        ViewUtils.rebindProgress(
                total,
                completed,
                getString(R.string.criteria_progress),
                progressTextView, progressBar);
    }

    @Override
    public void setSurveyDate(Date date) {
        setToolbarDate(date);
    }

    @Override
    public void setCategoryName(String schoolName) {
        setTitle(schoolName);
    }

    @Override
    public void navigateToCriteriasScreen(long passingId, long categoryId, long standardId) {
        startActivity(CriteriasActivity.createIntent(this, passingId, categoryId, standardId));
    }

    @Override
    public void showStandards(List<Standard> standards) {
        standardAdapter.setItems(standards);
    }

    @Override
    public void onItemClick(Standard item) {
        presenter.onStandardClicked(item);
    }
}
