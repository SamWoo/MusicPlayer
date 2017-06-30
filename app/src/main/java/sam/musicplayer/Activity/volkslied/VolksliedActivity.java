package sam.musicplayer.Activity.volkslied;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;

import com.jiongbull.jlog.JLog;

import java.util.List;

import butterknife.ButterKnife;
import sam.musicplayer.Activity.PlayMusicActivity;
import sam.musicplayer.Bean.MusicBean;
import sam.musicplayer.Constant;
import sam.musicplayer.MyApplication;
import sam.musicplayer.R;
import sam.musicplayer.greendao.MusicBeanDao;

/**
 * Created by Administrator on 2017/6/27.
 */

public class VolksliedActivity extends AppCompatActivity implements VolksliedConstract {

    //    @BindView(R.id.toolbar)
    private Toolbar mToolbar;
    //    @BindView(R.id.collapsing)
    private CollapsingToolbarLayout mCollapsing;
    //    @BindView(R.id.recycler)
    private RecyclerView mRecycler;
    //    @BindView(R.id.swipe_refersh)
    private SwipeRefreshLayout mSwipeRefresh;
    //    @BindView(R.id.coordinator_vol)
    private CoordinatorLayout mRootLayout;

    private static final String TAG = VolksliedActivity.class.getName();
    private List<MusicBean> mList;
    Messenger mMessengerClient;
    private VolksliedAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_volkslied);
        ButterKnife.bind(this);
        getWindow().setEnterTransition(new Fade());
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCollapsing = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        mRecycler = (RecyclerView) findViewById(R.id.recycler);
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refersh);
        mRootLayout = (CoordinatorLayout) findViewById(R.id.coordinator_vol);

//        bindService(new Intent(this, MediaPlayerService.class), mServiceConnection, BIND_AUTO_CREATE);
        mCollapsing.setTitle("");
        mCollapsing.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        mCollapsing.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener((view -> onBackPressed()));

        mAdapter = new VolksliedAdapter(this);
//        mAdapter.setOnItemClickListener((view,position)->playSong(position));
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(mAdapter);

        //初始化数据
        mList = MyApplication.getDaoSession().getMusicBeanDao().queryBuilder()
                .where(MusicBeanDao.Properties.Type.eq(Constant.MUSIC_VOLKSLIED)).list();
        if (null != mList) {
            mAdapter.setList(mList);
            mAdapter.notifyDataSetChanged();
        }

        //设置数据

    }

    private void playSong(int position) {
        Intent intent = new Intent(this, PlayMusicActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("flag", Constant.MUSIC_VOLKSLIED);
        JLog.e(TAG, "点击民谣一首音乐");
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    /**
     *
     */
    /*
    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceConnection = new Messenger(service);
            if (null != mServiceConnection) {
                Message msgToService = Message.obtain();
                msgToService.replyTo = mMessengerClient;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
    */
}
