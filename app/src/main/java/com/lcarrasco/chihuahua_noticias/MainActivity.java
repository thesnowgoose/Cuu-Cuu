package com.lcarrasco.chihuahua_noticias;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.lcarrasco.data.LoadNews;
import com.lcarrasco.model.News;
import com.tuesda.walker.circlerefresh.CircleRefreshLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements CardsListFragment.OnCardSelected {

    CircleRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final LoadingDialogFragment dialogFragment = new LoadingDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "Sample Fragment");

        LoadNews.buildNewsRequest(this, null, new LoadNews.OnFinishLoading() {
            @Override
            public void onFinishLoading(List<News> newsList) {
                if (dialogFragment.isVisible())
                    dialogFragment.dismiss();

                toolbar.setVisibility(View.VISIBLE);
                try {
                    if (newsList != null && newsList.size() > 0)
                        getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.activity_main, CardsListFragment.getInstance(newsList), getString(R.string.fragment_cards))
                            .commit();
                    else
                        //TODO show fragment of dificuldtades tecnicas
                        System.out.println("No hay noticias nuevas disponibles");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        refreshLayout = (CircleRefreshLayout) findViewById(R.id.refresh_layout);
        if (refreshLayout != null) {
            refreshLayout.setOnRefreshListener(new CircleRefreshLayout.OnCircleRefreshListener() {
                @Override
                public void completeRefresh() { }

                @Override
                public void refreshing() {
                    buildNewsRequest(null);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);

        final MenuItem itemSearch = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(itemSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                buildNewsRequest(query);
                return false;
            }
        });
        MenuItemCompat.setOnActionExpandListener(itemSearch, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                buildNewsRequest(null);
                return true;
            }
        });
        return true;
    }

    private void buildNewsRequest(final String query) {
        LoadNews.buildNewsRequest(getApplicationContext(), query, new LoadNews.OnFinishLoading() {
            @Override
            public void onFinishLoading(List<News> newsList) {
                CardsListFragment cardsListFragment = CardsListFragment.getInstance(newsList);
                cardsListFragment.updateList();
                if (query == null)
                refreshLayout.finishRefreshing();
            }
        });
    }

    @Override
    public void onCardSelected(News clickedNew) {
        Intent intent = new Intent(this, NewsDetailsActivity.class);
        intent.putExtra(getString(R.string.news_details), new Gson().toJson(clickedNew));
        startActivity(intent);
    }
}
