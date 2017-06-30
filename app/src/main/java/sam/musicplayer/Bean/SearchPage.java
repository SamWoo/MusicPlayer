package sam.musicplayer.Bean;

/**
 * Created by Administrator on 2017/6/27.
 */

public class SearchPage<T> {
    private int allNum;
    private int allPages;
    private int currentPage;
    private String notice;
    private int ret_code;
    private String w;
    private T contentlist;

    public int getAllNum() {
        return allNum;
    }

    public void setAllNum(int allNum) {
        this.allNum = allNum;
    }

    public int getAllPages() {
        return allPages;
    }

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public T getContentlist() {
        return contentlist;
    }

    public void setContentlist(T contentlist) {
        this.contentlist = contentlist;
    }
}
