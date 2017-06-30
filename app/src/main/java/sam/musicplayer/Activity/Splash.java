package sam.musicplayer.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.dk.animation.SwitchAnimationUtil;
import com.jiongbull.jlog.JLog;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import sam.musicplayer.Constant;
import sam.musicplayer.FontUtil.FontsFactory;
import sam.musicplayer.MyApplication;
import sam.musicplayer.R;
import sam.musicplayer.Util.RequestMusicUtil;
import sam.musicplayer.greendao.MusicBeanDao;

public class Splash extends AppCompatActivity {
    private static final String TAG = Splash.class.getName();
    private RequestMusicUtil mRequestMusicUtil;
    private String[] topics = new String[]{Constant.MUSIC_HONGKONG, Constant.MUSIC_HOT,
            Constant.MUSIC_INLAND, Constant.MUSIC_JAPAN, Constant.MUSIC_KOREA, Constant.MUSIC_ROCK,
            Constant.MUSIC_SALES, Constant.MUSIC_VOLKSLIED, Constant.MUSIC_WEST};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/hero.ttf");
        TextView splash = (TextView) findViewById(R.id.splash_text);
        splash.setTypeface(typeface);
        splash.setText("Music Lite");

        new SwitchAnimationUtil().startAnimation(getWindow().getDecorView(), SwitchAnimationUtil.AnimationType.ROTATE);
        FontsFactory.createRoboto(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, PlayActivity.class);
                Splash.this.startActivity(intent);
                Splash.this.finish();
            }
        }, 3000);

        initData();
    }

    //初始化数据
    public void initData() {
//        DaoSession session = MyApplication.getDaoSession();
//        Log.e(TAG, "session--------->" + session);
//        long count = session.getMusicBeanDao().queryBuilder()
        long count = MyApplication.getDaoSession().getMusicBeanDao().queryBuilder()
                .whereOr(MusicBeanDao.Properties.Type.eq(Integer.valueOf(Constant.MUSIC_WEST)),
                        MusicBeanDao.Properties.Type.eq(Integer.valueOf(Constant.MUSIC_INLAND)),
                        MusicBeanDao.Properties.Type.eq(Integer.valueOf(Constant.MUSIC_HONGKONG)),
                        MusicBeanDao.Properties.Type.eq(Integer.valueOf(Constant.MUSIC_KOREA)),
                        MusicBeanDao.Properties.Type.eq(Integer.valueOf(Constant.MUSIC_JAPAN)),
                        MusicBeanDao.Properties.Type.eq(Integer.valueOf(Constant.MUSIC_VOLKSLIED)),
                        MusicBeanDao.Properties.Type.eq(Integer.valueOf(Constant.MUSIC_ROCK)),
                        MusicBeanDao.Properties.Type.eq(Integer.valueOf(Constant.MUSIC_SALES)),
                        MusicBeanDao.Properties.Type.eq(Integer.valueOf(Constant.MUSIC_HOT))).count();

        JLog.e(TAG, "count:" + count);

        mRequestMusicUtil = new RequestMusicUtil();
        Observable.interval(1000, 1500, TimeUnit.MILLISECONDS).limit(9)
                .subscribe(aLong -> mRequestMusicUtil.requestMusic(topics[aLong.intValue()]));
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}
