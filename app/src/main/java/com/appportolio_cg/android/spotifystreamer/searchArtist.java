package com.appportolio_cg.android.spotifystreamer;

/*public class searchArtist extends AsyncTask<String, Void, List<Artist>> {

    *//* Note to self AsyncTask<Params, Progress, Result>*//*

    private static final String LOG_TAG = searchArtist.class.getSimpleName();

    @Override
    protected List<Artist> doInBackground(String... params) {

        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        ArtistsPager results = spotify.searchArtists(params.toString());

        //add some logging here to test
        List<Artist> artists = results.artists.items;
        for (int i = 0; i < artists.size(); i++){
            Artist artist = artists.get(i);
            Log.i(LOG_TAG, i + " " + artist.name);
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Artist> artistList){
        if (artistList != null){

        }
    }

}*/
