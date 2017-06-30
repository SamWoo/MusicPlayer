package sam.musicplayer.api;

import com.jiongbull.jlog.JLog;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import sam.musicplayer.Constant;

/**
 * Created by Administrator on 2017/6/27.
 */

public class ApiManager {
    private static final int READ_TIME_OUT = 3;
    private static final int CONNECT_TIME_OUT = 3;
    private QQMusicApi mQQMusicApiService;

    private ApiManager() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> showRetrofitLog(message))
                .setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
				.build();
        Retrofit retrofit1 = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constant.QQ_MUSIC_BASE_URL)
                .build();
        mQQMusicApiService = retrofit1.create(QQMusicApi.class);
    }

    /**
     * 单例对象持有者
     */
    private static class SingletonHolder {
        private static final ApiManager INSTANCE = new ApiManager();
    }

    /**
     * 获取ApiManager单例对象
     *
     * @return
     */
    public static ApiManager getApiManager() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 打印日志
     * 返回的是json，就打印格式化好的json，不是json就鸳鸯打印
     *
     * @param message
     */
    private void showRetrofitLog(String message) {
        if (message.startsWith("{")) {
            JLog.json(message);
        } else {
            JLog.e("Retrofit:", message);
        }
    }

    public QQMusicApi getQQMusicApiService() {
        return mQQMusicApiService;
    }
}
