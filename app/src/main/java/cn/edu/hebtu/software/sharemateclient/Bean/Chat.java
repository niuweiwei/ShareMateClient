package cn.edu.hebtu.software.sharemateclient.Bean;

public class Chat {

    private UserBean user;
    private String content;

    public Chat() {
    }

    public Chat(UserBean user, String content) {
        this.user = user;
        this.content = content;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
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
