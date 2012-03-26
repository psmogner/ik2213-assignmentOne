import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class ProtocolHandler {
	
	private String [] get_array;
	private String htmlfile = "";
	private HTTP_REQ http_request_info;
	private BufferedReader input;
	
	

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
			http_request_info = new HTTP_REQ(get_array[0], get_array[1], get_array[2]);
			
		}
		/*Post Req*/ 
		else if(inputString.startsWith("POST")){
			
		}
		return "";
	}
	
	
	/* Handle output */
	public String outputHandler(){
		
		String response = "";
		
		if(http_request_info.getMethod_name().equalsIgnoreCase("GET")){
			/* Send HTML File*/
			try {
				input = new BufferedReader(new FileReader("index.html"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			/* Read from the buffer */
			String line;
			try {
				while((line = input.readLine()) != null){
					htmlfile += line + "\r\n";
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response = "HTTP/1.0 200 OK\r\n";
			response += "Content-Type: text/html\r\n";
			response += "Content-Length " + htmlfile.length() + "\r\n";
			response += "Server: IK2213A1 Server \r\n";
			response += "Connection: Close \r\n";
			response += "\r\n";
			response += htmlfile;
			
			return response;
		}
	
		
//		else if(){
//			
//		}
		
		
		
		return "";
	}
	
	

}	
	

