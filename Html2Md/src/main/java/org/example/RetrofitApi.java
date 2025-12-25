package org.example;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Url;

import java.util.Map;

public interface RetrofitApi {
    @GET
    Call<ResponseBody> getHtml(@Url String url, @HeaderMap Map<String,String> headers);
    @GET
    Observable<ResponseBody> getHtmlByRx(@Url String url, @HeaderMap Map<String,String> headers);
}
