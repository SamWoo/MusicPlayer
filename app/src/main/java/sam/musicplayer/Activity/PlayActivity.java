package sam.musicplayer.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dk.animation.SwitchAnimationUtil;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import sam.musicplayer.Bean.MusicInfo;
import sam.musicplayer.Constant;
import sam.musicplayer.Fragments.BackgroundFragment;
import sam.musicplayer.Fragments.LocalTracks;
import sam.musicplayer.Fragments.NetTracks;
import sam.musicplayer.IServicePlayer;
import sam.musicplayer.R;
import sam.musicplayer.Service.MusicService;
import sam.musicplayer.Util.ImageUtil;
import sam.musicplayer.Util.MusicUtil;
import sam.musicplayer.Util.MyGlideImageLoader;
import sam.musicplayer.Util.SPUtil;
import sam.musicplayer.Util.StorageUtil;
import sam.musicplayer.Views.CircleImageViews;
import sam.musicplayer.Views.RobotoLightTextView;


public class PlayActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private LocalTracks mLocalTracks;
    private NetTracks mNetTracks;
    private BackgroundFragment backgroundFragment;
    private ArrayList<MusicInfo> musicInfos = null;
    private CircleImageViews mArtist;
    private ImageView mGuestView;
    private ImageButton mPlayLocal;
    private ImageButton mNextLocal;
    private ImageButton mPreLocal;
    private ImageButton mPrised;
    //NavHeader图像和名字设置
    private CircleImageView mImgHeader;
    private TextView mUserName;

    private RobotoLightTextView mSongName;
    private RelativeLayout playLayout;
    private DrawerLayout mRootLayout;
    private int songId;
    private int albumId;
    private Bitmap bitmap = null;
    private IServicePlayer iPlayer;
    private int currentPosition = 0;
    private MusicInfo musicInfo = null;
    private final static String AUTO_NEXT_SONG = "sam.musicplayer.AUTO_NEXT_SONG";
    private final static String PLAY_NEXT_SONG = "sam.musicplayer.PLAY_NEXT_SONG";
    private final static String PLAY_PRE_SONG = "sam.musicplayer.PLAY_PRE_SONG";
    private final static String CURRENT_POSITION = "sam.musicplayer.CURRENT_POSITION";
    private final static String NOTIFICATION_PREVIOUS_MUSIC = "sam.musicplayer.NOTIFICATION_PREVIOUS_MUSIC";
    private final static String NOTIFICATION_NEXT_MUSIC = "sam.musicplayer.NOTIFICATION_NEXT_MUSIC";
    private final static String NOTIFICATION_PAUSE_MUSIC = "sam.musicplayer.NOTIFICATION_PAUSE_MUSIC";
    private final static String NOTIFICATION_EXIT_MUSIC = "sam.musicplayer.NOTIFICATION_EXIT_MUSIC";
    private final static String PLAY_ACTIVITY_PAUSE_MUSIC = "sam.musicplayer.PLAY_ACTIVITY_PAUSE_MUSIC";
    public static final String BROADCAST_CHANGEBACKGROUD = "sam.musicplayer.changebackgroud";
    private PlayActivityReceiver receiver = null;
    private Intent intentService = null;

    private GalleryConfig mGalleryConfig;//图片选择器的配置
    private List<String> mNavHeaderImgPaths = new ArrayList<>();//记录已选图片

    private boolean isPause;
    private int flag = 1;
    // 裁剪后图片的宽(X)和高(Y)
    private static int output_X = 600;
    private static int output_Y = 600;
    //专辑封面旋转动画
    private RotateAnimation ra = null;
    private StorageUtil storageUtil;
    private AssetManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("currentPosition");
            Log.d("Sam", "***************oncreate==========" + currentPosition);
