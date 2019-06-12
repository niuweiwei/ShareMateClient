package cn.edu.hebtu.software.sharemateclient.Bean;

import java.io.Serializable;

public class Follow implements Serializable {
    private int id;
    //主动方 关注人的人
    private UserBean followUser;
    //被动方 被关注的人
    private UserBean followedUser;
    private String followDate;
    private boolean isFollow;

    public Follow() { }

    public Follow(int id, UserBean followUser, UserBean followedUser, String followDate) {
        this.id = id;
        this.followUser = followUser;
        this.followedUser = followedUser;
        this.followDate = followDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserBean getFollowUser() {
        return followUser;
    }

    public void setFollowUser(UserBean followUser) {
        this.followUser = followUser;
    }

    public UserBean getFollowedUser() {
        return followedUser;
    }

    public void setFollowedUser(UserBean followedUser) {
        this.followedUser = followedUser;
    }

    public String getFollowDate() {
        return followDate;
    }

    public void setFollowDate(String followDate) {
        this.followDate = followDate;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }


    @Override
    public String toString() {
        return "Follow{" +
                "id=" + id +
                ", followUser=" + followUser +
                ", followedUser=" + followedUser +
                ", followDate='" + followDate + '\'' +
                ", isFollow=" + isFollow +
                '}';
    }
}
