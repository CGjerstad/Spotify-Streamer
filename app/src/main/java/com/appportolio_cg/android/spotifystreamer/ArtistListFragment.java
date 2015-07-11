package com.appportolio_cg.android.spotifystreamer;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.RetrofitError;

/**
 * Created by Selvar on 7/3/2015.
 */
public class ArtistListFragment extends Fragment {

    public ArtistArrayAdapter mArtistAdapter;
    public ArrayList<CustomArtist> customArtistList;

    public ArtistListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey("artistData")) {
            customArtistList = new ArrayList<CustomArtist>();
        } else {
            customArtistList = savedInstanceState.getParcelableArrayList("artistData");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("artistData", customArtistList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.artist_list_fragment_layout, container, false);

        mArtistAdapter = new ArtistArrayAdapter(getActivity(), customArtistList);

        //get an reference to Listview
        ListView listView = (ListView) rootView.findViewById(R.id.artistList_ListView);
        // attach adapter
        listView.setAdapter(mArtistAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                CustomArtist artist = mArtistAdapter.getItem(i);

                Toast toast = Toast.makeText(getActivity().getApplicationContext(), artist.artistName, Toast.LENGTH_SHORT);
                toast.show();

                //Intent topTracksIntent = new Intent(getActivity(), TopTracksActivity.class);
                //startActivity(topTracksIntent);
            }
        });


        //set up the on click listener for the edit text
        final EditText editText = (EditText) rootView.findViewById(R.id.artist_search_editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchArtist(editText.getText().toString());
                    handled = true;

                    // makes the keyboard disappear after the user hits enter
                    InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                }

                return handled;
            }
        });

        return rootView;
    }

    public void searchArtist(String name) {
        SearchArtistTask searchArtist = new SearchArtistTask();
        searchArtist.execute(name);
    }

    public class SearchArtistTask extends AsyncTask<String, Void, ArtistsPager> {

        // Note to self AsyncTask<Params, Progress, Result>

        private final String LOG_TAG = SearchArtistTask.class.getSimpleName();

        @Override
        protected ArtistsPager doInBackground(String... params) {


            ArtistsPager results = null;

            try {
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();

                results = spotify.searchArtists(params[0]);


            } catch (RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage(), error);

            } finally {
                return results;
            }
        }

        @Override
        protected void onPostExecute(ArtistsPager results) {

            // Checks if "results" is null
            // If it isn't it will clear the adapter and procede to check for artist data
            // If the results are null it displays a toast to try again and ask the user -
            // to verify they have a connection ( for example this will prompt if the device is in airplane mode )
            if (results != null) {
                mArtistAdapter.clear();

                Log.i(LOG_TAG, "Test in onPostExecute - inside if statement");


                List<Artist> artists = results.artists.items;

                CustomArtist customArtist;

                // Checks if there are any artists in the artist array.
                // If they are it places each artist into mAdapterArtist
                // If not it displays a toast to inform the user the artist was not found.
                if (artists.size() > 0) {

                    for (int i = 0; i < artists.size(); i++) {

                        // checks to see if the artist object from spotiy has image data
                        // if it does it stores the URL to the image in the customArtist
                        // if not it sets the image URL data to  null;
                        if (artists.get(i).images.size() > 0) {
                            //stores date from spotify to the custom artist object
                            customArtist = new CustomArtist(artists.get(i).name,
                                    artists.get(i).id,
                                    artists.get(i).images.get(0).url);
                        } else {
                            customArtist = new CustomArtist(artists.get(i).name,
                                    artists.get(i).id,
                                    null);
                        }

                        mArtistAdapter.add(customArtist);
                    }

                } else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Unable to find artist", Toast.LENGTH_SHORT);
                    toast.show();
                }


            } else {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Please verify your have a network connection and try again", Toast.LENGTH_SHORT);
                toast.show();

            }
        }

    }
}
