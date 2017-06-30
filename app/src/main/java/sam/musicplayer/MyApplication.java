package sam.musicplayer;

import android.app.Application;
import android.content.Context;

import com.jiongbull.jlog.JLog;

import org.greenrobot.greendao.database.Database;

import sam.musicplayer.greendao.DaoMaster;
import sam.musicplayer.greendao.DaoSession;

/**
 * Created by dingmouren on 2017/1/18.
 */

public class MyApplication extends Application {
    public static DaoSession mDaoSession;
    public static Context mContext;
//    private static RefWatcher mRefWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = this.getApplicationContext();
        initGreenDao();//初始化数据库
        JLog.init(this).setDebug(BuildConfig.DEBUG);
//        mRefWatcher = LeakCanary.install(this);//使用RefWathcer检测内存泄漏
//        CrashReport.initCrashReport(getApplicationContext());//异常统计
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"music_db",null);
        Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
    }

    public static DaoSession getDaoSession(){
        return mDaoSession;
    }

//    public static RefWatcher getRefWatcher(){
//        return mRefWatcher;
//    }
}
