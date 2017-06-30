package sam.musicplayer.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.IOException;
import java.util.ArrayList;

import sam.musicplayer.IServicePlayer;
import sam.musicplayer.Bean.MusicInfo;
import sam.musicplayer.Activity.PlayActivity;
import sam.musicplayer.R;
import sam.musicplayer.Util.MusicUtil;

/**
 * Created by Administrator on 2015/10/31.
 */
public class MusicService extends Service {
    @Nullable
    public static MediaPlayer mediaPlayer = null;
    private ArrayList<MusicInfo> musicList;
    private int mCurrentPosition = -1;
    private String path;
    private final static String AUTO_NEXT_SONG = "sam.musicplayer.AUTO_NEXT_SONG";
    private final static String NOTIFICATION_PREVIOUS_MUSIC = "sam.musicplayer.NOTIFICATION_PREVIOUS_MUSIC";
    private final static String NOTIFICATION_NEXT_MUSIC = "sam.musicplayer.NOTIFICATION_NEXT_MUSIC";
    private final static String NOTIFICATION_PAUSE_MUSIC = "sam.musicplayer.NOTIFICATION_PAUSE_MUSIC";
    private final static String NOTIFICATION_EXIT_MUSIC = "sam.musicplayer.NOTIFICATION_EXIT_MUSIC";
    private final static String PLAY_MUSIC_ACTIVITY_PAUSE_MUSIC = "sam.musicplayer.PLAY_MUSIC_ACTIVITY_PAUSE_MUSIC";
    private final static String PLAY_ACTIVITY_PAUSE_MUSIC = "sam.musicplayer.PLAY_ACTIVITY_PAUSE_MUSIC";

    private Notification notification = null;
    private NotificationManager manager = null;
    private String notification_msg;
    private boolean isPause;
    private Bitmap bitmap = null;
    private String songName;
    private String singerName;
    private int albumId;
    private int songId;

    private RemoteViews contentViews = null;

