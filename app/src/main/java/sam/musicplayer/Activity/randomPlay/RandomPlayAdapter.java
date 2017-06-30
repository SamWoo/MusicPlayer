package sam.musicplayer.Activity.randomPlay;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import sam.musicplayer.Bean.MusicBean;
import sam.musicplayer.MyApplication;
import sam.musicplayer.R;

//import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2017/6/28.
 */

public class RandomPlayAdapter extends RecyclerView.Adapter<RandomPlayAdapter.ViewHolder> {
    private List<MusicBean> mList;
    private Context mContext;
    private MusicBean playingBean;

    public RandomPlayAdapter(Context context) {
        this.mContext = context;
    }

    public void setList(List<MusicBean> list) {
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_volkslied, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(mList.get(position), playingBean);
        holder.relative_volkslied.setOnClickListener((view -> {
//            if (null != onItemClickListener){
//                onItemClickListener.onItemClickListener(holder.relative_volkslied,position);
//            }
        }));
    }


    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    public void setOnItemClickListener() {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSongName;
        TextView tvSingerName;
        ImageView imgAlbum;
        ImageView fabIsPlay;
        RelativeLayout relative_volkslied;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSongName = (TextView) itemView.findViewById(R.id.tv_songname);
            tvSingerName = (TextView) itemView.findViewById(R.id.tv_singername);
            imgAlbum = (ImageView) itemView.findViewById(R.id.img_album);
            fabIsPlay = (ImageView) itemView.findViewById(R.id.fab_isPlay);
            relative_volkslied = (RelativeLayout) itemView.findViewById(R.id.relative_volkslied);

        }

        private void bindData(MusicBean bean, MusicBean playingBaen) {
            if (null != bean) {
                tvSongName.setText(bean.getSongname());
                tvSingerName.setText(bean.getSingername());
                Glide.with(mContext).load(bean.getAlbumpic_small()).asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.notification_img_holder)
                        .into(imgAlbum);
            }


            if (null != bean && null != playingBaen) {
                if (!bean.getSongname().equals(playingBaen.getSongname()) && bean.getSongid() != playingBaen.getSongid()) {
                    fabIsPlay.setVisibility(View.GONE);
                } else if (bean.getSongname().equals(playingBean.getSongname())
                        && bean.getSingername().equals(playingBaen.getSingername())) {
                    fabIsPlay.setVisibility(View.VISIBLE);
                    Glide.with(MyApplication.mContext).load(R.drawable.playing).asGif().diskCacheStrategy(DiskCacheStrategy.NONE).into(fabIsPlay);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(View view, int position);
    }
}

