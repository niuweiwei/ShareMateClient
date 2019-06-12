package cn.edu.hebtu.software.sharemateclient.Bean;

import android.graphics.Bitmap;

import java.io.Serializable;

public class UserBean implements Serializable {

    private int userId;
    private String userName;
    private String userPassword;
    private String userPhone;
    private String userPhoto;
    private String userSex;
    private String userAddress;
    private String userBirth;
    private String userIntro;
    private int followCount;
    private int fanCount;
    private int likeCount;
    private int noteCount;
    private boolean status;
    private String userPhotoPath;

    public UserBean() {
    }
    public UserBean(Object object){}
    public UserBean(int userId) {
        this.userId = userId;
    }

    public UserBean(String userName, String userPhotoPath) {
        this.userName = userName;
        this.userPhotoPath = userPhotoPath;
    }


    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public int getUserId() {return userId;}

    public void setUserId(int userId) {this.userId = userId;}

    public String getUserSex() {return userSex;}

    public void setUserSex(String userSex) {this.userSex = userSex;}

    public String getUserAddress() {return userAddress;}

    public void setUserAddress(String userAddress) {this.userAddress = userAddress;}

    public String getUserBirth() {return userBirth;}

    public void setUserBirth(String userBirth) {this.userBirth = userBirth;}

    public String getUserIntro() {return userIntro;}

    public void setUserIntro(String userIntroduce) {this.userIntro = userIntroduce;}

    public String getUserPhotoPath() {
        return userPhotoPath;
    }

    public void setUserPhotoPath(String userPhotoPath) {
        this.userPhotoPath = userPhotoPath;
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

    public int getNoteCount() {
        return noteCount;
    }

    public void setNoteCount(int noteCount) {
        this.noteCount = noteCount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean states) {
        this.status = states;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userPhoto='" + userPhoto + '\'' +
                ", userSex='" + userSex + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", userBirth='" + userBirth + '\'' +
                ", userIntro='" + userIntro + '\'' +
                ", followCount=" + followCount +
                ", fanCount=" + fanCount +
                ", likeCount=" + likeCount +
                ", noteCount=" + noteCount +
                ", status=" + status +
                ", userPhotoPath='" + userPhotoPath + '\'' +
                '}';
    }
}
