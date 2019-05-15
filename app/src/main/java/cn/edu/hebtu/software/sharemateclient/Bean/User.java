package cn.edu.hebtu.software.sharemateclient.Bean;

import java.io.Serializable;

public class User implements Serializable {

    private int userId;
    private String userName;
    private String userPassword;
    private String userPhoto;
    private String userSex;
    private String userPhone;
    private String userAddress;
    private String userBirth;
    private String userIntro;

    public User() { }

    public User(int userId, String userName, String userPassword, String userPhoto, String userSex, String userPhone, String userAddress, String userBirth, String userIntro) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userPhoto = userPhoto;
        this.userSex = userSex;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.userBirth = userBirth;
        this.userIntro = userIntro;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserBirth() {
        return userBirth;
    }

    public void setUserBirth(String userBirth) {
        this.userBirth = userBirth;
    }

    public String getUserIntro() {
        return userIntro;
    }

    public void setUserIntro(String userIntro) {
        this.userIntro = userIntro;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userPhoto='" + userPhoto + '\'' +
                ", userSex='" + userSex + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", userBirth='" + userBirth + '\'' +
                ", userIntro='" + userIntro + '\'' +
                '}';
    }
}
