package eu.trentorise.smartcampus.openservices.beans;

public class EmailMessage {
	private String from;
	private String subject;
	private String message;

	public EmailMessage(String from, String subject, String message) {
		this.from = from;
		this.subject = subject;
		this.message = message;
	}

	public EmailMessage(String subject, String message) {
		this.subject = subject;
		this.message = message;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

}
