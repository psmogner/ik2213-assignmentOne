import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ProtocolHandler {
	private String [] get_array;
	private String htmlfile = "";
	private HttpRequest http_request_info;
	private BufferedReader input;

	public ProtocolHandler(){
		http_request_info = new HttpRequest();
	}

	public String inputHandler(String inputString){
		System.out.println(inputString);
		if(inputString != null){
			if(inputString.startsWith("GET")){
				System.out.println("IS A GET");
				get_array = inputString.split(" ");
				http_request_info.setMethod_name(get_array[0]);
				http_request_info.setLocal_path(get_array[1]);
				http_request_info.setProtocol_version(get_array[2]);
			
			}else if(inputString.startsWith("POST")){
				System.out.println("IS A POST");
				get_array = inputString.split(" ");
				http_request_info.setMethod_name(get_array[0]);
				http_request_info.setLocal_path(get_array[1]);
				http_request_info.setProtocol_version(get_array[2]);
			
			}else if(inputString.startsWith("from=")){
				String[] message = inputString.split("&");

				//System.out.println(message[0].split("=")[1]);
				//System.out.println(message[1].split("=")[1]);
				//System.out.println(message[2].split("=")[1]);
				//System.out.println(message[3].split("=")[1]);
				//System.out.println(message[4].split("=")[1]);

				http_request_info.setFrom(message[0].split("=")[1]);
				http_request_info.setTo(message[1].split("=")[1]);
				http_request_info.setSubject(message[2].split("=")[1]);
				http_request_info.setSMTP(message[3].split("=")[1]);
				http_request_info.setMessage(message[4].split("=")[1]);
			}
		}
		return "";
	}

	/* Handle output */
	public String outputHandler(){
		String response = "";
		htmlfile = "";
		String line;

		if(http_request_info.getMethod_name().equals("GET")){
			System.out.println("EQUALS GET");
			try {
				input = new BufferedReader(new FileReader("index.html"));
			} catch (FileNotFoundException e) {e.printStackTrace();}
			try {
				while((line = input.readLine()) != null){
					htmlfile += line + "\r\n";
				}
			} catch (IOException e) {e.printStackTrace();}

		}else if(http_request_info.getMethod_name().equals("POST")){
			System.out.println("EQUALS POST");
			//ESTABLISH/SETUP CONNECTION TO THE SMTP SERVER HERE... I GUESS?
		}
		
		response = "HTTP/1.0 200 OK\r\n";
		response += "Content-Type: text/html\r\n";
		response += "Content-Length " + htmlfile.length() + "\r\n";
		response += "Server: IK2213A1 Server \r\n";
		response += "Connection: Close \r\n";
		response += "\r\n";
		response += htmlfile;
		//System.out.println(response);
		return response;
	}	
}


