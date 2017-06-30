package sam.musicplayer.FontUtil;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Administrator on 2016/7/17.
 */
public class FontsFactory {
    private static Typeface mTypefaceRoboto;
    private static Typeface mTypefaceHero;

    public static void createRoboto(Context context) {
        mTypefaceRoboto = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
    }

    public static void createHero(Context context) {
        mTypefaceHero = Typeface.createFromAsset(context.getAssets(), "fonts/hero.ttf");
    }

    public static Typeface getRoboto(Context context) {
        if (mTypefaceRoboto == null) {
            createRoboto(context);
        }
        return mTypefaceRoboto;
    }

    public static Typeface getHero(Context context) {
        if (mTypefaceHero == null) {
            createHero(context);
        }
        return mTypefaceHero;
    }
}
