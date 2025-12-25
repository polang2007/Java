package org.example;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Retrofit单例管理
 * Created by QinLiao on 2025/02/20
 */
public class RetrofitManager {
    private static final RetrofitManager sInstance = new RetrofitManager();
    private static Retrofit mRetrofit;

    private RetrofitManager() {
    }

    public static RetrofitManager getInstance() {
        return sInstance;
    }

    public static void init() {
        if (mRetrofit == null) {
            //初始化一个OkHttpClient
            OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(30000, TimeUnit.MILLISECONDS).readTimeout(30000, TimeUnit.MILLISECONDS).writeTimeout(30000, TimeUnit.MILLISECONDS);
//            builder.addInterceptor(new LoggingInterceptor());
            OkHttpClient okHttpClient = builder.build();

            //使用该OkHttpClient创建一个Retrofit对象
            String BASE_URL = "https://www.baidu.com/";
            mRetrofit = new Retrofit.Builder()
                    //添加Gson数据格式转换器支持
//                    .addConverterFactory(GsonConverterFactory.create())
                    //添加RxJava语言支持
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    //指定网络请求client
                    .client(okHttpClient).baseUrl(BASE_URL).build();
        }
    }

    public static Retrofit getRetrofit() {
        if (mRetrofit == null) {
            init();
        }
        return mRetrofit;
    }

    public static String getBodyStrSync(String url) {
        //使用默认的headers
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.88 Safari/537.36");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
        return getBodyStrSync(url, headers);
    }


    public static String getBodyStrSync(String url, Map<String, String> headers) {
        RetrofitApi retrofitApi = getRetrofit().create(RetrofitApi.class);
        try {
            Response<ResponseBody> responseBody = retrofitApi.getHtml(url, headers).execute();
            return responseBody.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

