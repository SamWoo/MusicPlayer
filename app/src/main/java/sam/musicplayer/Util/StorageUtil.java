package sam.musicplayer.Util;

import android.content.Context;
import android.content.SharedPreferences;

import sam.musicplayer.Interface.AppConstants;

/**
 * Created by Administrator on 2016/8/12.
 */
public class StorageUtil implements AppConstants {
    private SharedPreferences sp;
    private SharedPreferences.Editor mEditor;

    public StorageUtil(Context context) {
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_WORLD_WRITEABLE);
        mEditor = sp.edit();
    }

    /**
     * 保存背景图片的地址
     *
     * @param path
     */
    public void savePath(String path) {
        mEditor.putString(SP_BG_PATH, path);
        mEditor.commit();
    }

    public String getPath() {
        return sp.getString(SP_BG_PATH, null);
    }
}
