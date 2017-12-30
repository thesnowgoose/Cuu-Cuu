package com.lcarrasco.chihuahua_noticias;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.lcarrasco.model.News;

import java.util.List;

public class CardsListFragment extends Fragment {

    private static CardsListFragment instance;
    static OnCardSelected cardSelectedListener;
    static OnListBottomReached bottomReachedListener;
    private static List<News> newsList;
    private CardAdapter adapter;
    private int lastid;

    private Button btn_toTop;

    private int mAnimationDuration;
    private boolean isButtonToTopVisible;

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

        mAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);
        isButtonToTopVisible = false;
        final Activity activity = getActivity();
        final View view = inflater.inflate(R.layout.fragment_card_list, container, false);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        btn_toTop = (Button) view.findViewById(R.id.btn_toTop);
        btn_toTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.getLayoutManager().scrollToPosition(0);
            }
        });
        adapter = new CardAdapter(activity, newsList);
        try {
            recyclerView.setHasFixedSize(true);
            final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition() == 0)
                        btn_toTop.setVisibility(View.GONE);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager)
                            .findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION &&
                        lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1) {
                        lastid = adapter.getLastID();
                        bottomReachedListener.onListBottomReached(lastid);
                    }

                    if(dy < 0) { // If scrolls upwards
                        if (!isButtonToTopVisible) {
                            fadeIn();
                        }
                    }
                    else {
                        fadeOut();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private void fadeIn() {
        btn_toTop.setAlpha(0f);
        btn_toTop.setVisibility(View.VISIBLE);
        btn_toTop.animate()
                 .alpha(1f)
                 .setDuration(mAnimationDuration)
                 .setListener(new AnimatorListenerAdapter() {
                     @Override
                     public void onAnimationEnd(Animator animation) {
                         isButtonToTopVisible = true;
                     }
                 });
    }

    private void fadeOut(){
        btn_toTop.animate()
                .alpha(0f)
                .setDuration(mAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isButtonToTopVisible = false;
                        btn_toTop.setVisibility(View.GONE);
                    }
                });
    }

    public void updateList(boolean increaseList) {
        if (adapter != null) {
            adapter.setNewList(increaseList, newsList, getContext());
            adapter.notifyDataSetChanged();
        }
    }

}
