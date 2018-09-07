package fm.doe.national.ui.screens.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.omega_r.libs.omegatypes.Text;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.doe.national.R;

public abstract class BaseActivity extends MvpAppCompatActivity implements BaseView {

    @Nullable
    @BindView(R.id.imageview_toolbar_clock)
    protected ImageView clockIconImageView;

    @Nullable
    @BindView(R.id.textview_toolbar_title)
    protected TextView titleTextView;

    @Nullable
    @BindView(R.id.textview_toolbar_year)
    protected TextView yearTextView;

    @Nullable
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @Nullable
    private ProgressDialog progressDialog = null;
    private int progressDialogsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getContentView());
        ButterKnife.bind(this);
        initToolbar();
    }

    @LayoutRes
    protected abstract int getContentView();

    protected void initToolbar() {
        if (toolbar != null && getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP |
                                ActionBar.DISPLAY_SHOW_HOME |
                                ActionBar.DISPLAY_SHOW_CUSTOM);
            }
            toolbar.setNavigationOnClickListener(v -> onHomePressed());
        }
    }

    public void onHomePressed() {
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (upIntent != null) {
            upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                TaskStackBuilder.create(this)
                        .addNextIntentWithParentStack(upIntent)
                        .startActivities();
            } else {
                NavUtils.navigateUpTo(this, upIntent);
            }
        } else {
            finish();
        }
    }

    @Override
    public void showToast(Text text) {
        Toast.makeText(this, text.getString(getResources()), Toast.LENGTH_SHORT).show();
    }

    protected void hideKeyboard() {
        hideKeyboard(getWindow().getDecorView());
    }

    protected void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    @Override
    public void showWarning(Text title, Text message) {
        new AlertDialog.Builder(this)
                .setTitle(title.getString(getResources()))
                .setMessage(message.getString(getResources()))
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();
    }

    @SuppressWarnings("unchecked")
    protected <T extends Serializable> T getSerializableExtra(String extraName) {
        try {
            return (T) getIntent().getSerializableExtra(extraName);
        } catch (NullPointerException | ClassCastException ex) {
            throw new RuntimeException("Unable to obtain serializable argument");
        }
    }

    @Override
    public void showWaiting() {
        progressDialogsCount++;
        if (progressDialogsCount == 1) {
            progressDialog = createProgressDialog(Text.empty());
        }
    }

    @Override
    public void hideWaiting() {
        if (progressDialogsCount == 0) {
            return;
        }

        if (progressDialogsCount == 1 && progressDialog != null) {
            progressDialog.dismiss();
        }

        progressDialogsCount--;
    }

    protected void setToolbarMode(ToolbarDisplaying mode) {
        if (titleTextView == null) return;
        switch (mode) {
            case PRIMARY:
                titleTextView.setActivated(false);
                break;
            case SECONDARY:
                titleTextView.setActivated(true);
                break;
        }
    }

    public void setToolbarYear(int year) {
        if (clockIconImageView == null || yearTextView == null) return;
        yearTextView.setText(String.valueOf(year));
        clockIconImageView.setVisibility(View.VISIBLE);
        yearTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setTitle(int resId) {
        if (titleTextView == null) return;
        titleTextView.setText(resId);
    }

    @Override
    public void setTitle(CharSequence title) {
        if (titleTextView == null) return;
        titleTextView.setText(title);
    }

    @NonNull
    private ProgressDialog createProgressDialog(Text text) {
        ProgressDialog dialog = new ProgressDialog(this, R.style.Theme_AppCompat_Dialog);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage(text.getString(getResources()));
        dialog.show();
        return dialog;
    }

    protected enum ToolbarDisplaying {
        PRIMARY, SECONDARY
    }
}
