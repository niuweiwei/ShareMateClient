package cn.edu.hebtu.software.sharemateclient.Bean;

public class Follow {
    private int id;
    //主动方 关注人的人
    private User followUser;
    //被动方 被关注的人
    private User followedUser;
    private String followDate;

    public Follow() { }

    public Follow(int id, User followUser, User followedUser, String followDate) {
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

    public User getFollowUser() {
        return followUser;
    }

    public void setFollowUser(User followUser) {
        this.followUser = followUser;
    }

    public User getFollowedUser() {
        return followedUser;
    }

    public void setFollowedUser(User followedUser) {
        this.followedUser = followedUser;
    }

    public String getFollowDate() {
        return followDate;
    }

    public void setFollowDate(String followDate) {
        this.followDate = followDate;
    }

    @Override
    public String toString() {
        return "Follow{" +
                "id=" + id +
                ", followUser=" + followUser +
                ", followedUser=" + followedUser +
                ", followDate='" + followDate + '\'' +
                '}';
    }
}
