package cn.edu.hebtu.software.sharemateclient.Bean;

import java.util.Date;

public class Chat {

    private User user;
    private String content;
    private Date date;

    public Chat() {
    }

    public Chat(User user, String content) {
        this.user = user;
        this.content = content;
    }

    public Chat(User user, String content, Date date) {
        this.user = user;
        this.content = content;
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "user=" + user +
                ", content='" + content + '\'' +
                ", date=" + date +
                '}';
    }
}
