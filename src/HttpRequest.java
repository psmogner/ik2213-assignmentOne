public class HttpRequest {
	
	private String method_name; 
	private String local_path; 
	private String protocol_version; 
	private String mailFrom; 
	private String mailTo; 
	private String mailSubject; 
	private String mailSMTP; 
	private String mailText;
	
	public HttpRequest(){
		
	}
	
	

	public String getMethod_name() {
		return method_name;
	}

	public void setMethod_name(String method_name) {
		this.method_name = method_name;
	}

	public String getLocal_path() {
		return local_path;
	}

	public void setLocal_path(String local_path) {
		this.local_path = local_path;
	}

	public String getProtocol_version() {
		return protocol_version;
	}

	public void setProtocol_version(String protocol_version) {
		this.protocol_version = protocol_version;
	}

	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public String getMailTo() {
		return mailTo;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getMailSMTP() {
		return mailSMTP;
	}

	public void setMailSMTP(String mailSMTP) {
		this.mailSMTP = mailSMTP;
	}

	public String getMailText() {
		return mailText;
	}

	public void setMailText(String mailText) {
		this.mailText = mailText;
	}
	

}
