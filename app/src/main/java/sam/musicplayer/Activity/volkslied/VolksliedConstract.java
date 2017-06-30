package sam.musicplayer.Activity.volkslied;

import java.util.List;

import sam.musicplayer.Bean.MusicBean;
import sam.musicplayer.base.BasePresenter;
import sam.musicplayer.base.BaseView;

/**
 * Created by Administrator on 2017/6/28.
 */

public interface VolksliedConstract {
    interface View extends BaseView{
        void setData(List<MusicBean>list);
        void setRefresh(boolean refresh);
    }

    interface Presenter extends BasePresenter{}
}
