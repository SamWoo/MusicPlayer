package sam.musicplayer.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import sam.musicplayer.Activity.PlayActivity;
import sam.musicplayer.Activity.jk.JKActivity;
import sam.musicplayer.Activity.randomPlay.RandomPlayActivity;
import sam.musicplayer.Activity.rock.RockActivity;
import sam.musicplayer.Activity.volkslied.VolksliedActivity;
import sam.musicplayer.Adapter.TrackListAdapter;
import sam.musicplayer.Bean.MusicInfo;
import sam.musicplayer.Constant;
import sam.musicplayer.R;
import sam.musicplayer.Util.MusicUtil;
import sam.musicplayer.Views.CircleProgress;

/**
 * Created by Administrator on 2016/7/18.
 */
public class NetTracks extends Base {

    private ListView mListView;
    private ArrayList<MusicInfo> musicInfos = null;
    private TrackListAdapter mAdapter = null;
    private PlayActivity mActivity;
    private final static String CURRENT_POSITION = "sam.musicplayer.CURRENT_POSITION";
    private View view;
    private CircleProgress mCircleProgress;
    private ImageView img_random;
    private ImageView img_jk;
    private ImageView img_rock;
    private ImageView img_volkslied;

    @Override
    public void onPraisedPressed() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_net_songs, null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCircleProgress = (CircleProgress) view.findViewById(R.id.progress);
        mCircleProgress.startAnim();
//        new GetSystemMusicDataTask().execute();
        img_jk = (ImageView) view.findViewById(R.id.img_jk);
        img_random = (ImageView) view.findViewById(R.id.img_random);
        img_rock = (ImageView) view.findViewById(R.id.img_rock);
        img_volkslied = (ImageView) view.findViewById(R.id.img_volkslied);

        img_volkslied.setOnClickListener(this::onClick);
        img_rock.setOnClickListener(this::onClick);
        img_jk.setOnClickListener(this::onClick);
        img_random.setOnClickListener(this::onClick);
    }

    /**
     * 点击图片跳转相应的界面
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_jk:
                turnToJK(view);
                break;
            case R.id.img_random:
                playRandom();
                break;
            case R.id.img_rock:
                turnToRock(view);
                break;
            case R.id.img_volkslied:
                turnToVolkslied(view);
                break;
        }
    }

    private void playRandom() {
        Intent intent = new Intent(mActivity, RandomPlayActivity.class);
        intent.putExtra("flag", Constant.MAIN_RANDOM);
        startActivity(intent);
    }

    private void turnToJK(View view) {

        startActivity(new Intent(mActivity, JKActivity.class));
    }

    private void turnToRock(View view) {

        startActivity(new Intent(mActivity, RockActivity.class));
    }

    private void turnToVolkslied(View view) {
        startActivity(new Intent(mActivity, VolksliedActivity.class));
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (PlayActivity) activity;
    }


    /**
     * 后台加载数量庞大的数据
     */
    public class GetSystemMusicDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            musicInfos = MusicUtil.getAllSongList(getContext());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter = new TrackListAdapter(getContext(), musicInfos);
            mListView.setAdapter(mAdapter);
            mCircleProgress.stopAnim();
            mCircleProgress.setVisibility(View.GONE);
        }
    }
}
