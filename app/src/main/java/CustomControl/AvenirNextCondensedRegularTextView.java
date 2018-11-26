package CustomControl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.TextView;

public class AvenirNextCondensedRegularTextView extends TextView {


    public AvenirNextCondensedRegularTextView(Context context) {
        super(context);
        setTypeface(context);
    }

    public AvenirNextCondensedRegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(context);
    }

    public AvenirNextCondensedRegularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(context);
    }
    private void setTypeface(Context context) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "Font/avenir_next_condensed_regular.ttf"));
    }

}
