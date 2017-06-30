package sam.musicplayer.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import sam.musicplayer.Activity.PlayActivity;
import sam.musicplayer.Interface.AppConstants;
import sam.musicplayer.R;
import sam.musicplayer.Util.StorageUtil;

/**
 * Created by Administrator on 2016/8/12.
 */
public class BackgroundFragment extends Fragment implements AdapterView.OnItemClickListener, AppConstants, OnClickListener {
    private ImageButton mBackBtn;
    private GridView mGridView;
    private List<BgEntity> mBgList;
    private StorageUtil storageUtil;
    private String mDefaultBgPath;
    private GridAdapter gridAdapter;
    private PlayActivity mActivity;

    private class BgEntity {
        Bitmap bitmap;
        String path;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.background_fragment, container, false);
        storageUtil = new StorageUtil(getActivity());
        mDefaultBgPath = storageUtil.getPath();

        getData();
        initView(view);
        return view;
    }

    /**
     * 获取背景图片数据
     */
    private void getData() {
        AssetManager am = getActivity().getAssets();
        try {
            String[] drawableList = am.list("bkgs");
            mBgList = new ArrayList<BgEntity>();
            for (String path : drawableList) {
                BgEntity bg = new BgEntity();
                InputStream is = am.open("bkgs/" + path);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                bg.path = path;
                bg.bitmap = bitmap;
                mBgList.add(bg);
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化界面视图
     *
     * @param view
     */
    private void initView(View view) {
        mBackBtn = (ImageButton) view.findViewById(R.id.backBtn);
        mBackBtn.setOnClickListener(this);
        mGridView = (GridView) view.findViewById(R.id.grid_content);
        gridAdapter = new GridAdapter(mBgList);

        mGridView.setOnItemClickListener(this);
        mGridView.setAdapter(gridAdapter);
    }

    /**
     * 为GridView进行适配操作
     */
    private class GridAdapter extends BaseAdapter {

        private List<BgEntity> bgList;
        private Resources resources;

        public GridAdapter(List<BgEntity> list) {
            this.bgList = list;
            this.resources = getActivity().getResources();
        }

        @Override
        public int getCount() {
            return bgList.size();
        }

        @Override
        public Object getItem(int position) {
            return bgList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = getActivity().getLayoutInflater().inflate(R.layout.background_gridview_item, null);
                holder.backgroudIv = (ImageView) convertView.findViewById(R.id.gridview_item_iv);
                holder.checkedIv = (ImageView) convertView.findViewById(R.id.gridview_item_checked_iv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.backgroudIv.setBackgroundDrawable(new BitmapDrawable(resources, bgList.get(position).bitmap));
            if (bgList.get(position).path.equals(mDefaultBgPath)) {
                holder.checkedIv.setVisibility(View.VISIBLE);
            } else {
                holder.checkedIv.setVisibility(View.GONE);
            }
            return convertView;
        }

        private class ViewHolder {
            ImageView checkedIv, backgroudIv;
        }
    }

    /**
     * 点击返回按钮返回
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v == mBackBtn) {
            mActivity.onBackPressed();
        }
    }

    /**
     * fragment绑定对应的activity
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (PlayActivity) activity;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String path = mBgList.get(position).path;
        storageUtil.savePath(path);

        mDefaultBgPath = path;
        gridAdapter.notifyDataSetChanged();

        Intent intent = new Intent(BROADCAST_CHANGEBACKGROUD);
        intent.putExtra("path", path);
//        Log.e("Sam", "bg===========" + path);
        getActivity().sendBroadcast(intent);
    }
}
