package sam.musicplayer.Util;

import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/13.
 */
public class MemoryCache {
    private Map<String,SoftReference<Bitmap>> cache= Collections.synchronizedMap(new HashMap<String, SoftReference<Bitmap>>());//软引用

    public Bitmap get(String id){
        if (!cache.containsKey(id)){
            return null;
        }
        SoftReference<Bitmap> ref=cache.get(id);
        return ref.get();
    }

    public void put(String id,Bitmap bitmap){
        cache.put(id, new SoftReference<Bitmap>(bitmap));
    }

    public void clear(){
        cache.clear();
    }
}
