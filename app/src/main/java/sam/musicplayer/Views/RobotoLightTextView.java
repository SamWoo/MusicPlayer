package sam.musicplayer.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import sam.musicplayer.FontUtil.FontsFactory;

/**
 * Created by Administrator on 2016/7/17.
 */
public class RobotoLightTextView extends TextView {
    public RobotoLightTextView(Context context) {
        super(context);
        setUseRoboto();
    }

    public RobotoLightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUseRoboto();
    }

    public RobotoLightTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUseRoboto();
    }

    private void setUseRoboto() {
        setTypeface(FontsFactory.getRoboto(getContext()));
    }
}
