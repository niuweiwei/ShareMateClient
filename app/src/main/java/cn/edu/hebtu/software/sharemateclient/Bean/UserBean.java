package cn.edu.hebtu.software.sharemateclient.Bean;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

public class UserBean implements Serializable {

    private int userId;
    private String userName;
    private String userPassword;
    private String userPhone;
    private String userPhoto;
    private String userSex;
    private String userAddress;
    private String userBirth;
    private String userIntroduce;
    private int followCount;
    private int fanCount;
    private int likeCount;

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

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public UserBean() {
    }
    public UserBean(Object object){}
    public UserBean(int userId) {
        this.userId = userId;
    }


    public UserBean(String userName, String userPhoto) {
        this.userName = userName;
        this.userPhoto = userPhoto;
    }
    public UserBean(int userId, String userPhoto, String userName, String userSex,
                    String userAddress, String userBirth, String userIntroduce) {
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.userName = userName;
        this.userSex = userSex;
        this.userAddress = userAddress;
        this.userBirth = userBirth;
        this.userIntroduce = userIntroduce;
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

    public String getUserIntroduce() {return userIntroduce;}

    public void setUserIntroduce(String userIntroduce) {this.userIntroduce = userIntroduce;}

}
