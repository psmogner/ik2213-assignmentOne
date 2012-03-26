
public class HttpRequest {
	
	private String method_name, local_path, protocol_version;
	
	public HttpRequest(){
		
	}
	
	public String getMethod_name() {
		return method_name;
	}

	public String getLocal_path() {
		return local_path;
	}

	public String getProtocol_version() {
		return protocol_version;
	}

	public void setMethod_name(String method_name) {
		this.method_name = method_name;
	}

	public void setLocal_path(String local_path) {
		this.local_path = local_path;
	}

	public void setProtocol_version(String protocol_version) {
		this.protocol_version = protocol_version;
	}
	
	
	
}
