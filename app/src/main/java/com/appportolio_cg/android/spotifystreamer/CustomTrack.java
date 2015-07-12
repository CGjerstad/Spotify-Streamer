package com.appportolio_cg.android.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Selvar on 7/11/2015.
 */
public class CustomTrack implements Parcelable {

    public final Parcelable.Creator<CustomTrack> CREATOR = new Parcelable.Creator<CustomTrack>() {
        @Override
        public CustomTrack createFromParcel(Parcel parcel) {
            return new CustomTrack(parcel);
        }

        @Override
        public CustomTrack[] newArray(int i) {
            return new CustomTrack[i];
        }
    };

    String artistName;
    String trackName;
    String albumName;
    String albumArtURL;
    String albumID;

    public CustomTrack(String artName, String traName, String albName, String imageURL, String id) {
        this.artistName = artName;
        this.trackName = traName;
        this.albumName = albName;
        this.albumArtURL = imageURL;
        this.albumID = id;
    }

    private CustomTrack(Parcel in) {
        artistName = in.readString();
        trackName = in.readString();
        albumName = in.readString();
        albumArtURL = in.readString();
        albumID = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artistName);
        dest.writeString(trackName);
        dest.writeString(albumName);
        dest.writeString(albumArtURL);
        dest.writeString(albumID);
    }
}
