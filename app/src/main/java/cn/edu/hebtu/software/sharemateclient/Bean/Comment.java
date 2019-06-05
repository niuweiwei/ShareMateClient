package cn.edu.hebtu.software.sharemateclient.Bean;

import java.io.Serializable;

public class Comment implements Serializable{
    private int commentId;
    private int nodeId;
    private int headImage;
    private String name;
    private String content;
    private String commentTime;
    private int countZan;
    private UserBean user;
    public Comment() {
    }

    public Comment(int commentId, int nodeId, int headImage, String name, String content,
                   String commentTime, int countZan) {
        this.commentId = commentId;
        this.nodeId = nodeId;
        this.headImage = headImage;
        this.name = name;
        this.content = content;
        this.commentTime = commentTime;
        this.countZan = countZan;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getHeadImage() {
        return headImage;
    }

    public void setHeadImage(int headImage) {
        this.headImage = headImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public int getCountZan() {
        return countZan;
    }

    public void setCountZan(int countZan) {
        this.countZan = countZan;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
}
