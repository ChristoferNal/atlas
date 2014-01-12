package com.christofer.atlas.splashscreen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.christofer.atlas.R;

/**
 * @author Christoforos Nalmpantis
 *         This Fragment is responsible for showing the splash screen.
 */
public class SplashScreenFragment extends Fragment {

    // Variables.
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.splash_screen, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        return view;
    }

    /**
     * Sets the progress of the {@link android.widget.ProgressBar}.
     *
     * @param progress The new progress with values 0-100.
     */
    public void setProgress(int progress) {
        //TODO: Needs to throw exception if progress value is <0 or >100.
        progressBar.setProgress(progress);
    }

}
