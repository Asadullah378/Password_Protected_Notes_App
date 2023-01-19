package com.asadullahnawaz.i200761;

public class NoteModel {

    int id;
    String title;
    String description;
    String date;
    String date_updated;
    String image;
    boolean lock_status;
    String password;
    int category_id;
    String color;

    public NoteModel(int id, String title, String description, String date,String date_updated, String image, boolean lock_status, String password, int category_id, String color) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.date_updated = date_updated;
        this.image = image;
        this.lock_status = lock_status;
        this.password = password;
        this.category_id = category_id;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isLock_status() {
        return lock_status;
    }

    public void setLock_status(boolean lock_status) {
        this.lock_status = lock_status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
