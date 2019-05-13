package cn.edu.hebtu.software.sharemateclient.Entity;


public class Note {

	private int NoteId;
	private String noteVideo;
	private String noteTitle;
	private String noteDetail;
	private String noteImage;
	private String noteDate;
	private int typeId;
	private User user;

	public Note() {
		super();
	}
	public int getNoteId() {
		return NoteId;
	}
	public void setNoteId(int noteId) {
		NoteId = noteId;
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
	@Override
	public String toString() {
		return "Note [NoteId=" + NoteId + ", noteVideo=" + noteVideo + ", noteTitle=" + noteTitle + ", noteDetail="
				+ noteDetail + ", noteImage=" + noteImage + ", noteDate=" + noteDate + ", typeId=" + typeId + ", user="
				+ user + "]";
	}




}
