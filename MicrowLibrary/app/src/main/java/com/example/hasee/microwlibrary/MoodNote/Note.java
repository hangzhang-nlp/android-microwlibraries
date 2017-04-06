package com.example.hasee.microwlibrary.MoodNote;

public class Note {
	private int id;
	private String noteContent;
	private String noteDate;
	private String noteAddress;
	
	public String getNoteAddress() {
		return noteAddress;
	}
	public void setNoteAddress(String noteAddress) {
		this.noteAddress = noteAddress;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNoteContent() {
		return noteContent;
	}
	public void setNoteContent(String noteContent) {
		this.noteContent = noteContent;
	}
	
	public String getNoteDate() {
		return noteDate;
	}
	public void setNoteDate(String noteDate) {
		this.noteDate = noteDate;
	}

	
	
}
