package com.lcarrasco.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by lcarrasco on 7/6/16.
 */
public interface INewsApp {
    @GET("api/articles")
    Call<List<News>>getNewsList();

    @GET("api/search/{text}")
    Call<List<News>>getSearchedNewsList(@Path("text") String text);
}
