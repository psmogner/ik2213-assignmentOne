import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class ProtocolHandler {
	private String [] get_array;
	private String htmlfile = "";
	private HttpRequest http_request_info;
	private BufferedReader input;
	private String thisCase;
	

	public ProtocolHandler(){
		http_request_info = new HttpRequest();
	}
	/* Handle input */
	public boolean StringCompare(String stringOne, String stringTwo){		
		char[] stringOneCharArray = stringOne.toCharArray();
		char [] stringTwoCharArray = stringTwo.toCharArray();

		if(stringOneCharArray.length == stringTwoCharArray.length){
			for(int i = 0; i<stringOneCharArray.length; i++){
				if(stringOneCharArray[i] != stringTwoCharArray[i]){
					return false;
				}
			}
		}
		return true;
	}


	public String inputHandler(String inputString){
		//System.out.println(inputString);
		get_array = null;
	
		if(inputString.startsWith("GET")){
			System.out.println("IS A GET");
			get_array = inputString.split(" ");
			http_request_info.setMethod_name(get_array[0]);
			http_request_info.setLocal_path(get_array[1]);
			http_request_info.setProtocol_version(get_array[2]);
		}
		else if(inputString.startsWith("POST")){
			System.out.println("IS A POST");
			get_array = inputString.split(" ");
			http_request_info.setMethod_name(get_array[0]);
			http_request_info.setLocal_path(get_array[1]);
			http_request_info.setProtocol_version(get_array[2]);
			
		}else if(inputString.startsWith("from=")){
			String[] message = inputString.split("&");
		
			System.out.println(message[0].split("=")[1]);
			System.out.println(message[1].split("=")[1]);
			System.out.println(message[2].split("=")[1]);
			System.out.println(message[3].split("=")[1]);
			System.out.println(message[4].split("=")[1]);
			
			http_request_info.setFrom(message[0].split("=")[1]);
			http_request_info.setTo(message[1].split("=")[1]);
			http_request_info.setSubject(message[2].split("=")[1]);
			http_request_info.setSMTP(message[3].split("=")[1]);
			http_request_info.setMessage(message[4].split("=")[1]);
		}
		return "";
	}

	/* Handle output */
	public String outputHandler(){
		String response = "";
		htmlfile = "";
		//System.out.println("Test" + http_request_info.getMethod_name());
		//		if(http_request_info.getMethod_name() == null){
		//			System.out.println("Den ar null");
		//		}
		//		http_request_info.setMethod_name("GET");
		if(StringCompare(http_request_info.getMethod_name(), "GET")){
			/* Send HTML File*/
			System.out.println("EQUALS GET");

			try {
				input = new BufferedReader(new FileReader("index.html"));
				System.out.println("<------------------------------------->");
			} catch (FileNotFoundException e) {
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
		}else if(StringCompare(http_request_info.getMethod_name(), "POST")){
			System.out.println("EQUALS POST");
		}
		response = "HTTP/1.0 200 OK\r\n";
		response += "Content-Type: text/html\r\n";
		response += "Content-Length " + htmlfile.length() + "\r\n";
		response += "Server: IK2213A1 Server \r\n";
		response += "Connection: Close \r\n";
		response += "\r\n";
		response += htmlfile;

//		System.out.println(response);
		return response;
	}	
}


