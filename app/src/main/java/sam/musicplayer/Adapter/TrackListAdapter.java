package sam.musicplayer.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import sam.musicplayer.Bean.MusicInfo;
import sam.musicplayer.R;
import sam.musicplayer.Util.MusicUtil;

/**
 * Created by Administrator on 2016/7/18.
 */
public class TrackListAdapter extends BaseAdapter {

    private List<MusicInfo> infos = new ArrayList<>();
    private Context context;

    public TrackListAdapter(Context context, ArrayList<MusicInfo> musicInfos) {
        this.context = context;
        infos = musicInfos;
    }

    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int position) {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return infos.get(position).getMusicIndex();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.music_list_item, null);

            holder.ablumImage = (ImageView) convertView.findViewById(R.id.listImage);
            holder.singerName = (TextView) convertView.findViewById(R.id.listSinger);
            holder.songName = (TextView) convertView.findViewById(R.id.listName);
            holder.songTime = (TextView) convertView.findViewById(R.id.duration);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int songid = infos.get(position).getMusicIndex();//获取音频文件的排序号
        String musicName = infos.get(position).getMusicName();
        String musicSinger = infos.get(position).getMusicSinger();
        String songTime = MusicUtil.formatTime(infos.get(position).getMusicTime());
        if (musicSinger.contains("<unknown>")) {
            musicSinger = "<未知>";
        }
        Bitmap bitmap = MusicUtil.getArtWork(context.getApplicationContext(), songid, infos.get(position).getMusicAlubmId(), true);
        holder.songName.setText(musicName);
        holder.singerName.setText(musicSinger);
        holder.ablumImage.setImageBitmap(bitmap);
        holder.songTime.setText(songTime);

        return convertView;
    }

    static class ViewHolder {
        public ImageView ablumImage;
        public TextView songName;
        public TextView singerName;
        public TextView songTime;
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDispaly = !displayedImages.contains(imageUri);
                FadeInBitmapDisplayer.animate(imageView, 1000);
                if (firstDispaly) {
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
