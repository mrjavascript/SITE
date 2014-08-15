package org.sitenv.portlets.directmailbox.models;

public class EmailMetaData {
	private String From;
	private String To;
	private String DateTimeStr;
	private String Subject;
	private Boolean HasAttachment;
	private String AttachmentName;
	private int Size;
	
	
	public String getFrom() {
		return From;
	}
	public void setFrom(String from) {
		From = from;
	}
	public String getTo() {
		return To;
	}
	public void setTo(String to) {
		To = to;
	}
	public String getDateReceived() {
		return DateTimeStr;
	}
	public void setDateReceived(String dateReceived) {
		DateTimeStr = dateReceived;
	}
	public String getSubject() {
		return Subject;
	}
	public void setSubject(String subject) {
		Subject = subject;
	}
	public String getAttachmentName() {
		return AttachmentName;
	}
	public void setAttachmentName(String attachmentName) {
		AttachmentName = attachmentName;
	}
	public Boolean getHasAttachment() {
		return HasAttachment;
	}
	public void setHasAttachment(Boolean hasAttachment) {
		HasAttachment = hasAttachment;
	}
	public int getSize() {
		return Size;
	}
	public void setSize(int size) {
		Size = size;
	}
}