    IServicePlayer.Stub stub = new IServicePlayer.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void play(int position) throws RemoteException {
            if (position != mCurrentPosition) {
                mCurrentPosition = position;
                //更新通知栏
                setRemoteViews();
                path = musicList.get(mCurrentPosition).getMusicPath();
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(path);
//                    mediaPlayer.setLooping(true);//循环播放
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d("Sam", "Position==================--->" + position + "=========mCurrent====================" + mCurrentPosition);
                Log.d("Sam", "Path---------------------------------------------->" + path);
            }
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Intent intent = new Intent();
                    intent.setAction(AUTO_NEXT_SONG);
                    sendBroadcast(intent);
                    Log.d("Sam", "***************播放完了，我发广播了！！！！");
                }
            });

        }

        @Override
        public void pause() throws RemoteException {
            mediaPlayer.pause();
            isPause = true;
        }

        @Override
        public void stop() throws RemoteException {
            mediaPlayer.stop();
        }

        @Override
        public int getDuration() throws RemoteException {
            return mediaPlayer.getDuration();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return mediaPlayer.getCurrentPosition();
        }

        @Override
        public void seekTo(int current) throws RemoteException {
            mediaPlayer.seekTo(current);
        }

        @Override
        public boolean setLoop(boolean loop) throws RemoteException {
            return false;
        }
    };

    @Override
    public void onCreate() {
        musicList = MusicUtil.getAllSongList(MusicService.this);
        mediaPlayer = new MediaPlayer();
        //注册监听广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(NOTIFICATION_EXIT_MUSIC);
        filter.addAction(NOTIFICATION_PAUSE_MUSIC);
        filter.addAction(NOTIFICATION_PREVIOUS_MUSIC);
        filter.addAction(NOTIFICATION_NEXT_MUSIC);
        filter.addAction(PLAY_ACTIVITY_PAUSE_MUSIC);
        filter.addAction(PLAY_MUSIC_ACTIVITY_PAUSE_MUSIC);
        registerReceiver(NotificationReciver, filter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //显示通知栏
        initNotification();
        return 0;
    }

    /**
     * 通知栏显示当前播放的歌曲信息
     */
    @SuppressLint("WrongConstant")
    private void initNotification() {
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        notification = builder.build();
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notification.icon = R.drawable.logo;
        contentViews = new RemoteViews(getPackageName(), R.layout.notification_layout);

        notification.contentView = contentViews;
        notification.bigContentView = contentViews;

        Intent notificationIntent = new Intent(this, PlayActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.contentIntent = contentIntent;
        //上一曲
        Intent previousButtonIntent = new Intent(NOTIFICATION_PREVIOUS_MUSIC);
        PendingIntent pendPreviousButtonIntent = PendingIntent.getBroadcast(this, 0, previousButtonIntent, 0);
        contentViews.setOnClickPendingIntent(R.id.notification_previous_song_button, pendPreviousButtonIntent);
        //下一曲
        Intent nextButtonIntent = new Intent(NOTIFICATION_NEXT_MUSIC);
        PendingIntent pendNextButtonIntent = PendingIntent.getBroadcast(this, 0, nextButtonIntent, 0);
        contentViews.setOnClickPendingIntent(R.id.notification_next_song_button, pendNextButtonIntent);
        //暂停播放
        Intent playButtonIntent = new Intent(NOTIFICATION_PAUSE_MUSIC);
        PendingIntent pendPlayButtonIntent = PendingIntent.getBroadcast(this, 0, playButtonIntent, 0);
        contentViews.setOnClickPendingIntent(R.id.notification_play_button, pendPlayButtonIntent);
        //退出程序
        Intent exitButtonIntent = new Intent(NOTIFICATION_EXIT_MUSIC);
        PendingIntent pendExitButtonIntent = PendingIntent.getBroadcast(this, 0, exitButtonIntent, 0);
        contentViews.setOnClickPendingIntent(R.id.notification_exit_button, pendExitButtonIntent);

        setRemoteViews();
    }


    public void setRemoteViews() {
        if (mCurrentPosition == -1) {
            songId = musicList.get(0).getMusicIndex();
            albumId = musicList.get(0).getMusicAlubmId();
            songName = musicList.get(0).getMusicName();
            singerName = musicList.get(0).getMusicSinger();
        } else {
            songId = musicList.get(mCurrentPosition).getMusicIndex();
            albumId = musicList.get(mCurrentPosition).getMusicAlubmId();
            songName = musicList.get(mCurrentPosition).getMusicName();
            singerName = musicList.get(mCurrentPosition).getMusicSinger();
        }
        bitmap = MusicUtil.getArtWork(MusicService.this, songId, albumId, true);
        contentViews.setImageViewBitmap(R.id.notification_artist_image, bitmap);
        contentViews.setTextViewText(R.id.notification_music_title, songName);
        contentViews.setTextViewText(R.id.notification_music_Artist, singerName);
        if (MusicUtil.Status) {
            contentViews.setImageViewResource(R.id.notification_play_button, R.drawable.notification_play);
        } else {
            contentViews.setImageViewResource(R.id.notification_play_button, R.drawable.notification_pause);
        }
        manager.notify(0, notification);
    }

    /**
     * 通知栏监听广播
     */
    BroadcastReceiver NotificationReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            notification_msg = intent.getAction();
            switch (notification_msg) {
                case NOTIFICATION_PAUSE_MUSIC:
                case PLAY_ACTIVITY_PAUSE_MUSIC:
                case PLAY_MUSIC_ACTIVITY_PAUSE_MUSIC:
                    Log.e("Sam", "Lalalalalalalalala====我在这里了" + MusicUtil.Status);
                    if (MusicUtil.Status) {
                        contentViews.setImageViewResource(R.id.notification_play_button, R.drawable.notification_play);
                    } else {
                        contentViews.setImageViewResource(R.id.notification_play_button, R.drawable.notification_pause);
                    }
                    manager.notify(0, notification);
                    break;
                case NOTIFICATION_NEXT_MUSIC:
                case NOTIFICATION_PREVIOUS_MUSIC:
                    break;
                case NOTIFICATION_EXIT_MUSIC:
                    Log.e("Sam", "Lalalalalalalalala====我要取消通知栏咯！！！！");
                    manager.cancel(0);
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (contentViews != null)
            manager.cancel(0);
        if (NotificationReciver != null)
            unregisterReceiver(NotificationReciver);
    }
}
