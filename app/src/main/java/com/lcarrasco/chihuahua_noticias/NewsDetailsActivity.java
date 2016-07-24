package com.lcarrasco.chihuahua_noticias;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;
import com.bluejamesbond.text.style.TextAlignment;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.lcarrasco.data.NewsUtils;
import com.lcarrasco.model.News;

public class NewsDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        News currentNews = new Gson()
                    .fromJson(bundle
                            .getString(getString(R.string.news_details)),
                            News.class);

        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.thumbnail);
        TextView categoryTV = (TextView) findViewById(R.id.category);
        TextView urlSrcTV = (TextView) findViewById(R.id.url);
        TextView titleTV = (TextView) findViewById(R.id.title);
        TextView dateTV = (TextView) findViewById(R.id.date);
        TextView newsDesc = (TextView) findViewById(R.id.newsDesc);
        DocumentView description = (DocumentView) findViewById(R.id.newsDescription);


        draweeView.setImageURI(NewsUtils.getImageUri(currentNews.getThumbnail()));
        categoryTV.setText(currentNews.getCategory());
        urlSrcTV.setText(NewsUtils.getSource(currentNews.getUrl()));
        titleTV.setText(currentNews.getTitle());
        dateTV.setText(NewsUtils.getDate(currentNews.getCreatedAt()));
        newsDesc.setText(Html.fromHtml(currentNews.getContent()));
//        newsDesc.getFontFeatureSettings();
        description.setText(Html.fromHtml(currentNews.getContent()));


        if (currentNews.getCategory() == null || currentNews.getCategory().isEmpty())
            categoryTV.setVisibility(View.GONE);
        else
            categoryTV.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
