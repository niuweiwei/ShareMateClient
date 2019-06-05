package cn.edu.hebtu.software.sharemateclient.Bean;

import android.graphics.Bitmap;

import org.w3c.dom.Comment;

import java.util.List;;
import cn.edu.hebtu.software.sharemateclient.R;

public class NoteBean {
    private int noteId;
    private String noPhoto;
    private String title;
    private String noteImage;
    private String noteDetail,noteTitle;
    private UserBean user;
    private String noteTime;
    private int noteLikeCount,sharecount,noteCollectionCount,noteCommentCount;

    private String noteImagePath;

    private Bitmap noteImage1;
    private Comment comment;
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
    private String commentdetial;
    private String userImage;
    private String userName;
//嘉星-------------------------------------
    private Type type;
    private String noteDate;
    //----------------------------------

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getCommentdetial() {
        return commentdetial;
    }

    public void setCommentdetial(String commentdetial) {
        this.commentdetial = commentdetial;
    }

    public NoteBean() {
    }

    public NoteBean(String noteDetail, String noteTitle, UserBean user, int typeid) {
        this.noteDetail = noteDetail;
        this.noteTitle = noteTitle;
        this.user = user;
        this.typeid = typeid;
    }

    public NoteBean(String noteImage, String noteDetail, String noteTitle, UserBean user, String noteTime, int noteLikeCount, int sharecount, int noteCollectionCount, int noteCommentCount) {
        this.noteImage = noteImage;
        this.noteDetail = noteDetail;
        this.noteTitle = noteTitle;
        this.user = user;
        this.noteTime = noteTime;
        this.noteLikeCount = noteLikeCount;
        this.sharecount = sharecount;
        this.noteCollectionCount = noteCollectionCount;
        this.noteCommentCount = noteCommentCount;
    }
    public NoteBean(String noteImage, String noteDetail, String noteTitle,
                    UserBean user, String noteTime, CommentBean Commenet,
                    int zancount, int sharecount, int noteCollectionCount, int noteCommentCount) {
        this.noteImage = noteImage;
        this.noteDetail = noteDetail;
        this.noteTitle = noteTitle;
        this.user = user;
        this.noteTime = noteTime;
        this.comment = comment;
        this.noteLikeCount = zancount;
        this.sharecount = sharecount;
        this.noteCollectionCount = noteCollectionCount;
        this.noteCommentCount = noteCommentCount;
    }
    public NoteBean(String noPhoto, String title) {
        this.noPhoto = noPhoto;
        this.title = title;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public String getNoPhoto() {
        return noPhoto;
    }

    public void setNoPhoto(String noPhoto) {
        this.noPhoto = noPhoto;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String content) {
        this.title = content;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noId) {
        this.noteId = noId;
    }

    public String getNoteImage() {
        return noteImage;
    }

    public void setNoteImage(String noteImage) {
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



    public int getNoteLikeCount() {
        return noteLikeCount;
    }

    public void setNoteLikeCount(int zancount) {
        this.noteLikeCount = zancount;
    }

    public int getSharecount() {
        return sharecount;
    }

    public void setSharecount(int sharecount) {
        this.sharecount = sharecount;
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

    public Bitmap getNoteImage1() {
        return noteImage1;
    }

    public void setNoteImage1(Bitmap noteImage1) {
        this.noteImage1 = noteImage1;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment commentBean) {
        this.comment = commentBean;
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
