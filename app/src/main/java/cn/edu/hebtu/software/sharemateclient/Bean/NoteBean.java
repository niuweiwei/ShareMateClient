package cn.edu.hebtu.software.sharemateclient.Bean;

import android.graphics.Bitmap;

import java.util.List;;
import cn.edu.hebtu.software.sharemateclient.R;

public class NoteBean {
    private int noId;
    private int noPhoto;
    private String title;
    private int noteImage;
    private String noteDetail,noteTitle;
    private UserBean user;
    private String noteTime;
    private List<CommentBean> comment;
    private int zancount,sharecount,collectcount,pingluncount;

    private String noteImagePath;

    private Bitmap noteImage1;
    private CommentBean commentBean;
    private String zancount1;
    private String commentDetail = "";
    private int zan=R.drawable.xin;
    private int col=R.drawable.xingxing;
    private int fol=R.drawable.cancelfollowedbutton_style;
    private int islike=-1;
    private int iscollect=-1;
    private int isfollow;//1表示关注了该用户 -1表示未关注该用户
    private UserBean userContent;
    private int typeid;
    public NoteBean() {
    }

    public NoteBean(String noteDetail, String noteTitle, UserBean user, int typeid) {
        this.noteDetail = noteDetail;
        this.noteTitle = noteTitle;
        this.user = user;
        this.typeid = typeid;
    }

    public NoteBean(int noteImage, String noteDetail, String noteTitle, UserBean user, String noteTime, int zancount, int sharecount, int collectcount, int pingluncount) {
        this.noteImage = noteImage;
        this.noteDetail = noteDetail;
        this.noteTitle = noteTitle;
        this.user = user;
        this.noteTime = noteTime;
        this.zancount = zancount;
        this.sharecount = sharecount;
        this.collectcount = collectcount;
        this.pingluncount = pingluncount;
    }
    public NoteBean(int noteImage, String noteDetail, String noteTitle,
                    UserBean user, String noteTime, List<CommentBean> comment,
                    int zancount, int sharecount, int collectcount, int pingluncount) {
        this.noteImage = noteImage;
        this.noteDetail = noteDetail;
        this.noteTitle = noteTitle;
        this.user = user;
        this.noteTime = noteTime;
        this.comment = comment;
        this.zancount = zancount;
        this.sharecount = sharecount;
        this.collectcount = collectcount;
        this.pingluncount = pingluncount;
    }
    public NoteBean(int noPhoto, String title) {
        this.noPhoto = noPhoto;
        this.title = title;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public int getNoPhoto() {
        return noPhoto;
    }

    public void setNoPhoto(int noPhoto) {
        this.noPhoto = noPhoto;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String content) {
        this.title = content;
    }

    public int getNoId() {
        return noId;
    }

    public void setNoId(int noId) {
        this.noId = noId;
    }

    public int getNoteImage() {
        return noteImage;
    }

    public void setNoteImage(int noteImage) {
        this.noteImage = noteImage;
    }

    public String getNoteDetail() {
        return noteDetail;
    }

    public void setNoteDetail(String noteDetail) {
        this.noteDetail = noteDetail;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getNoteTime() {
        return noteTime;
    }

    public void setNoteTime(String noteTime) {
        this.noteTime = noteTime;
    }

    public List<CommentBean> getComment() {
        return comment;
    }

    public void setComment(List<CommentBean> comment) {
        this.comment = comment;
    }

    public int getZancount() {
        return zancount;
    }

    public void setZancount(int zancount) {
        this.zancount = zancount;
    }

    public int getSharecount() {
        return sharecount;
    }

    public void setSharecount(int sharecount) {
        this.sharecount = sharecount;
    }

    public int getCollectcount() {
        return collectcount;
    }

    public void setCollectcount(int collectcount) {
        this.collectcount = collectcount;
    }

    public int getPingluncount() {
        return pingluncount;
    }

    public void setPingluncount(int pingluncount) {
        this.pingluncount = pingluncount;
    }

    public String getNoteImagePath() {
        return noteImagePath;
    }

    public void setNoteImagePath(String noteImagePath) {
        this.noteImagePath = noteImagePath;
    }

    public Bitmap getNoteImage1() {
        return noteImage1;
    }

    public void setNoteImage1(Bitmap noteImage1) {
        this.noteImage1 = noteImage1;
    }

    public CommentBean getCommentBean() {
        return commentBean;
    }

    public void setCommentBean(CommentBean commentBean) {
        this.commentBean = commentBean;
    }

    public String getZancount1() {
        return zancount1;
    }

    public void setZancount1(String zancount1) {
        this.zancount1 = zancount1;
    }

    public String getCommentDetail() {
        return commentDetail;
    }

    public void setCommentDetail(String commentDetail) {
        this.commentDetail = commentDetail;
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

    public int isIslike() {
        return islike;
    }

    public void setIslike(int islike) {
        this.islike = islike;
    }

    public int getIscollect() {
        return iscollect;
    }

    public void setIscollect(int iscollect) {
        this.iscollect = iscollect;
    }

    public int getIsfollow() {
        return isfollow;
    }

    public void setIsfollow(int isfollow) {
        this.isfollow = isfollow;
    }

    public UserBean getUserContent() {
        return userContent;
    }

    public void setUserContent(UserBean userContent) {
        this.userContent = userContent;
    }

}
