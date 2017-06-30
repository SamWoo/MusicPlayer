package sam.musicplayer.Util;

import android.widget.Toast;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import sam.musicplayer.Constant;
import sam.musicplayer.Bean.MusicBean;
import sam.musicplayer.Bean.QQMusicBody;
import sam.musicplayer.Bean.QQMusicPage;
import sam.musicplayer.Bean.QQMusicResult;
import sam.musicplayer.MyApplication;
import sam.musicplayer.api.ApiManager;
import sam.musicplayer.greendao.MusicBeanDao;

/**
 * Created by Administrator on 2017/6/27.
 */

public class RequestMusicUtil {
    private static final String TAG = RequestMusicUtil.class.getName();

    public void requestMusic(String topic) {
        ApiManager.getApiManager().getQQMusicApiService()
                .getQQMusic(Constant.QQ_MUSIC_APP_ID, Constant.QQ_MUSIC_SIGN, topic)
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<QQMusicResult<QQMusicBody<QQMusicPage<List<MusicBean>>>>>() {
                    @Override
                    public void call(QQMusicResult<QQMusicBody<QQMusicPage<List<MusicBean>>>> qqMusicBodyQQMusicResult) {
                        parseData(topic, qqMusicBodyQQMusicResult.getShowapi_res_body().getPagebean().getSonglist());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(qqMusicBodyQQMusicResult -> {
                }, this::loadError);

    }

    /**
     * 将数据保存到数据库
     *
     * @param
     */
    private static void parseData(String topic, List<MusicBean> list) {
        if (null != topic && null != list) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setType(Integer.parseInt(topic));
                if (0 == MyApplication.getDaoSession().getMusicBeanDao().queryBuilder()
                        .where(MusicBeanDao.Properties.Songid.eq(list.get(i).getSongid())).count()) {
                    MyApplication.getDaoSession().getMusicBeanDao().insertOrReplace(list.get(i));
                }
            }
        }
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        Toast.makeText(MyApplication.mContext, "网络错误", Toast.LENGTH_SHORT).show();
    }
}
