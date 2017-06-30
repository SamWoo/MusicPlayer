package sam.musicplayer.Util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016/7/13.
 */
public class FileCache {
    private File cacheDir;

    public FileCache(Context context){
        //用来缓存图片的路径
        if (android.os.Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)){
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"MusicDownload");
        }else {
            cacheDir=context.getCacheDir();
        }
        if (!cacheDir.exists()){
            cacheDir.mkdirs();
        }
    }

    /**
     * 通过路径获取相应的文件
     * @param url
     * @return
     */
    public File getFile(String url){
        String filename=String.valueOf(url.hashCode());
        File file=new File(cacheDir,filename);
        return file;
    }

    /**
     * 删除缓存文件
     */
    public void clear(){
        File[] files = cacheDir.listFiles();
        if (files==null){
            return;
        }
        for (File f:files){
            f.delete();
        }
    }

}
