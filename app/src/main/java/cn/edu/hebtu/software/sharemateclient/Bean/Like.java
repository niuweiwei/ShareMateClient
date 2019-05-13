package cn.edu.hebtu.software.sharemateclient.Bean;

import java.util.Date;

public class Like {

    private int likeId;
    private User user;
    private Note note;
    private String likeDate;

    public Like() { }

    public Like(int likeId, User user, Note note,String date) {
        this.likeId = likeId;
        this.user = user;
        this.note = note;
        this.likeDate = date;
    }

    public int getLikeId() {
        return likeId;
    }

    public void setLikeId(int likeId) {
        this.likeId = likeId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public String getDate() {
        return likeDate;
    }

    public void setLikeDate(String date) {
        this.likeDate = date;
    }

    @Override
    public String toString() {
        return "Like{" +
                "likeId=" + likeId +
                ", user=" + user +
                ", note=" + note +
                ", likeDate=" + likeDate +
                '}';
    }
}
