package sam.musicplayer.Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2016/7/13.
 */
public class StreamUtil {
    /**
     * 读取数据流并返回
     * @param is
     * @return
     * @throws Exception
     */
    public static byte[] readStream(InputStream is) throws Exception{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len=-1;
        while((len=is.read(buffer))!=-1){
            bos.write(buffer,0,len);
        }
        bos.close();
        is.close();
        return bos.toByteArray();
    }

    /**
     * 复制文件到其他地方
     * @param is
     * @param os
     */
    public  static void copyStream(InputStream is ,OutputStream os){
        final int buffer_size=1024;
        byte[] bytes = new byte[buffer_size];
        for (;;){
            try {
                int count = is.read(bytes,0,buffer_size);
                if (count==-1) break;
                os.write(bytes,0,count);
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
