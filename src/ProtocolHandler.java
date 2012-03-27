import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
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
	private Socket smtpsocket;
	private BufferedReader is;
	private BufferedOutputStream os;
	private String ipAddress = "192.168.3.12";

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
	@SuppressWarnings("deprecation")
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

			//MESSAGE TO THE SMTP SERVER
			SMTPresponse = "HELO "+http_request_info.getSMTP()+"\r\n";
			SMTPresponse += "MAIL FROM: <"+http_request_info.getFrom()+">\r\n";
			SMTPresponse += "RCPT TO: <"+http_request_info.getTo()+">\r\n";
			SMTPresponse += "DATA\r\n";
			SMTPresponse += "Subject: "+http_request_info.getText();
			SMTPresponse += "\r\n.\r\n";
			SMTPresponse += "QUIT\r\n";

			//ESTABLISH/SETUP CONNECTION TO THE SMTP SERVER HERE... I GUESS?
			/* Socket, Outputstream % InputStream*/
			smtpsocket = null;
			os = null;
			is = null;


			try {
				smtpsocket = new Socket(ipAddress, 25);
				os = new BufferedOutputStream(smtpsocket.getOutputStream());
				is = new BufferedReader(new InputStreamReader(smtpsocket.getInputStream()));

				if(smtpsocket != null && os != null && is != null){ 
					os.write(SMTPresponse.getBytes());					
				}			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Now send the email off and check the server reply.  
			// Was an OK is reached you are complete.
			String responseline;
			try {
				while((responseline = is.readLine())!=null)
				{  // System.out.println(responseline);
					if(responseline.indexOf("Ok") != -1)
						break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}






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


