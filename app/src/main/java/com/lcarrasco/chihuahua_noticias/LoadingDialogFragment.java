package com.lcarrasco.chihuahua_noticias;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by lcarrasco on 7/7/16.
 */
public class LoadingDialogFragment extends DialogFragment{

    ProgressBar spinner;
    TextView tv_loading;
    Button btn_reload;

    static RetryRequest retryRequest;

    private static LoadingDialogFragment instance;

    static LoadingDialogFragment newInstance(){
        if (instance == null)
            instance = new LoadingDialogFragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int style = DialogFragment.STYLE_NORMAL,
            theme = android.R.style.Theme_Holo_Light;

        setStyle(style, theme);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof RetryRequest)
            retryRequest = (RetryRequest) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loading, container, false);
        spinner = (ProgressBar) view.findViewById(R.id.spinner);
        tv_loading = (TextView) view.findViewById(R.id.loading);
        btn_reload = (Button) view.findViewById(R.id.reload);

        spinner.setVisibility(View.VISIBLE);
        tv_loading.setVisibility(View.VISIBLE);
        btn_reload.setVisibility(View.GONE);

        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retryRequest.retryRequest();
            }
        });

        return view;
    }

    public void showReloadButton() {
        spinner.setVisibility(View.GONE);
        tv_loading.setVisibility(View.GONE);
        btn_reload.setVisibility(View.VISIBLE);
    }

    public interface RetryRequest {
        void retryRequest();
    };
}
