package com.appportolio_cg.android.spotifystreamer;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;

/**
 * Created by Selvar on 7/11/2015.
 */
public class TopTrackActivity_Fragment extends Fragment {

    public TrackArrayAdapter mTrackAdapter;
    public ArrayList<CustomTrack> customTrackList;

    public TopTrackActivity_Fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey("trackData")) {
            customTrackList = new ArrayList<CustomTrack>();
        } else {
            customTrackList = savedInstanceState.getParcelableArrayList("trackData");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("trackData", customTrackList);
        super.onSaveInstanceState(outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_toptracks, container, false);

        mTrackAdapter = new TrackArrayAdapter(getActivity(), customTrackList);

        ListView listView = (ListView) rootView.findViewById(R.id.toptrack_listview);
        listView.setAdapter(mTrackAdapter);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {

            // gets the top tracks useing the artist ID passed in by the intent
            getTopTracks(intent.getStringExtra(intent.EXTRA_TEXT));
        }

        return rootView;
    }

    // call this method to get the top tracks by passing in a artist ID
    public void getTopTracks(String id) {
        TopTracksTask topTracksTask = new TopTracksTask();
        topTracksTask.execute(id);
    }

    public class TopTracksTask extends AsyncTask<String, Void, Tracks> {

        private final String LOG_TAG = TopTracksTask.class.getSimpleName();

        @Override
        protected Tracks doInBackground(String... params) {

            Tracks results = null;

            Log.i(LOG_TAG, "Tracks doInBackground param passed - " + params[0]);

            try {
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();

                Map<String, Object> options = new HashMap<>();
                options.put(SpotifyService.COUNTRY, Locale.getDefault().getCountry());
                //options.put("country", "US");

                results = spotify.getArtistTopTrack(params[0], options);

                Log.i(LOG_TAG, "Tracks doInBackground - " + results.tracks.size());

            } catch (RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage(), error);
            } finally {
                return results;
            }
        }

        @Override
        protected void onPostExecute(Tracks results) {

            if (results != null) {

                mTrackAdapter.clear();

                Log.i(LOG_TAG, "Tracks oPostExecute passed null check");

                List<Track> tracks = results.tracks;

                CustomTrack customTrack;

                if (tracks.size() > 0) {

                    Log.i(LOG_TAG, "Tracks oPostExecute passed tracks.size check");

                    for (int i = 0; i < tracks.size(); i++) {

                        if (tracks.get(i).album.images.size() > 0) {

                            customTrack = new CustomTrack((null),
                                    tracks.get(i).name,
                                    tracks.get(i).album.name,
                                    tracks.get(i).album.images.get(0).url,
                                    tracks.get(i).id);

                            //Log.i(LOG_TAG, "Tracks oPostExecute inside" + tracks.get(i).artists.get(i).name);
                            Log.i(LOG_TAG, "Tracks oPostExecute inside" + tracks.get(i).name);
                            Log.i(LOG_TAG, "Tracks oPostExecute inside" + tracks.get(i).album.name);
                            Log.i(LOG_TAG, "Tracks oPostExecute inside" + tracks.get(i).album.images.get(0).url);
                            Log.i(LOG_TAG, "Tracks oPostExecute inside" + tracks.get(i).id);

                        } else {
                            customTrack = new CustomTrack((tracks.get(i).artists.get(i).name),
                                    tracks.get(i).name,
                                    tracks.get(i).album.name,
                                    null,
                                    tracks.get(i).id);
                        }

                        mTrackAdapter.add(customTrack);

                    }

                } else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext()
                            , "Unable to find any Tracks", Toast.LENGTH_SHORT);
                    toast.show();
                }

            } else {
                Toast toast = Toast.makeText(getActivity().getApplicationContext()
                        , "Please verify your have a network connection and try again", Toast.LENGTH_SHORT);
                toast.show();
            }

        }
    }
}
