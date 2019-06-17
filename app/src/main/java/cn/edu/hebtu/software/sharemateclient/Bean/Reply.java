package cn.edu.hebtu.software.sharemateclient.Bean;

import java.util.List;

public class Reply {
	//薇薇
	private int replyId;
	private CommentBean comment;
	private Reply reply;//当前回复 回复的 回复
	private List<Reply> replyList;//回复了当前回复的回复列表
	private UserBean user;
	private String replyDetail;
	private String replyDate;

	//春柳
	private int commentId;
	private int reReplyId;
	private int userId;
	private String replyTime;
	private UserBean replyedUser ;
	private boolean like;

	public int getReplyId() {
		return replyId;
	}

	public void setReplyId(int replyId) {
		this.replyId = replyId;
	}

	public CommentBean getComment() {
		return comment;
	}

	public void setComment(CommentBean comment) {
		this.comment = comment;
	}

	public Reply getReply() {
		return reply;
	}

	public void setReply(Reply reply) {
		this.reply = reply;
	}

	public List<Reply> getReplyList() {
		return replyList;
	}

	public void setReplyList(List<Reply> replyList) {
		this.replyList = replyList;
	}

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

	public String getReplyDetail() {
		return replyDetail;
	}

	public void setReplyDetail(String replyDetail) {
		this.replyDetail = replyDetail;
	}

	public String getReplyDate() {
		return replyDate;
	}

	public void setReplyDate(String replyDate) {
		this.replyDate = replyDate;
	}

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public int getReReplyId() {
		return reReplyId;
	}

	public void setReReplyId(int reReplyId) {
		this.reReplyId = reReplyId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}

	public UserBean getReplyedUser() {
		return replyedUser;
	}

	public void setReplyedUser(UserBean replyedUser) {
		this.replyedUser = replyedUser;
	}

	public boolean isLike() {
		return like;
	}

	public void setLike(boolean like) {
		this.like = like;
	}
}
