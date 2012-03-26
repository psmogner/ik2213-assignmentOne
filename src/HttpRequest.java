
public class HttpRequest {
	
	private String method_name, local_path, protocol_version;
	
	public HttpRequest(String method_name, String local_path, String protocol_version){
		this.method_name = method_name;
		this.local_path = local_path;
		this.protocol_version = protocol_version;
				
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
	
	
	
}
