package cn.edu.hebtu.software.sharemateclient.Bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Reply implements Serializable {

    private int replyId;
    private Comment comment;
    private Reply reply;//当前回复 回复的 回复
    private List<Reply> replyList;//回复了当前回复的回复列表
    private User user;
    private String replyDetail;
    private String replyDate;

    public Reply() {
    }

    public Reply(int replyId, Comment comment, Reply reply,List<Reply> replyList, User user, String replyDetail, String replyDate) {
        this.replyId = replyId;
        this.comment = comment;
        this.reply = reply;
        this.replyList = replyList;
        this.user = user;
        this.replyDetail = replyDetail;
        this.replyDate = replyDate;
    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
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

    public List<Reply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Reply> replyList) {
        this.replyList = replyList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getReplyDetail() {
        return replyDetail;
    }

    public void setReplyDetail(String replyDetail) {
        this.replyDetail = replyDetail;
    }

    public String getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(String replyDate) {
        this.replyDate = replyDate;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "replyId=" + replyId +
                ", comment=" + comment +
                ", reply=" + reply +
                ", replyList=" + replyList +
                ", user=" + user +
                ", replyDetail='" + replyDetail + '\'' +
                ", replyDate='" + replyDate + '\'' +
                '}';
    }
}
