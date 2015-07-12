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

        //get an reference to Listview
        ListView listView = (ListView) rootView.findViewById(R.id.toptrack_listview);
        // attach adapter
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

            try {
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();


                // Thanks to my Udacity peers on the forums for sort out this country code stuff
                Map<String, Object> options = new HashMap<>();
                options.put(SpotifyService.COUNTRY, Locale.getDefault().getCountry());

                results = spotify.getArtistTopTrack(params[0], options);

            } catch (RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage(), error);
            } finally {
                return results;
            }
        }

        @Override
        protected void onPostExecute(Tracks results) {

            // Checks if "results" is null
            // If it isn't it will clear the adapter and procede to check for artist data
            // If the results are null it displays a toast to try again and ask the user -
            // to verify if they have a connection ( for example this will prompt if the device is in airplane mode )
            if (results != null) {

                mTrackAdapter.clear();
                List<Track> tracks = results.tracks;
                CustomTrack customTrack;

                // Checks if there are any tracks in the track array.
                // If they are it places each track into mAdapterArtist
                // If not it displays a toast to inform the user no tracks were found
                if (tracks.size() > 0) {

                    for (int i = 0; i < tracks.size(); i++) {

                        // checks to see if the album object ( in the track object ) from spotiy has image data
                        // if it does it stores the URL to the image in the customTrack
                        // if not it sets the image URL data to  null;
                        if (tracks.get(i).album.images.size() > 0) {

                            //stores date from spotify to the custom track object
                            // This first part is set to null due to a bug causing a crash
                            // I am working on cleaning it up for Project 2
                            customTrack = new CustomTrack((null),
                                    tracks.get(i).name,
                                    tracks.get(i).album.name,
                                    tracks.get(i).album.images.get(0).url,
                                    tracks.get(i).id);

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
