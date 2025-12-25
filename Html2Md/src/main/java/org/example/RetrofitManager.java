package org.example;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Retrofit 单例管理器
 * <p>
 * 采用“静态内部类”方式实现线程安全懒加载，避免重复初始化。
 * 提供同步 GET 请求快捷方法，默认携带浏览器 UA 头。
 *
 * @author QinLiao
 * @since 2025/02/20
 */
public final class RetrofitManager {

    /*==================== 常量 ====================*/

    /**
     * 默认请求超时时间（毫秒）
     */
    private static final long DEFAULT_TIMEOUT_MS = 30_000L;

    /**
     * 默认 baseUrl，尾部无空格，防止拼接出错
     */
    private static final String BASE_URL = "https://www.baidu.com/";

    /**
     * 默认头信息，可复用，减少重复创建 Map
     */
    private static final Map<String, String> DEFAULT_HEADERS;

    static {
        Map<String, String> temp = new HashMap<>(4);
        temp.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.88 Safari/537.36");
        temp.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
        DEFAULT_HEADERS = Collections.unmodifiableMap(temp);
    }

    /*==================== 单例 ====================*/

    /**
     * 内部缓存 Retrofit 实例，仅初始化一次
     */
    private final Retrofit mRetrofit = createRetrofit();

    private RetrofitManager() {
        // 私有构造，禁止外部 new
    }

    /**
     * 对外唯一入口
     */
    public static RetrofitManager getInstance() {
        return Holder.INSTANCE;
    }

    /*==================== Retrofit 实例 ====================*/

    private Retrofit createRetrofit() {
        // 构建 OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(DEFAULT_TIMEOUT_MS, TimeUnit.MILLISECONDS).readTimeout(DEFAULT_TIMEOUT_MS, TimeUnit.MILLISECONDS).writeTimeout(DEFAULT_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                // 如需日志拦截器，可在此处打开
                // .addInterceptor(new LoggingInterceptor())
                .build();

        // 构建 Retrofit
        return new Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
                // 如需 Gson 解析，可打开下一行
                // .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
    }

    /**
     * 获取 Retrofit 实例（已确保非空）
     */
    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    /**
     * 同步 GET 请求，返回整个 HTML 字符串（使用默认头）
     *
     * @param url 完整 URL
     * @return 非空 Optional，若发生异常则 Optional.empty()
     */
    public Optional<String> getBodyStrSync(final String url) {
        return getBodyStrSync(url, DEFAULT_HEADERS);
    }
    public Response<ResponseBody> getResponse(final String url){
        return getResponse(url, DEFAULT_HEADERS);
    }
    /*==================== 业务快捷方法 ====================*/

    /**
     * 同步 GET 请求，可自定义头信息
     *
     * @param url     完整 URL
     * @param headers 自定义头（可空）
     * @return 非空 Optional，若发生异常则 Optional.empty()
     */
    public Optional<String> getBodyStrSync(final String url, final Map<String, String> headers) {
        Response<ResponseBody> response = getResponse(url, headers);
        if (response != null && response.isSuccessful() && response.body() != null) {
            try {
                return Optional.of(response.body().string());
            } catch (IOException e) {
                // 区分 IO 异常并打印
                System.err.println("同步 GET 请求异常，url=" + url);
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }
    public Response<ResponseBody> getResponse(final String url, final Map<String, String> headers) {
        if (url == null || url.trim().isEmpty()) {
            System.err.println("url 不能为空");
            return null;
        }

        try {
            final RetrofitApi api = getRetrofit().create(RetrofitApi.class);
            final Response<ResponseBody> response = api.getHtml(url, headers).execute();

            if (response.isSuccessful() && response.body() != null) {
                return response;
            } else {
                // 记录非 200 情况
                System.err.println("请求失败，code=" + response.code() + ", message=" + response.message());
            }
        } catch (IOException e) {
            // 区分 IO 异常并打印
            System.err.println("同步 GET 请求异常，url=" + url);
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 静态内部类持有 Retrofit 实例，首次调用 getInstance 时加载，线程安全
     */
    private static class Holder {
        private static final RetrofitManager INSTANCE = new RetrofitManager();
    }
}

