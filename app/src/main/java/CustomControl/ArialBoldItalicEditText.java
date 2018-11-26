package CustomControl;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;


public class ArialBoldItalicEditText extends EditText {
    public ArialBoldItalicEditText(Context context) {
        super(context);
        setTypeface(context);
    }

    public ArialBoldItalicEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(context);
    }

    public ArialBoldItalicEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(context);
    }

    private void setTypeface(Context context) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "Font/arial_bold_italic.ttf"));
    }
}
