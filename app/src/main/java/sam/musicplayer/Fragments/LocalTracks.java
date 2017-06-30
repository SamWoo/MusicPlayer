package sam.musicplayer.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

import sam.musicplayer.Adapter.TrackListAdapter;
import sam.musicplayer.Bean.MusicInfo;
import sam.musicplayer.Activity.PlayActivity;
import sam.musicplayer.R;
import sam.musicplayer.Util.MusicUtil;
import sam.musicplayer.Views.CircleProgress;

/**
 * Created by Administrator on 2016/7/18.
 */
public class LocalTracks extends Base {

    private ListView mListView;
    private ArrayList<MusicInfo> musicInfos = null;
    private TrackListAdapter mAdapter = null;
    private PlayActivity mActivity;
    private final static String CURRENT_POSITION = "sam.musicplayer.CURRENT_POSITION";
    private View view;
    private CircleProgress mCircleProgress;

    @Override
    public void onPraisedPressed() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_songs, null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.list_songs);
        mCircleProgress = (CircleProgress) view.findViewById(R.id.progress);
        mCircleProgress.startAnim();
        new GetSystemMusicDataTask().execute();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setAction(CURRENT_POSITION);
                intent.putExtra("currentPosition", position);
                mActivity.sendBroadcast(intent);
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //弹出对话框是否删除该项
                DeleteDialog(position);
                return false;
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (PlayActivity) activity;
    }

    public void DeleteDialog(final int position){
        AlertDialog.Builder builder=new AlertDialog.Builder(mActivity);
        builder.setMessage("确定要删除文件?")
                .setTitle("提示")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file=new File(String.valueOf(musicInfos.get(position)));
                        Log.e("Sam", "file---------------------------->" + file);
                        file.delete();
                        //通知adapter更新
                        mAdapter = new TrackListAdapter(getContext(), musicInfos);
                        mListView.setAdapter(mAdapter);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();

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
