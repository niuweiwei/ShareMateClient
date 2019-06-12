package cn.edu.hebtu.software.sharemateclient.Bean;

import java.io.Serializable;
import java.util.Date;

public class Like implements Serializable {

    private int likeId;
    private UserBean user;
    private NoteBean note;
    private CommentBean comment;
    private Reply reply;
    private String likeDate;
    private int likeType;
    private int followCount;
    private int fanCount;
    private int likeCount;
    public static final int NOTE = 0;
    public static final  int COMMENT = 1;
    public static final  int REPLY = 2;

    public Like() { }

    public Like(int likeId, UserBean user, NoteBean note, CommentBean comment, Reply reply, String likeDate) {
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

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public NoteBean getNote() {
        return note;
    }

    public void setNote(NoteBean note) {
        this.note = note;
    }

    public CommentBean getComment() {
        return comment;
    }

    public void setComment(CommentBean comment) {
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

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public int getFanCount() {
        return fanCount;
    }

    public void setFanCount(int fanCount) {
        this.fanCount = fanCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
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
