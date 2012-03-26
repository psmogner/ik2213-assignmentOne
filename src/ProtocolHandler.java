
public class ProtocolHandler {
	
	String [] get_array;
	

	public ProtocolHandler(){
		
	}

	
	/* Handle input */
	
	
	/**
The initial line is different for the request than for the response. A request line has three parts, separated by spaces: a method name, the local path of the requested resource, and the version of HTTP being used. A typical request line is:

GET /path/to/file/index.html HTTP/1.0
	 * 
	 */
	
	
	/*
	 POST /path/script.cgi HTTP/1.0
From: frog@jmarshall.com
User-Agent: HTTPTool/1.0
Content-Type: application/x-www-form-urlencoded
Content-Length: 32

home=Cosby&favorite+flavor=flies
	 
	 */
	
	
	public String inputHandler(String inputString){
		
		/* Get Req */
		if(inputString.startsWith("GET")){
			get_array = inputString.split(" ");
			
		}
		/*Post Req*/ 
		else if(inputString.startsWith("POST")){
			
		}
		
		
		
		
		
		return "";
	}
	
	
	/* Handle output */
	public String outputHandler(){
		
		
		
		
		return "";
	}
	
	

}	
	

