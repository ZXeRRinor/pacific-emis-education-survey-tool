package fm.doe.national.accreditation.ui.custom_views.bottom_nav;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import fm.doe.national.accreditation.R;

public class BottomNavigatorView extends RelativeLayout implements View.OnClickListener {

    private TextView hintTextView;
    private Button prevButton;
    private Button nextButton;

    @Nullable
    private Listener listener;

    public BottomNavigatorView(Context context) {
        this(context, null);
    }

    public BottomNavigatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public BottomNavigatorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inflate(context, R.layout.view_bottom_navigator, this);
        bindViews();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BottomNavigatorView, defStyleAttr, defStyleRes);
        setPrevText(a.getString(R.styleable.BottomNavigatorView_prevText));
        setNextText(a.getString(R.styleable.BottomNavigatorView_nextText));
        setHintText(a.getString(R.styleable.BottomNavigatorView_hintText));
        a.recycle();

        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
    }

    private void bindViews() {
        hintTextView = findViewById(R.id.textview_complete_hint);
        prevButton = findViewById(R.id.button_prev);
        nextButton = findViewById(R.id.button_next);
    }

    public void setPrevText(@Nullable String text) {
        updateView(prevButton, text);
    }

    public void setNextText(@Nullable String text) {
        updateView(nextButton, text);
    }

    public void setHintText(@Nullable String text) {
        updateView(hintTextView, text, View.INVISIBLE);
    }

    private void updateView(TextView view, @Nullable String text) {
        updateView(view, text, View.GONE);
    }

    private void updateView(TextView view, @Nullable String text, int disappearVisibility) {
        view.setText(text);

        if (text == null) {
            view.setVisibility(disappearVisibility);
        }
    }

    public void setListener(@Nullable Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener == null) {
            return;
        }

        int id = v.getId();

        if (id == R.id.button_prev) {
            listener.onPrevPressed();
        } else if (id == R.id.button_next) {
            listener.onNextPressed();
        }
    }


    public interface Listener {
        void onPrevPressed();
        void onNextPressed();
    }
}
