package cn.edu.hebtu.software.sharemateclient.Bean;

import java.io.Serializable;

public class CommentBean implements Serializable{

    public static final int COMMENT = 0;
    public static final int REPLYCOMMENT = 1;
    public static final int REPLYREPLY =2;

    private int tag;//标志判断展示的是其他用户对“我的”笔记的评论，还是对我的评论的回复
    private UserBean user;//发布评论或者回复的人
    private String comment;//回复的内容
    private int notePhoto;//回复的相关笔记的图片
    private String name;//被回复的人的昵称
    private String argued;//被回复的内容
    private String commentDetail;

    private int id;
    private String date;//发布的时间
    private int noteId;//回复的相关笔记的id
    private String notePhotoPath;
    private int userId;
    private int arguedId;//被回复的评论id或被回复的回复id
    private boolean isLike;//当前用户是否对该评论进行点赞


    public CommentBean() {
    }

    public CommentBean(int tag, UserBean user, String date, String comment, int notePhoto, String name, String argued) {
        this.tag = tag;
        this.user = user;
        this.date = date;
        this.comment = comment;
        this.notePhoto = notePhoto;
        this.name = name;
        this.argued = argued;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getNotePhoto() {
        return notePhoto;
    }

    public void setNotePhoto(int notePhoto) {
        this.notePhoto = notePhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArgued() {
        return argued;
    }

    public void setArgued(String argued) {
        this.argued = argued;
    }

    public String getCommentDetail() {
        return commentDetail;
    }

    public void setCommentDetail(String commentDetail) {
        this.commentDetail = commentDetail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNotePhotoPath() {
        return notePhotoPath;
    }

    public void setNotePhotoPath(String notePhotoPath) {
        this.notePhotoPath = notePhotoPath;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getArguedId() {
        return arguedId;
    }

    public void setArguedId(int arguedId) {
        this.arguedId = arguedId;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }
}
