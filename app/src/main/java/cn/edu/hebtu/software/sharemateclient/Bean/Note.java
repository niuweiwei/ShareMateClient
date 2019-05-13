package cn.edu.hebtu.software.sharemateclient.Bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {

    private int noteId;
    private String noteTitle;
    private String noteDetail;
    private String noteImage;
    private String noteDate;
    //笔记类型
    private int typeId;
    //笔记的发布者
    private User user;

    public Note() { }

    public Note(int noteId, String noteTitle, String noteDetail, String noteImage, String noteDate, int typeId, User user) {
        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.noteDetail = noteDetail;
        this.noteImage = noteImage;
        this.noteDate = noteDate;
        this.typeId = typeId;
        this.user = user;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
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
        return "Note{" +
                "noteId=" + noteId +
                ", noteTitle='" + noteTitle + '\'' +
                ", noteDetail='" + noteDetail + '\'' +
                ", noteImage='" + noteImage + '\'' +
                ", noteDate=" + noteDate +
                ", noteId='" + typeId + '\'' +
                ", user=" + user +
                '}';
    }
}