//            MusicUtil.Status = savedInstanceState.getBoolean("play_status");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicInfos = MusicUtil.getAllSongList(PlayActivity.this);
        init();

        //绑定及开启服务
        intentService = new Intent();
        intentService.setClass(PlayActivity.this, MusicService.class);
        bindService(intentService, conn, Context.BIND_AUTO_CREATE);
        startService(intentService);

        new SwitchAnimationUtil().startAnimation(getWindow().getDecorView(), SwitchAnimationUtil.AnimationType.FLIP_HORIZON);

        //注册广播接收器
        receiver = new PlayActivityReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AUTO_NEXT_SONG);
        filter.addAction(PLAY_NEXT_SONG);
        filter.addAction(PLAY_PRE_SONG);
        filter.addAction(CURRENT_POSITION);
        filter.addAction(NOTIFICATION_EXIT_MUSIC);
        filter.addAction(NOTIFICATION_NEXT_MUSIC);
        filter.addAction(NOTIFICATION_PAUSE_MUSIC);
        filter.addAction(NOTIFICATION_PREVIOUS_MUSIC);
        filter.addAction(BROADCAST_CHANGEBACKGROUD);
        registerReceiver(receiver, filter);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);//去除ActionBar状态栏

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.GONE);//出去浮点按钮

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 初始化界面
     */
    private void init() {
        am = getApplication().getAssets();

        //find DrawerLayout中的CircleImageView控件
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
//        View layoutView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        mImgHeader = (CircleImageView) headerView.findViewById(R.id.guest_image);
        mUserName = (TextView) headerView.findViewById(R.id.user_name);

        mArtist = (CircleImageViews) findViewById(R.id.iv_art_bottom);
        mPlayLocal = (ImageButton) findViewById(R.id.btn_play);
        mNextLocal = (ImageButton) findViewById(R.id.btn_next);
        mPreLocal = (ImageButton) findViewById(R.id.btn_pre);
        mSongName = (RobotoLightTextView) findViewById(R.id.title);
        mGuestView = (CircleImageViews) findViewById(R.id.guest_image);
        playLayout = (RelativeLayout) findViewById(R.id.layout_play);
        mRootLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//设置背景图片
        storageUtil = new StorageUtil(getApplicationContext());
        try {
            InputStream is = am.open("bkgs/" + storageUtil.getPath());
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            Drawable drawable = ImageUtil.bitmapToDrawable(bitmap);
            playLayout.setBackground(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPlayLocal.setOnClickListener(this);
        mPreLocal.setOnClickListener(this);
        mNextLocal.setOnClickListener(this);
        mArtist.setOnClickListener(this);

        //设置图像更改监听
        mImgHeader.setOnClickListener((view -> changeHeader()));
        mUserName.setOnClickListener((view -> changeName()));

        //Fragment显示
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mLocalTracks = new LocalTracks();
        ft.replace(R.id.container, mLocalTracks);
        ft.commit();

        //动画特效
        ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setInterpolator(new LinearInterpolator());
        ra.setRepeatCount(-1);
        ra.setDuration(5000 * 2);
        ra.setStartOffset(10);
    }


    //更换图像
    private void changeHeader() {
        initGalleryConfig();//初始化图片选择器的配置参数
        initPermission();//授权管理
    }

    //初始化图片选择器的配置参数
    private void initGalleryConfig() {
        mGalleryConfig = new GalleryConfig.Builder()
                .imageLoader(new MyGlideImageLoader())
                .iHandlerCallBack(imgTakeListener)
                .pathList(mNavHeaderImgPaths)
                .multiSelect(false)
                .crop(true)
                .isShowCamera(true)
                .filePath("/Sam_Pic")
                .build();
    }

    //授权管理
    private void initPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //需要授权
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Snackbar.make(mRootLayout, "请在 设置-应用管理 中开启此应用的存储权限", Snackbar.LENGTH_SHORT).show();
            } else {
                //进行授权
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 8);
            }
        } else {
            //不需要授权
            GalleryPick.getInstance().setGalleryConfig(mGalleryConfig).open(this);
        }
    }

    //授权管理结果返回
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 8) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //同意授权
                GalleryPick.getInstance().setGalleryConfig(mGalleryConfig).open(this);
            } else {

            }
        }
    }

    //图片选择器的监听借口
    IHandlerCallBack imgTakeListener = new IHandlerCallBack() {
        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(List<String> photoList) {
            SPUtil.put(PlayActivity.this, Constant.HEADER_IMG_PATH, photoList.get(0));
            Glide.with(PlayActivity.this).load(photoList.get(0)).into(mImgHeader);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onFinish() {

        }

        @Override
        public void onError() {

        }
    };

    //更改UserName
    private void changeName() {
        startActivity(new Intent(this, ChangeNameActivity.class));
    }

    /**
     * 为主界面各个控件添加监听事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_play:
                if (!MusicUtil.Status) {
                    try {
                        if (currentPosition == -1)
                            currentPosition = 0;
                        iPlayer.play(currentPosition);
                        showInfo(currentPosition);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    mPlayLocal.setImageResource(R.drawable.notification_play);
                    MusicUtil.Status = true;
                    mArtist.startAnimation(ra);
                } else {
                    try {
                        iPlayer.pause();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    mPlayLocal.setImageResource(R.drawable.notification_pause);
                    MusicUtil.Status = false;
                    mArtist.clearAnimation();
                }
                Intent notificatioIntent = new Intent();
                notificatioIntent.setAction(PLAY_ACTIVITY_PAUSE_MUSIC);
                sendBroadcast(notificatioIntent);
                break;
            case R.id.btn_next:
                currentPosition++;
                if (currentPosition < 0) {
                    currentPosition = musicInfos.size() - 1;
                } else if (currentPosition > (musicInfos.size() - 1)) {
                    currentPosition = 0;
                }
                playMusicById(currentPosition);
                MusicUtil.Status = true;
                break;
            case R.id.btn_pre:
                currentPosition--;
                if (currentPosition < 0) {
                    currentPosition = musicInfos.size() - 1;
                } else if (currentPosition > (musicInfos.size() - 1)) {
                    currentPosition = 0;
                }
                playMusicById(currentPosition);
                MusicUtil.Status = true;
                break;
            case R.id.iv_art_bottom:
                if (flag == 1 && !MusicUtil.Status) {
                    Toast.makeText(this, "Please choose a song to play!!", Toast.LENGTH_SHORT).show();
                } else {
                    flag = 0;
                    Intent intent = new Intent(PlayActivity.this, PlayMusicActivity.class);
                    intent.putExtra("position", currentPosition);
                    startActivity(intent);
                }
                break;
        }

    }

    /**
     * 通过ID来播放音乐
     */
    public void playMusicById(int currentPosition) {
        musicInfo = musicInfos.get(currentPosition);
        String SongName = musicInfo.getMusicName();
        mSongName.setText(SongName);
        songId = musicInfos.get(currentPosition).getMusicIndex();
        albumId = musicInfos.get(currentPosition).getMusicAlubmId();
        bitmap = MusicUtil.getArtWork(PlayActivity.this, songId, albumId, true);
        mArtist.setImageBitmap(bitmap);

        try {
            iPlayer.play(currentPosition);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mArtist.startAnimation(ra);
        mPlayLocal.setImageResource(R.drawable.notification_play);
    }

    /**
     * 显示当前歌曲的信息
     *
     * @param position
     */
    private void showInfo(int position) {
        songId = musicInfos.get(position).getMusicIndex();
        albumId = musicInfos.get(position).getMusicAlubmId();
        bitmap = MusicUtil.getArtWork(PlayActivity.this, songId, albumId, true);
        mArtist.setImageBitmap(bitmap);
        mArtist.startAnimation(ra);
        mSongName.setText(musicInfos.get(position).getMusicName());
    }

    /**
     * adil后台Service
     */
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iPlayer = IServicePlayer.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    /**
     * 设置圆形图标
     *
     * @param view
     */
    public void setCircleView(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(PlayActivity.this);
        builder.setTitle("选择照片");
        builder.setPositiveButton("相机", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.addCategory("android.intent.category.DEFAULT");
                startActivityForResult(intent, 0);
            }
        });
        builder.setNegativeButton("相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.addCategory("android.intent.category.DEFAULT");
                startActivityForResult(intent, 1);
            }
        });
        builder.create().show();
    }

    /**
     * 选择图片后的回调函数
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ContentResolver resolver = getContentResolver();
        //如果不拍照或者不选择图片返回不执行任何操作
        if (data != null) {
            if (requestCode == 1) {//选择相册图片
                cropRawPhoto(data.getData());
            } else if (requestCode == 0) {//选择拍照图片
                String sdStatus = Environment.getExternalStorageState();
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                    return;
                }

                //照片的命名，目标文件夹下，以当前的时间数字串为名称，即可保证每张照片名称都不一样
                String str = null;
                Date date = null;
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                date = new Date();
                str = format.format(date);
                String fileName = "/sdcard/MyImage/" + str + ".jpg";
                File file = new File(fileName);
                file.mkdirs();

                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(fileName);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                cropRawPhoto(Uri.fromFile(file));

            } else if (requestCode == 2) {
                setImageToHeadView(data);
            }
        }
    }

    public void cropRawPhoto(Uri uri) {
        Intent intent = new Intent("com.android.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);
        intent.addCategory("android.intent.category.DEFAULT");
        startActivityForResult(intent, 2);
    }

    /**
     * 设置用户图标
     *
     * @param data
     */
    private void setImageToHeadView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            mGuestView.setImageBitmap(bitmap);
        }
    }

    /**
     * 处理各控件发出的广播
     */
    public class PlayActivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Sam", "***************哟呵。。。。。。。我收到广播了！！！！");
            String action = intent.getAction();
            switch (action) {
                case PLAY_NEXT_SONG:
                case NOTIFICATION_NEXT_MUSIC:
                case AUTO_NEXT_SONG:
                    currentPosition++;
                    if (currentPosition < 0) {
                        currentPosition = musicInfos.size() - 1;
                    } else if (currentPosition > (musicInfos.size() - 1)) {
                        currentPosition = 0;
                    }
                    playMusicById(currentPosition);
                    break;
                case PLAY_PRE_SONG:
                case NOTIFICATION_PREVIOUS_MUSIC:
                    currentPosition--;
                    if (currentPosition < 0) {
                        currentPosition = musicInfos.size() - 1;
                    } else if (currentPosition > (musicInfos.size() - 1)) {
                        currentPosition = 0;
                    }
                    playMusicById(currentPosition);
                    break;
                case CURRENT_POSITION:
                    currentPosition = intent.getIntExtra("currentPosition", 0);
                    Log.d("Sam", "$$$$$$$$$$$$$$$$=====" + currentPosition);

                    if (!MusicUtil.Status) {
                        MusicUtil.Status = true;
                    }
                    mPlayLocal.setImageResource(R.drawable.notification_play);
                    playMusicById(currentPosition);
                    break;
                case NOTIFICATION_PAUSE_MUSIC:
                    if (!MusicUtil.Status) {
                        try {
                            if (currentPosition == -1)
                                currentPosition = 0;
                            iPlayer.play(currentPosition);
                            showInfo(currentPosition);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        mPlayLocal.setImageResource(R.drawable.notification_play);
                        MusicUtil.Status = true;
                        mArtist.startAnimation(ra);
                    } else {
                        try {
                            iPlayer.pause();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        mPlayLocal.setImageResource(R.drawable.notification_pause);
                        MusicUtil.Status = false;
                        mArtist.clearAnimation();
                    }
                    break;
                case NOTIFICATION_EXIT_MUSIC:
                    MusicUtil.Status = false;
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
                        playLayout.setBackground(drawable);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_local) {
            if (mLocalTracks == null) {
                mLocalTracks = new LocalTracks();
            }
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, mLocalTracks);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.nav_net) {
            if (mNetTracks == null) {
                mNetTracks = new NetTracks();
            }
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, mNetTracks);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.nav_slideshow) {
            if (backgroundFragment == null) {
                backgroundFragment = new BackgroundFragment();
            }
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, backgroundFragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(PlayActivity.this, SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        //重新打开应用获取之前的状态信息
        String mName = (String) SPUtil.get(this, Constant.USER_NAME, "Your name");
        mUserName.setText(mName);

        if (MusicUtil.Status) {
            mPlayLocal.setImageResource(R.drawable.notification_play);
        } else {
            mPlayLocal.setImageResource(R.drawable.notification_pause);
        }
        if (iPlayer != null && isPause) {
            isPause = false;
        }
        super.onResume();
        Log.d("Sam", "================onResume================");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        Log.d("Sam", "-----------onDestory-----------");
    }
}
