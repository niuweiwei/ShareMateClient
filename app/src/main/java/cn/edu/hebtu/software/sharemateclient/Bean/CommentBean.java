package cn.edu.hebtu.software.sharemateclient.Bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentBean {
    //薇薇
    private int commentId;
    private String commentDetail;
    private String commentDate;
    private UserBean user;
    private NoteBean note;


    //春柳
    private int noteId;
    private List<Reply> replyList=new ArrayList<>();
    private boolean like;

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getCommentDetail() {
        return commentDetail;
    }

    public void setCommentDetail(String commentDetail) {
        this.commentDetail = commentDetail;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
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

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public List<Reply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Reply> replyList) {
        this.replyList = replyList;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}
