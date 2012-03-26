public class HttpRequest {
	
	private String method_name; 
	private String local_path; 
	private String protocol_version; 
	private String mailFrom; 
	private String mailTo; 
	private String mailSubject; 
	private String mailSMTP; 
	private String mailMessage;
	
	public HttpRequest(){
		
	}
	
	public String getMethod_name() {return method_name;}
	public String getLocal_path() {return local_path;}
	public String getProtocol_version() {return protocol_version;}
	public String getFrom(){return mailFrom;}
	public String getTo(){return mailTo;}
	public String getSubject(){return mailSubject;}
	public String getSMTP(){return mailSMTP;}
	public String getMessage(){return mailMessage;}

	public String setFrom(String from){return mailFrom;}
	public String setTo(String to){return mailTo;}
	public String setSubject(String subject){return mailSubject;}
	public String setSMTP(String SMTP){return mailSMTP;}
	public String setMessage(String message){return mailMessage;}
	public void setMethod_name(String method_name) {this.method_name = method_name;}
	public void setLocal_path(String local_path) {this.local_path = local_path;}
	public void setProtocol_version(String protocol_version) {this.protocol_version = protocol_version;}
}
