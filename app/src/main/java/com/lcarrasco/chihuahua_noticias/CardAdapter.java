package com.lcarrasco.chihuahua_noticias;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lcarrasco.data.NewsUtils;
import com.lcarrasco.model.News;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lcarrasco on 7/6/16.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<News> newsList;

    public CardAdapter(Context context, List<News> news){
        newsList = news;
        layoutInflater = LayoutInflater.from(context);
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private SimpleDraweeView draweeView;
        private TextView categoryTV;
        private TextView urlSrcTV;
        private TextView titleTV;
        private TextView dateTV;

        protected ViewHolder(View itemView) {
            super(itemView);

            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.thumbnail);
            categoryTV = (TextView) itemView.findViewById(R.id.category);
            urlSrcTV = (TextView) itemView.findViewById(R.id.url);
            titleTV = (TextView) itemView.findViewById(R.id.title);
            dateTV = (TextView) itemView.findViewById(R.id.date);
        }

        protected void setData(Uri imageUrl, String category, String urlSource, String title, String date){

            draweeView.setImageURI(imageUrl);
            categoryTV.setText(category);
            urlSrcTV.setText(Html.fromHtml(urlSource));
            urlSrcTV.setMovementMethod(LinkMovementMethod.getInstance());
            titleTV.setText(title);
            dateTV.setText(date);

            if (category == null || category.isEmpty())
                categoryTV.setVisibility(View.GONE);
            else
                categoryTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater
            .inflate(R.layout.card_item_new, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        try {
            final News currentNew = newsList.get(position);
            holder.setData(
                    NewsUtils.getImageUri(currentNew.getThumbnail()),
                    newsList.get(position).getCategory(),
                    NewsUtils.getSource(currentNew.getUrl()),
                    newsList.get(position).getTitle(),
                    NewsUtils.getDate(currentNew.getCreatedAt()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CardsListFragment.cardSelectedListener.onCardSelected(currentNew);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void setNewList(boolean increaseList, List<News> models, Context context) {
        if (increaseList) {
            newsList = new ArrayList<>(newsList);
            newsList.addAll(models);
        } else
            newsList = new ArrayList<>(models);

        if (newsList.size() == 0)
            Toast.makeText(context, context.getString(R.string.noNewsDetected), Toast.LENGTH_SHORT).show();
    }

    int getLastID() {
        return newsList.get(newsList.size() - 1).getArticleId();
    }
}
