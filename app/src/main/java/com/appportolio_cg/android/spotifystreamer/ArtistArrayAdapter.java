package com.appportolio_cg.android.spotifystreamer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;


/**
 * Created by Selvar on 7/6/2015.
 */
public class ArtistArrayAdapter extends ArrayAdapter<Artist> {

    private static final String LOG_TAG = ArtistArrayAdapter.class.getSimpleName();

    public ArtistArrayAdapter(Activity context, List<Artist> artistList) {
        super(context, 0, artistList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item_artist, parent, false);
        }

        Artist artist = getItem(position);

        //load the image view with a artist thumbnail or a default image if the image array is null
        ImageView artistImageView = (ImageView) convertView.findViewById(R.id.list_Artist_Image);

        if (artist.images.size() == 0) {
            artistImageView.setImageResource(R.drawable.missing_image);
        } else {

            String url = artist.images.get(0).url;

            Picasso.with(getContext()).load(url)
                    .into((ImageView) convertView.findViewById(R.id.list_Artist_Image));
        }

        //load the text view with the artists name
        TextView artistNameView = (TextView) convertView.findViewById(R.id.list_Artist_Name);
        artistNameView.setText(artist.name);


        return convertView;
    }
}
