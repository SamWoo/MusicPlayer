package sam.musicplayer.Activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import sam.musicplayer.Bean.MusicInfo;
import sam.musicplayer.IServicePlayer;
import sam.musicplayer.R;
import sam.musicplayer.Service.MusicService;
import sam.musicplayer.Util.ImageUtil;
import sam.musicplayer.Util.MusicUtil;
import sam.musicplayer.Util.StorageUtil;
import sam.musicplayer.Views.CircleImageViews;

public class PlayMusicActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private ImageButton imagePre;
    private ImageButton imagePlay;
    private ImageButton imageNext;
    private SeekBar musicSeekBar;
    private CircleImageViews singerView;
    private TextView songName;
    private TextView singerName;
    private TextView mCurrentProgress;//当前进度
    private TextView mFinalProgress;//总进度
    private RelativeLayout bgLayout;
    private String musicTime;
    private int songId;
    private int albumId;
    private Bitmap bitmap = null;

    private IServicePlayer iPlayer;
    private int mCurrentPosition = -1;
    private MusicInfo musicInfo = null;
    private ArrayList<MusicInfo> musicList = null;
    private PlayMusicReceiver receiver;
    private final static String AUTO_NEXT_SONG = "sam.musicplayer.AUTO_NEXT_SONG";
    private final static String PLAY_NEXT_SONG = "sam.musicplayer.PLAY_NEXT_SONG";
    private final static String PLAY_PRE_SONG = "sam.musicplayer.PLAY_PRE_SONG";
    private final static String PLAY_STATUS = "sam.musicplayer.PLAY_STATUS";
    private final static String PAUSE_STATUS = "sam.musicplayer.PAUSE_STATUS";
    private final static String PLAY_MUSIC_ACTIVITY_PAUSE_MUSIC = "sam.musicplayer.PLAY_MUSIC_ACTIVITY_PAUSE_MUSIC";
    private final static String NOTIFICATION_PAUSE_MUSIC = "sam.musicplayer.NOTIFICATION_PAUSE_MUSIC";
    private final static String NOTIFICATION_EXIT_MUSIC = "sam.musicplayer.NOTIFICATION_EXIT_MUSIC";
    private final static String NOTIFICATION_PREVIOUS_MUSIC = "sam.musicplayer.NOTIFICATION_PREVIOUS_MUSIC";
    private final static String NOTIFICATION_NEXT_MUSIC = "sam.musicplayer.NOTIFICATION_NEXT_MUSIC";
    public static final String BROADCAST_CHANGEBACKGROUD = "sam.musicplayer.changebackgroud";
    private boolean isPause;
    private RotateAnimation ra = null;
    private Intent intentService = null;
    private AssetManager am;
    private StorageUtil storageUtil;
    private boolean notification_flag = true;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private Runnable updateThread = new Runnable() {
        @Override
        public void run() {
            if (iPlayer != null && notification_flag) {
                try {
                    musicSeekBar.setMax(iPlayer.getDuration());
                    musicSeekBar.setProgress(iPlayer.getCurrentPosition());
                    mCurrentProgress.setText(MusicUtil.formatTime(iPlayer.getCurrentPosition()));
                    mFinalProgress.setText(MusicUtil.formatTime(iPlayer.getDuration()));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            handler.post(updateThread);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        initViews();

        musicList = MusicUtil.getAllSongList(PlayMusicActivity.this);

        final int position = getIntent().getIntExtra("position", -1);
        if (position != mCurrentPosition) {
            mCurrentPosition = position;
        }
        singerName.setText(musicList.get(position).getMusicSinger());
        songName.setText(musicList.get(position).getMusicName());
        mCurrentProgress.setText("00:00");
        musicTime = MusicUtil.formatTime(musicList.get(mCurrentPosition).getMusicTime());
        mFinalProgress.setText(musicTime);
        songId = musicList.get(mCurrentPosition).getMusicIndex();
        albumId = musicList.get(mCurrentPosition).getMusicAlubmId();
        bitmap = MusicUtil.getArtWork(PlayMusicActivity.this, songId, albumId, true);
        singerView.setImageBitmap(bitmap);
        singerView.startAnimation(ra);

        //绑定及开启服务
        intentService = new Intent();
        intentService.setClass(PlayMusicActivity.this, MusicService.class);
        bindService(intentService, conn, Context.BIND_AUTO_CREATE);
        startService(intentService);


        /**
         * 播放按钮
         */
        imagePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MusicUtil.Status) {
                    try {
                        iPlayer.play(mCurrentPosition);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    imagePlay.setBackgroundResource(R.drawable.start);
                    MusicUtil.Status = true;
                    singerView.startAnimation(ra);
                } else {
                    try {
                        iPlayer.pause();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    imagePlay.setBackgroundResource(R.drawable.pause);
                    MusicUtil.Status = false;
                    singerView.clearAnimation();
                }
                Intent notificationIntent = new Intent();
                notificationIntent.setAction(PLAY_MUSIC_ACTIVITY_PAUSE_MUSIC);
                sendBroadcast(notificationIntent);
            }

        });

        /*** 下一曲 ***/
        imageNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentPosition++;
                if (mCurrentPosition < 0) {
                    mCurrentPosition = musicList.size() - 1;
                } else if (mCurrentPosition > (musicList.size() - 1)) {
                    mCurrentPosition = 0;
                }
                playMusicById(mCurrentPosition);
                Intent intent = new Intent();
                intent.setAction(PLAY_NEXT_SONG);
                sendBroadcast(intent);
            }
        });

        /**
         * 上一曲
         */
        imagePre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentPosition--;
                if (mCurrentPosition < 0) {
                    mCurrentPosition = musicList.size() - 1;
                } else if (mCurrentPosition > (musicList.size() - 1)) {
                    mCurrentPosition = 0;
                }
                playMusicById(mCurrentPosition);
                Intent intent = new Intent();
                intent.setAction(PLAY_PRE_SONG);
                sendBroadcast(intent);

            }
        });

        /**
         * 进度条实时更新
         */
        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (iPlayer != null) {
                    try {
                        iPlayer.seekTo(seekBar.getProgress());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //实时刷新进度条
        handler.post(updateThread);

        //注册广播接收器
        receiver = new PlayMusicReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AUTO_NEXT_SONG);
        filter.addAction(PLAY_STATUS);
        filter.addAction(PAUSE_STATUS);
        filter.addAction(NOTIFICATION_PAUSE_MUSIC);
        filter.addAction(NOTIFICATION_EXIT_MUSIC);
        filter.addAction(NOTIFICATION_PREVIOUS_MUSIC);
        filter.addAction(NOTIFICATION_NEXT_MUSIC);
        filter.addAction(BROADCAST_CHANGEBACKGROUD);
        //动态注册广播监听器
        registerReceiver(receiver, filter);
    }

    private void initViews() {
        am = getApplicationContext().getAssets();
        storageUtil = new StorageUtil(getApplicationContext());

        backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this::onClick);

        imagePre = (ImageButton) findViewById(R.id.img_Pre);
        imagePlay = (ImageButton) findViewById(R.id.img_Play);
        imageNext = (ImageButton) findViewById(R.id.img_Next);
        musicSeekBar = (SeekBar) findViewById(R.id.seekBar);
        singerView = (CircleImageViews) findViewById(R.id.img_singer);
        songName = (TextView) findViewById(R.id.song_name);
        singerName = (TextView) findViewById(R.id.singer_name);
        mFinalProgress = (TextView) findViewById(R.id.total_time);
        mCurrentProgress = (TextView) findViewById(R.id.current_time);
        bgLayout = (RelativeLayout) findViewById(R.id.bg);
        storageUtil = new StorageUtil(getApplicationContext());
        try {
            InputStream is = am.open("bkgs/" + storageUtil.getPath());
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            Drawable drawable = ImageUtil.bitmapToDrawable(bitmap);
            bgLayout.setBackground(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (MusicUtil.Status) {
            imagePlay.setBackgroundResource(R.drawable.start);
        } else {
            imagePlay.setBackgroundResource(R.drawable.pause);
        }

        ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setInterpolator(new LinearInterpolator());
        ra.setRepeatCount(-1);
        ra.setDuration(5000 * 4);
        ra.setStartOffset(10);
    }

    /**
     * 点击返回按钮返回
     *
     * @param v
     */
    public void onClick(View v) {
        if (v == backBtn) {
            onBackPressed();
        }
    }

    /**
     * 播放并显示相关信息
     */
    public void playMusicById(int mCurrentPosition) {
        musicInfo = musicList.get(mCurrentPosition);
        String SongName = musicInfo.getMusicName();
        songName.setText(SongName);
        String SingerName = musicInfo.getMusicSinger();
        singerName.setText(SingerName);
        songId = musicList.get(mCurrentPosition).getMusicIndex();
        albumId = musicList.get(mCurrentPosition).getMusicAlubmId();
        bitmap = MusicUtil.getArtWork(PlayMusicActivity.this, songId, albumId, true);
        singerView.setImageBitmap(bitmap);
        singerView.startAnimation(ra);

        try {
            iPlayer.play(mCurrentPosition);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放完成后自动播放下一曲
     */
    public class PlayMusicReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case AUTO_NEXT_SONG:
                case NOTIFICATION_NEXT_MUSIC:
                    mCurrentPosition++;
                    if (mCurrentPosition < 0) {
                        mCurrentPosition = musicList.size() - 1;
                    } else if (mCurrentPosition > (musicList.size() - 1)) {
                        mCurrentPosition = 0;
                    }
                    playMusicById(mCurrentPosition);
                    Log.d("Sam", "******自动播放下一曲***********");
                    break;
                case NOTIFICATION_PREVIOUS_MUSIC:
                    mCurrentPosition--;
                    if (mCurrentPosition < 0) {
                        mCurrentPosition = musicList.size() - 1;
                    } else if (mCurrentPosition > (musicList.size() - 1)) {
                        mCurrentPosition = 0;
                    }
                    playMusicById(mCurrentPosition);
                    Log.d("Sam", "******自动播放下一曲***********");
                    break;
                case NOTIFICATION_PAUSE_MUSIC:
                    //点击Notification播放按钮相应改变播放界面的按钮
                    if (MusicUtil.Status) {
                        imagePlay.setBackgroundResource(R.drawable.start);
                    } else {
                        imagePlay.setBackgroundResource(R.drawable.pause);
                    }
                    break;
                case NOTIFICATION_EXIT_MUSIC:
                    notification_flag = false;
                    iPlayer = null;
                    unbindService(conn);
                    stopService(intentService);
                    finish();
                    break;
                case BROADCAST_CHANGEBACKGROUD:
                    String path = intent.getStringExtra("path");
                    try {
                        InputStream is = am.open("bkgs/" + path);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        Drawable drawable = ImageUtil.bitmapToDrawable(bitmap);
                        bgLayout.setBackground(drawable);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iPlayer = IServicePlayer.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (iPlayer != null && MusicUtil.Status) {
            isPause = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (iPlayer != null && isPause) {
            isPause = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
