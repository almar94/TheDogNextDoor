package com.android.thedognextdoor.PhotoAlbumP;

import com.google.firebase.database.Exclude;

public class Upload {
    private String name;
    private String photoUrl;
    private String mKey;

    public Upload() {
    }

    public Upload(String name, String photoUrl) {
        if (name.trim().equals("")) {
            this.name = " no name";
        }
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }

    public void setKey(String Key) {
        this.mKey = Key;
    }
}
