package cn.edu.hebtu.software.sharemateclient.Bean;

import android.graphics.Bitmap;

import org.w3c.dom.Comment;

import java.io.Serializable;
import java.util.List;;
import cn.edu.hebtu.software.sharemateclient.R;

public class NoteBean implements Serializable {
    //薇薇
    private int noteId;
    private String noteTitle;
    private String noteDetail;
    private String noteImage;
    private String noteDate;
    //笔记类型
    private int typeId;
    //笔记的发布者
    private UserBean user;

    //春柳
    private String noteVideo;
    private String noteAddress;
    private int likeCount,collectCount,commentCount;
    private boolean like;
    private boolean collect;
    private boolean follow;

    //付娆
    private String title;
    private int noteLikeCount,noteCollectionCount,noteCommentCount;
    private String noteImagePath;
    private int zan=R.drawable.xin;
    private int col=R.drawable.xingxing;
    private int fol=R.drawable.cancelfollowedbutton_style;
    private int typeid;
    private String commentdetial;
    private String userImage;
    private String userName;
    private int zanTag;
    private int collectTag;
    private String commentUserImage;

    public String getCommentUserImage() {
        return commentUserImage;
    }

    public void setCommentUserImage(String commentUserImage) {
        this.commentUserImage = commentUserImage;
    }

    public int getZanTag() {
        return zanTag;
    }

    public void setZanTag(int zanTag) {
        this.zanTag = zanTag;
    }

    public int getCollectTag() {
        return collectTag;
    }

    public void setCollectTag(int collectTag) {
        this.collectTag = collectTag;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDetail() {
        return noteDetail;
    }

    public void setNoteDetail(String noteDetail) {
        this.noteDetail = noteDetail;
    }

    public String getNoteImage() {
        return noteImage;
    }

    public void setNoteImage(String noteImage) {
        this.noteImage = noteImage;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getNoteVideo() {
        return noteVideo;
    }

    public void setNoteVideo(String noteVideo) {
        this.noteVideo = noteVideo;
    }

    public String getNoteAddress() {
        return noteAddress;
    }

    public void setNoteAddress(String noteAddress) {
        this.noteAddress = noteAddress;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public boolean isCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNoteLikeCount() {
        return noteLikeCount;
    }

    public void setNoteLikeCount(int noteLikeCount) {
        this.noteLikeCount = noteLikeCount;
    }

    public int getNoteCollectionCount() {
        return noteCollectionCount;
    }

    public void setNoteCollectionCount(int noteCollectionCount) {
        this.noteCollectionCount = noteCollectionCount;
    }

    public int getNoteCommentCount() {
        return noteCommentCount;
    }

    public void setNoteCommentCount(int noteCommentCount) {
        this.noteCommentCount = noteCommentCount;
    }

    public String getNoteImagePath() {
        return noteImagePath;
    }

    public void setNoteImagePath(String noteImagePath) {
        this.noteImagePath = noteImagePath;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getFol() {
        return fol;
    }

    public void setFol(int fol) {
        this.fol = fol;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public String getCommentdetial() {
        return commentdetial;
    }

    public void setCommentdetial(String commentdetial) {
        this.commentdetial = commentdetial;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
