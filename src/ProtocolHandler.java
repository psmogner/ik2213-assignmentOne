import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class ProtocolHandler {
	private String [] get_array;
	private String htmlfile = "";
	private HttpRequest http_request_info;
	private BufferedReader input;
	private String to = null;
	private String from = null;
	private String subject = null;
	private String smtp = null;
	private String text = null;

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

				try {
					from = URLDecoder(message[0].split("=")[1]);
					to = URLDecoder(message[1].split("=")[1]);
					subject = (message[2].split("=")[1]);
					smtp = URLDecoder(message[3].split("=")[1]);
					text = URLDecoder(message[4].split("=")[1]);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				validateEmail(from);
				validateEmail(to);

				http_request_info.setFrom(from);
				http_request_info.setTo(to);
				http_request_info.setSubject(subject);
				http_request_info.setSMTP(smtp);
				http_request_info.setText(text);
				
			}
		}
		return "";
	}

	/* Handle output */
	public String outputHandler(){
		String response = "";
		String SMTPresponse = "";
		htmlfile = "";
		String line;

		if(http_request_info.getMethod_name().equals("GET")){
			System.out.println("OUTPUTHANDLER EQUALS GET");
			try {
				input = new BufferedReader(new FileReader("index.html"));
			} catch (FileNotFoundException e) {e.printStackTrace();}
			try {
				while((line = input.readLine()) != null){
					htmlfile += line + "\r\n";
				}
			} catch (IOException e) {e.printStackTrace();}

		}else if(http_request_info.getMethod_name().equals("POST")){
			System.out.println("OUTPUTHANDLER EQUALS POST");
			//ESTABLISH/SETUP CONNECTION TO THE SMTP SERVER HERE... I GUESS?
			
			
			
			//MESSAGE TO THE SMTP SERVER
			SMTPresponse = "HELO "+http_request_info.getSMTP()+"\r\n";
			SMTPresponse += "MAIL FROM: <"+http_request_info.getFrom()+">\r\n";
			SMTPresponse += "RCPT TO: <"+http_request_info.getTo()+">\r\n";
			SMTPresponse += "DATA\r\n";
			SMTPresponse += "Subject: "+http_request_info.getText();
			SMTPresponse += "\r\n.\r\n";
			SMTPresponse += "QUIT\r\n";
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

	public String URLDecoder(String input) throws UnsupportedEncodingException{
		System.out.println("<BEFORE: " + input);
		String result = URLDecoder.decode(input, "UTF-8");
		System.out.println("<AFTER: " +result);
		return result;
	}
	public Boolean validateEmail(String emailForValidation){
		boolean valid = false;
	
		if(emailForValidation.contains("@") == true){
			String[] result = emailForValidation.split("@");
			
			if(result[0] == null || result[1] == null){
				System.out.println("<INVALID EMAIL "+ emailForValidation);
				return false;
			}else if(result[1].contains(".") == false){
				System.out.println("<INVALID EMAIL "+emailForValidation);
				return false;
			}else{
				System.out.println("<VALID EMAIL "+emailForValidation);
				valid = true;
			}
		}
		return valid;
	}

}


