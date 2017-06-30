package sam.musicplayer.api;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import sam.musicplayer.Bean.LyricBean;
import sam.musicplayer.Bean.MusicBean;
import sam.musicplayer.Bean.QQMusicBody;
import sam.musicplayer.Bean.QQMusicPage;
import sam.musicplayer.Bean.QQMusicResult;
import sam.musicplayer.Bean.SearchBean;
import sam.musicplayer.Bean.SearchPage;

/**
 * Created by Administrator on 2017/6/27.
 */

public interface QQMusicApi {
    //请求歌曲
    @GET("213-4")
    Observable<QQMusicResult<QQMusicBody<QQMusicPage<List<MusicBean>>>>>
    getQQMusic(@Query("showapi_appid") String showapi_appid, @Query("showapi_sign")String showapi_sign,@Query("topid")   String topic);

    //搜索歌曲
    @GET("213-1")
    Observable<QQMusicResult<QQMusicBody<SearchPage<List<SearchBean>>>>>
    searchQQMusic(@Query("showapi_appid") String showapi_appid, @Query("showapi_sign")String showapi_sign,@Query("keyword")   String keyword,@Query("page") String page);

    //歌词
    @GET("213-2")
    Observable<QQMusicResult<LyricBean>>
    searchLyric(@Query("showapi_appid") String showapi_appid, @Query("showapi_sign")String showapi_sign,@Query("musicid")   String musicid);

}
