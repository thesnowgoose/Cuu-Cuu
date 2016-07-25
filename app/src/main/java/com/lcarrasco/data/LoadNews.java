package com.lcarrasco.data;

import android.content.Context;
import android.widget.Toast;

import com.lcarrasco.chihuahua_noticias.ChihuahuaNoticiasApp;
import com.lcarrasco.chihuahua_noticias.R;
import com.lcarrasco.model.INewsApp;
import com.lcarrasco.model.News;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lcarrasco on 7/7/16.
 */
public class LoadNews {

    private static OnFinishLoading finish;
    private static List<News> newsList;

    public interface OnFinishLoading {
        void onFinishLoading(List<News> newsList);
    }

    public static void buildNewsRequest(Context context, String query, int id, final OnFinishLoading loading) {
        finish = loading;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Call<List<News>> newsListCall;

        if (id != 0)
            newsListCall = retrofit.create(INewsApp.class)
                    .getNextGroupOfNews(id);
        else if (query == null)
            newsListCall = retrofit.create(INewsApp.class)
                    .getNewsList();
        else
            newsListCall = retrofit.create(INewsApp.class)
                    .getSearchedNewsList(query);

        doRequest(context, newsListCall);
    }

    private static void doRequest(final Context context, Call<List<News>> newsListCall) {
        newsListCall.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if (!response.isSuccessful())
                    Toast.makeText(context, context.getString(R.string.msg_apiFailed), Toast.LENGTH_SHORT).show();
                else
                    newsList = response.body();
                finishLoading();
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                Toast.makeText(context, context.getString(R.string.msg_apiFailed), Toast.LENGTH_SHORT).show();
                finishLoading();
            }
        });
    }

    private static void finishLoading() {
        finish.onFinishLoading(newsList);
    }
}
