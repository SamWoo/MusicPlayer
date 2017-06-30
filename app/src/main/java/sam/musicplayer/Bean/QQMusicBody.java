package sam.musicplayer.Bean;

/**
 * Created by Administrator on 2017/6/27.
 */

public class QQMusicBody<T> {
    private int ret_code;
    private T pagebean;

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public T getPagebean() {
        return pagebean;
    }

    public void setPagebean(T pagebean) {
        this.pagebean = pagebean;
    }

}
