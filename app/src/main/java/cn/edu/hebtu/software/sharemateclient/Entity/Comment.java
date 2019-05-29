package cn.edu.hebtu.software.sharemateclient.Entity;

import java.util.ArrayList;
import java.util.List;

public class Comment {

	private int commentId;
	private String commentDetail;
	private String commentDate;
	private User user;
	private int noteId;
	private List<Reply> replyList=new ArrayList<>();

	public Comment() {
		super();
	}
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public String getCommentDetail() {
		return commentDetail;
	}
	public void setCommentDetail(String commentDeatil) {
		this.commentDetail = commentDeatil;
	}
	public String getCommentDate() {
		return commentDate;
	}
	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getNoteId() {
		return noteId;
	}
	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}
	public List<Reply> getReplyList() {
		return replyList;
	}
	public void setReplyList(List<Reply> replyList) {
		this.replyList = replyList;
	}

	
	
}
