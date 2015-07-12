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

/**
 * Created by Selvar on 7/11/2015.
 */
public class TrackArrayAdapter extends ArrayAdapter<CustomTrack> {

    public TrackArrayAdapter(Activity context, List<CustomTrack> trackList) {
        super(context, 0, trackList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item_track, parent, false);
        }

        CustomTrack track = getItem(position);

        //load the image view with a artist thumbnail or a default image if the image array is null
        ImageView albumImageView = (ImageView) convertView.findViewById(R.id.list_album_Image);

        if (track.albumArtURL == null) {
            albumImageView.setImageResource(R.drawable.missing_image);
        } else {

            String url = track.albumArtURL;

            Picasso.with(getContext()).load(url)
                    .into((ImageView) convertView.findViewById(R.id.list_album_Image));
        }

        //load the text views with the album and track names
        TextView trackNameView = (TextView) convertView.findViewById(R.id.list_track_Name);
        trackNameView.setText(track.trackName);

        TextView albumNameView = (TextView) convertView.findViewById(R.id.list_album_Name);
        albumNameView.setText(track.albumName);

        return convertView;
    }
}
