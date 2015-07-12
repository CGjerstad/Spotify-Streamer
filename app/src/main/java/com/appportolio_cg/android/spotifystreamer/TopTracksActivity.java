package com.appportolio_cg.android.spotifystreamer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by Selvar on 7/9/2015.
 */
public class TopTracksActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_tracks);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            TopTrackActivity_Fragment topTrackActivity_fragment = new TopTrackActivity_Fragment();

            fragmentTransaction.add(R.id.top_tracks_activity_Fragment_Container, topTrackActivity_fragment);
            fragmentTransaction.commit();
        }
    }

}
