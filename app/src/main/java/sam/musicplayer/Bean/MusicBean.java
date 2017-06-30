package sam.musicplayer.Bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import sam.musicplayer.base.BaseBean;

/**
 * Created by Administrator on 2017/6/27.
 */
@Entity
public class MusicBean extends BaseBean{

    @Id(autoincrement = true)
    private Long id;
    private String songname;
    private int seconds;
    private String albummid;
    private int songid;
    private int singerid;
    private String albumpic_big;
    private String albumpic_small;
    private String downUrl;
    private String url;
    private String singername;
    private int albumid;
    private int type;
    private boolean isCollected;

    @Generated(hash = 1899243370)
    public MusicBean() {
    }

    @Generated(hash = 1740311575)
    public MusicBean(Long id, String songname, int seconds, String albummid, int songid, int singerid,
            String albumpic_big, String albumpic_small, String downUrl, String url, String singername,
            int albumid, int type, boolean isCollected) {
        this.id = id;
        this.songname = songname;
        this.seconds = seconds;
        this.albummid = albummid;
        this.songid = songid;
        this.singerid = singerid;
        this.albumpic_big = albumpic_big;
        this.albumpic_small = albumpic_small;
        this.downUrl = downUrl;
        this.url = url;
        this.singername = singername;
        this.albumid = albumid;
        this.type = type;
        this.isCollected = isCollected;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSongname() {
        return this.songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public int getSeconds() {
        return this.seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getAlbummid() {
        return this.albummid;
    }

    public void setAlbummid(String albummid) {
        this.albummid = albummid;
    }

    public int getSongid() {
        return this.songid;
    }

    public void setSongid(int songid) {
        this.songid = songid;
    }

    public int getSingerid() {
        return this.singerid;
    }

    public void setSingerid(int singerid) {
        this.singerid = singerid;
    }

    public String getAlbumpic_big() {
        return this.albumpic_big;
    }

    public void setAlbumpic_big(String albumpic_big) {
        this.albumpic_big = albumpic_big;
    }

    public String getAlbumpic_small() {
        return this.albumpic_small;
    }

    public void setAlbumpic_small(String albumpic_small) {
        this.albumpic_small = albumpic_small;
    }

    public String getDownUrl() {
        return this.downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSingername() {
        return this.singername;
    }

    public void setSingername(String singername) {
        this.singername = singername;
    }

    public int getAlbumid() {
        return this.albumid;
    }

    public void setAlbumid(int albumid) {
        this.albumid = albumid;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean getIsCollected() {
        return this.isCollected;
    }

    public void setIsCollected(boolean isCollected) {
        this.isCollected = isCollected;
    }
}
