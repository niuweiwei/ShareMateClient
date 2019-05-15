package cn.edu.hebtu.software.sharemateclient.Bean;

import java.io.Serializable;
import java.util.Date;

public class Like implements Serializable {

    private int likeId;
    private User user;
    private Note note;
    private Comment comment;
    private Reply reply;
    private String likeDate;
    private int likeType;
    public static final int NOTE = 0;
    public static final  int COMMENT = 1;
    public static final  int REPLY = 2;

    public Like() { }

    public Like(int likeId, User user, Note note, Comment comment, Reply reply, String likeDate) {
        this.likeId = likeId;
        this.user = user;
        this.note = note;
        this.comment = comment;
        this.reply = reply;
        this.likeDate = likeDate;
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

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Reply getReply() {
        return reply;
    }

    public void setReply(Reply reply) {
        this.reply = reply;
    }

    public String getLikeDate() {
        return likeDate;
    }

    public void setLikeDate(String likeDate) {
        this.likeDate = likeDate;
    }

    public int getLikeType() {
        return likeType;
    }

    public void setLikeType(int likeType) {
        this.likeType = likeType;
    }

    @Override
    public String toString() {
        return "Like{" +
                "likeId=" + likeId +
                ", user=" + user +
                ", note=" + note +
                ", comment=" + comment +
                ", reply=" + reply +
                ", likeDate=" + likeDate +
                '}';
    }
}
