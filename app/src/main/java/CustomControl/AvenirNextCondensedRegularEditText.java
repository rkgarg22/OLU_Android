package CustomControl;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;


public class AvenirNextCondensedRegularEditText extends EditText {
    public AvenirNextCondensedRegularEditText(Context context) {
        super(context);
        setTypeface(context);
    }

    public AvenirNextCondensedRegularEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(context);
    }

    public AvenirNextCondensedRegularEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(context);
    }

    private void setTypeface(Context context) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "Font/avenir_next_condensed_regular.ttf"));
    }
}
