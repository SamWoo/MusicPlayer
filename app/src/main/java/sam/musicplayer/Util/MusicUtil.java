package sam.musicplayer.Util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import sam.musicplayer.Bean.MusicInfo;
import sam.musicplayer.R;

/**
 * Created by Administrator on 2016/7/3.
 */
public class MusicUtil {
    private static final Uri albumArtUri = Uri.parse("content://media/external/audio/albumart");
    public static boolean Status = false;

    /**
     * 获取手机音乐文件
     *
     * @param context
     * @return
     */
    public static ArrayList<MusicInfo> getAllSongList(Context context) {
        ArrayList<MusicInfo> songList = new ArrayList<MusicInfo>();
        ContentResolver cr = context.getContentResolver();
        String[] str = new String[]{MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.ALBUM_ID, MediaStore.Audio.Media.DATA};
        Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, str, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int j = 0;
            for (int i = 0; i < cursor.getCount(); i++) {
                if (cursor.getString(0).endsWith(".mp3")) {
                    MusicInfo info = new MusicInfo();
                    String musicName = cursor.getString(0).substring(cursor.getString(0).indexOf("-") + 2, cursor.getString(0).lastIndexOf(".mp3"));
                    info.setMusicIndex(j++);
                    info.setMusicName(musicName);
                    info.setMusicAlubm(cursor.getString(1));
                    info.setMusicSinger(cursor.getString(2));
                    info.setMusicTime(cursor.getInt(3));
                    info.setMusicSize(cursor.getInt(4));
                    info.setMusicAlubmId(cursor.getInt(5));
                    info.setMusicPath(cursor.getString(6));
                    songList.add(info);
                }
                cursor.moveToNext();
            }
        }
        return songList;
    }

    /**
     * 格式化歌曲的时间显示格式
     *
     * @param time
     * @return
     */
    public static String formatTime(long time) {
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";
        if (min.length() < 2) {
            min = "0" + time / (1000 * 60) + "";
        } else {
            min = time / (1000 * 60) + "";
        }

        if (sec.length() == 4) {
            sec = "0" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 3) {
            sec = "00" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 2) {
            sec = "000" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 1) {
            sec = "0000" + (time % (1000 * 60)) + "";
        }
        return min + ":" + sec.trim().substring(0, 2);
    }

    /**
     * 获取专辑图片
     */
    private static final Uri artWorkUri = Uri.parse("content://media/external/audio/albumart");
    private static final BitmapFactory.Options options = new BitmapFactory.Options();

    public static Bitmap getArtWork(Context context, long songId, long albumId, boolean allowdefault) {
        if (albumId < 0) {
            if (songId >= 0) {
                Bitmap bm = getArtWorkFromFile(context, songId, -1);
                if (bm != null) {
                    return bm;
                }
            }
            if (allowdefault) {
                return getDefaultArtWork(context);
            }
            return null;
        }

        ContentResolver cr = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(artWorkUri, albumId);
        if (uri != null) {
            InputStream in = null;
            try {
                in = cr.openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                if (bitmap == null) {
                    bitmap = getDefaultArtWork(context);
                }
                return bitmap;
            } catch (FileNotFoundException e) {
                Bitmap bitmap = getArtWorkFromFile(context, songId, albumId);
                if (bitmap != null) {
                    if (bitmap.getConfig() == null) {
                        bitmap = bitmap.copy(Bitmap.Config.RGB_565, false);
                        if (bitmap == null && allowdefault) {
                            return getDefaultArtWork(context);
                        }
                    }
                } else if (allowdefault) {
                    bitmap = getDefaultArtWork(context);
                }
                return bitmap;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static Bitmap getArtWorkFromFile(Context context, long songId, long albumId) {
        Bitmap bitmap = null;
        if (songId < 0 && albumId < 0) {
            throw new IllegalArgumentException("Must specify an album or a song id");
        }
        try {
            if (albumId < 0) {
                Uri uri = Uri.parse("content://media/external/audio/media/" + songId + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bitmap = BitmapFactory.decodeFileDescriptor(fd);
                }
            } else {
                Uri uri = ContentUris.withAppendedId(artWorkUri, albumId);
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bitmap = BitmapFactory.decodeFileDescriptor(fd);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;

    }

    private static Bitmap getDefaultArtWork(Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeStream(context.getResources().openRawResource(R.raw.default_artist), null, options);
    }
}
