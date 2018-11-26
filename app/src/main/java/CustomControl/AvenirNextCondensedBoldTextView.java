package CustomControl;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class AvenirNextCondensedBoldTextView extends TextView {
    public AvenirNextCondensedBoldTextView(Context context) {
        super(context);
        setTypeface(context);
    }

    public AvenirNextCondensedBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(context);
    }

    public AvenirNextCondensedBoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(context);
    }
    private void setTypeface(Context context) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "Font/avenir_next_condensed_bold.ttf"));
    }
}
