package com.android.thedognextdoor.SearchMatchesP;

public class Cards {

    private int drawableId;
    private String name;
    private String location;

    public Cards(int drawableId, String name, String location) {
        this.drawableId = drawableId;
        this.name = name;
        this.location = location;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }
}
