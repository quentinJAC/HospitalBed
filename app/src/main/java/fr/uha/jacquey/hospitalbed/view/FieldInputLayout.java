package fr.uha.jacquey.hospitalbed.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import fr.uha.hassenforder.teams.R;

public class FieldInputLayout extends LinearLayout {

    private View line;
    private TextView hint;
    private LinearLayout content;
    private TextView error;
    private ImageView errorIcon;
    static private final int paddingLeft = 44;
    static private final int paddingTop = 12;
    static private final int paddingRight = 32;
    static private final int lineThickness = 2;
    private int okColor = Color.BLACK;
    private int errorColor = 0xffb00022;
    private int backgroundColor = 0xffe0e0e0;
    private int focusColor = 0xff00ffff;

    public FieldInputLayout(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public FieldInputLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public FieldInputLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public FieldInputLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init (Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.FieldInputLayout, defStyleAttr, defStyleRes);
        String hintText = a.getString(R.styleable.FieldInputLayout_hint);
        String errorText = a.getString(R.styleable.FieldInputLayout_error);
        errorColor = a.getColor(R.styleable.FieldInputLayout_errorColor, errorColor);
        backgroundColor = a.getColor(R.styleable.FieldInputLayout_backgroundColor, backgroundColor);
        a.recycle();

        setOrientation(LinearLayout.VERTICAL);

        hint = new TextView(context);
        hint.setBackgroundColor(backgroundColor);
        hint.setPadding(paddingLeft,paddingTop,paddingRight,0);
        hint.setText(hintText);
        int textSize = 12;
        hint.setTextSize(textSize);
        addView(hint, -1, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        okColor = hint.getCurrentTextColor();

        content = new LinearLayout(context);
        content.setOrientation(LinearLayout.HORIZONTAL);
        content.setBackgroundColor(backgroundColor);
        content.setPadding(paddingLeft,0,paddingRight,0);
        addView(content, -1, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        errorIcon = new ImageView(context);
        errorIcon.setImageResource(R.drawable.ic_baseline_error_24);
        errorIcon.setVisibility(View.INVISIBLE);
        errorIcon.setColorFilter(errorColor);
        errorIcon.setScaleType(ImageView.ScaleType.FIT_END);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0);
        params.gravity = Gravity.CENTER;
        content.addView(errorIcon, -1, params);

        line = new View (context);
        addView(line, -1, new LayoutParams(LayoutParams.MATCH_PARENT, lineThickness));

        error = new TextView(context);
        error.setText(errorText);
        error.setTextSize(textSize);
        error.setTextColor(errorColor);
        error.setPadding(paddingLeft,0,paddingRight,0);
        addView(error, -1, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void addView(@NonNull View child, int index, @NonNull final ViewGroup.LayoutParams params) {
        if (child == line || child == hint || child == error || child == content) {
            super.addView(child, index, params);
        } else {
            content.addView(child, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }
    }

    public void setHint(String hintText) {
        this.hint.setText(hintText);
    }

    public void setHint(@StringRes int hintId) {
        this.hint.setText(hintId);
    }

    private void applyError (boolean hasError) {
        if (hasError) {
            this.error.setVisibility(View.VISIBLE);
            this.errorIcon.setVisibility(View.VISIBLE);
            this.hint.setTextColor(errorColor);
            this.line.setBackgroundColor(errorColor);
        } else {
            this.error.setVisibility(View.GONE);
            this.errorIcon.setVisibility(View.INVISIBLE);
            this.hint.setTextColor(okColor);
            this.line.setBackgroundColor(okColor);
        }
    }

    public void setError(String errorText) {
        if (errorText == null) {
            this.error.setText("");
        } else {
            this.error.setText(errorText);
        }
        applyError(errorText != null);
    }

    public void setError(@StringRes int errorId) {
        if (errorId == 0) {
            this.error.setText("");
        } else {
            this.error.setText(errorId);
        }
        applyError(errorId != 0);
    }

}
