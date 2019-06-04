package cn.edu.hebtu.software.sharemateclient.Bean;

public class Chat {

    private User user;
    private String content;

    public Chat() {
    }

    public Chat(User user, String content) {
        this.user = user;
        this.content = content;
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

    @Override
    public String toString() {
        return "Chat{" +
                "user=" + user +
                ", content='" + content + '\'' +
                '}';
    }
}
