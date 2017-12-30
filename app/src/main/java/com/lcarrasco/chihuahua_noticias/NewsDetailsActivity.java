package com.lcarrasco.chihuahua_noticias;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.lcarrasco.data.NewsUtils;
import com.lcarrasco.model.News;

public class NewsDetailsActivity extends AppCompatActivity {

    private News currentNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        currentNews = new Gson()
                    .fromJson(bundle
                            .getString(getString(R.string.news_details)),
                            News.class);

        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.thumbnail);
        TextView categoryTV = (TextView) findViewById(R.id.category);
        TextView urlSrcTV = (TextView) findViewById(R.id.url);
        TextView titleTV = (TextView) findViewById(R.id.title);
        TextView dateTV = (TextView) findViewById(R.id.date);
        WebView desc = (WebView) findViewById(R.id.newsDesc);
        DocumentView description = (DocumentView) findViewById(R.id.newsDescription);


        draweeView.setImageURI(NewsUtils.getImageUri(currentNews.getThumbnail()));
        categoryTV.setText(currentNews.getCategory());
        urlSrcTV.setText(Html.fromHtml(NewsUtils.getSource(currentNews.getUrl())));
        urlSrcTV.setMovementMethod(LinkMovementMethod.getInstance());
        titleTV.setText(currentNews.getTitle());
        dateTV.setText(NewsUtils.getDate(currentNews.getCreatedAt()));
        desc.loadDataWithBaseURL(null, "<style>img{display: block; height: auto !important; width: 100%;}iframe{display: block; align: center; height: 12em; width: 100%;}body{color:#808080; text-align:justify}</style>" + currentNews.getContent(), "text/html", "charset=UTF-8", null);
        desc.getSettings().setJavaScriptEnabled(true);
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
            case R.id.action_share:
                shareNews();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareNews() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, currentNews.getUrl() + "\n" + getString(R.string.sharedCuuCuu));
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
    }
}
