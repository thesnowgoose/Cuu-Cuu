package com.lcarrasco.chihuahua_noticias;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * Created by lcarrasco on 7/7/16.
 */
public class LoadingDialogFragment extends DialogFragment{

    ProgressBar spinner;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loading, container, false);
        spinner = (ProgressBar) view.findViewById(R.id.spinner);
        return view;
    }
}
