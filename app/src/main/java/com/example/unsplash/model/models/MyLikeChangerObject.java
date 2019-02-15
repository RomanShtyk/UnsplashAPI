package com.example.unsplash.model.models;

public class MyLikeChangerObject {

    private String id;
    private boolean isLiked;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public MyLikeChangerObject(String id, boolean isLiked, int position) {
        this.id = id;
        this.isLiked = isLiked;
        this.position = position;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
