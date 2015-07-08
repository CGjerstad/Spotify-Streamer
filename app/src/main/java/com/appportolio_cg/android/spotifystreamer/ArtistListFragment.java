package com.appportolio_cg.android.spotifystreamer;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * Created by Selvar on 7/3/2015.
 */
public class ArtistListFragment extends Fragment {

    public ArtistArrayAdapter mArtistAdapter;

    public ArtistListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.artist_list_fragment_layout, container, false);

        mArtistAdapter = new ArtistArrayAdapter(getActivity(), new ArrayList<Artist>());

        //get an reference to Listview
        ListView listView = (ListView) rootView.findViewById(R.id.artistList_ListView);
        // attach adapter
        listView.setAdapter(mArtistAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        SearchArtistTask searchArtist = new SearchArtistTask();
        searchArtist.execute("coldplay");
    }

    public class SearchArtistTask extends AsyncTask<String, Void, ArtistsPager> {

        // Note to self AsyncTask<Params, Progress, Result>

        private final String LOG_TAG = SearchArtistTask.class.getSimpleName();

        @Override
        protected ArtistsPager doInBackground(String... params) {

            Log.i(LOG_TAG, "Test in doInBackground");

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            Log.i(LOG_TAG, "Test in doInBackground, params = " + params);

            ArtistsPager results = spotify.searchArtists(params[0]);

            //add some logging here to test
            List<Artist> artists = results.artists.items;
            Log.i(LOG_TAG, "Test in doInBackground - Size of array = " + artists.size());
            for (int i = 0; i < artists.size(); i++) {
                Artist artist = artists.get(i);
                Log.i(LOG_TAG, "Test - " + i + " " + artist.name);
            }

            return results;
        }

        @Override
        protected void onPostExecute(ArtistsPager results) {
            Log.i(LOG_TAG, "Test in onPostExecute - outside if statement");
            if (results != null) {
                mArtistAdapter.clear();

                Log.i(LOG_TAG, "Test in onPostExecute - inside if statement");

                List<Artist> artists = results.artists.items;
                for (int i = 0; i < artists.size(); i++) {
                    mArtistAdapter.add(artists.get(i));
                    Log.i(LOG_TAG, "Test in onPostExecute - " + i);
                }
            } else {
                Log.i(LOG_TAG, "Test in onPostExecute - results were null");
            }
        }

    }
}
