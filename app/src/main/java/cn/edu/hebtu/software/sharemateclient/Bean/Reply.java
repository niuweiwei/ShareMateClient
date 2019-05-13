package cn.edu.hebtu.software.sharemateclient.Bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Reply implements Parcelable {
    private int replyId;
    private int commentId;
    private String reReplyName;
    private int image;
    private String userName;
    private String userPhoto;
    private String content;
    private String time;
    private int countZan;
    private int userId;
    private UserBean user;

    public Reply() {
    }

    public Reply(int replyId, int commentId, int image, String name, String content, String time, int countZan) {
        this.replyId=replyId;
        this.commentId = commentId;
        this.image = image;
        this.userName = name;
        this.content = content;
        this.time = time;
        this.countZan = countZan;
    }

    protected Reply(Parcel in) {
        replyId = in.readInt();
        commentId = in.readInt();
        reReplyName = in.readString();
        image = in.readInt();
        userName = in.readString();
        userPhoto = in.readString();
        content = in.readString();
        time = in.readString();
        countZan = in.readInt();
    }

    public static final Creator<Reply> CREATOR = new Creator<Reply>() {
        @Override
        public Reply createFromParcel(Parcel in) {
            return new Reply(in);
        }

        @Override
        public Reply[] newArray(int size) {
            return new Reply[size];
        }
    };

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getReplyId() { return replyId; }

    public void setReplyId(int replyId) { this.replyId = replyId; }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getReReplyName() {
        return reReplyName;
    }

    public void setReReplyName(String reReplyName) {
        this.reReplyName = reReplyName;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCountZan() {
        return countZan;
    }

    public void setCountZan(int countZan) {
        this.countZan = countZan;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(replyId);
        dest.writeInt(commentId);
        dest.writeString(reReplyName);
        dest.writeInt(image);
        dest.writeString(userName);
        dest.writeString(userPhoto);
        dest.writeString(content);
        dest.writeString(time);
        dest.writeInt(countZan);
    }
}
