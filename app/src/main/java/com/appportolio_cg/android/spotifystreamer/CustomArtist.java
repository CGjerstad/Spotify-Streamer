package com.appportolio_cg.android.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Selvar on 7/11/2015.
 *
 * This is a parcelable used to store the results from spotify
 */


public class CustomArtist implements Parcelable {

    public final Parcelable.Creator<CustomArtist> CREATOR = new Parcelable.Creator<CustomArtist>() {
        @Override
        public CustomArtist createFromParcel(Parcel parcel) {
            return new CustomArtist(parcel);
        }

        @Override
        public CustomArtist[] newArray(int i) {
            return new CustomArtist[i];
        }
    };

    String artistName;
    String artistId;
    String artistImageURL;

    public CustomArtist(String name, String id, String imageURL) {
        this.artistName = name;
        this.artistId = id;
        this.artistImageURL = imageURL;
    }

    private CustomArtist(Parcel in) {
        artistName = in.readString();
        artistId = in.readString();
        artistImageURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artistName);
        dest.writeString(artistId);
        dest.writeString(artistImageURL);
    }
}
