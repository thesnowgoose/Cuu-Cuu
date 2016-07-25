package com.lcarrasco.chihuahua_noticias;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcarrasco.data.LoadNews;
import com.lcarrasco.model.News;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CardsListFragment extends Fragment {

    private static CardsListFragment instance;
    static OnCardSelected cardSelectedListener;
    static OnListBottomReached bottomReachedListener;
    private static List<News> newsList;
    private CardAdapter adapter;
    private int lastid;

    public static CardsListFragment getInstance(List<News> news) {
        if (instance == null)
            instance = new CardsListFragment();
        newsList = news;

        return instance;
    }

    public CardsListFragment() {
    }

    public interface OnCardSelected {
        void onCardSelected(News clickedNew);
    }

    public interface OnListBottomReached {
        void onListBottomReached(int id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnCardSelected)
            cardSelectedListener = (OnCardSelected) context;
        if (context instanceof  OnListBottomReached)
            bottomReachedListener = (OnListBottomReached) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final Activity activity = getActivity();
        final View view = inflater.inflate(R.layout.fragment_card_list, container, false);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new CardAdapter(activity, newsList);
        try {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(adapter);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                            .findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION &&
                        lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1) {
                        lastid = adapter.getLastID();
                        bottomReachedListener.onListBottomReached(lastid);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    public void updateList(boolean increaseList) {
        if (adapter != null) {
            adapter.setNewList(increaseList, newsList, getContext());
            adapter.notifyDataSetChanged();
        }
    }

}
