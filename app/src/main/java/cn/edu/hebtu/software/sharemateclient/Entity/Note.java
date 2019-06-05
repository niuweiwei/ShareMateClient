package cn.edu.hebtu.software.sharemateclient.Entity;


import java.io.Serializable;

public class Note implements Serializable{

	private int noteId;
	private String noteVideo;
	private String noteTitle;
	private String noteDetail;
	private String noteImage;
	private String noteDate;
	private String noteAddress;
	private int typeId;
	private User user;
	private int likeCount,collectCount,commentCount;
	private boolean like;
	private boolean collect;
	private boolean follow;

	public Note() {
		super();
	}
	public int getNoteId() {
		return noteId;
	}
	public void setNoteId(int noteId) {
		noteId = noteId;
	}
	public String getNoteVideo() {
		return noteVideo;
	}
	public void setNoteVideo(String noteVideo) {
		this.noteVideo = noteVideo;
	}
	public String getNoteTitle() {
		return noteTitle;
	}
	public void setNoteTitle(String noteTitle) {
		this.noteTitle = noteTitle;
	}
	public String getNoteDetail() {
		return noteDetail;
	}
	public void setNoteDetail(String noteDetail) {
		this.noteDetail = noteDetail;
	}
	public String getNoteImage() {
		return noteImage;
	}
	public void setNoteImage(String noteImage) {
		this.noteImage = noteImage;
	}
	public String getNoteDate() {
		return noteDate;
	}
	public void setNoteDate(String noteDate) {
		this.noteDate = noteDate;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	@Override
	public String toString() {
		return "Note [NoteId=" + noteId + ", noteVideo=" + noteVideo + ", noteTitle=" + noteTitle + ", noteDetail="
				+ noteDetail + ", noteImage=" + noteImage + ", noteDate=" + noteDate + ", typeId=" + typeId + ", user="
				+ user + "]";
	}

	public boolean isLike() {
		return like;
	}

	public void setLike(boolean like) {
		this.like = like;
	}

	public boolean isCollect() {
		return collect;
	}

	public void setCollect(boolean collect) {
		this.collect = collect;
	}

	public boolean isFollow() {
		return follow;
	}

	public void setFollow(boolean follow) {
		this.follow = follow;
	}

	public int getCollectCount() {
		return collectCount;
	}

	public void setCollectCount(int collectCount) {
		this.collectCount = collectCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public String getNoteAddress() {
		return noteAddress;
	}

	public void setNoteAddress(String noteAddress) {
		this.noteAddress = noteAddress;
	}
}
