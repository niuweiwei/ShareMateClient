package cn.edu.hebtu.software.sharemateclient.Bean;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.edu.hebtu.software.sharemateclient.R;

public class NoteBean implements Serializable{
    private int noteId;
    private String noteTitle;
    private String noteDetail;
    private String noteImage;
    private String noteDate;
    private UserBean user;
    private Type type;

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

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
