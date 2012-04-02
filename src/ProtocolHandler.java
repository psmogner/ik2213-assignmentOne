import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.xml.soap.MimeHeaders;

public class ProtocolHandler {
	private String [] get_array;
	private String htmlfile = "";
	private static HttpRequest http_request_info;
	private BufferedReader input;
	private String to = null;
	private String from = null;
	private String subject = null;
	private String smtp = null;
	private String text = null;
	private Socket smtpsocket;
	private BufferedReader is;
	private BufferedOutputStream os;
	private String ipAddress = "192.168.3.11";
	private boolean temp = false;

	public ProtocolHandler(){
		http_request_info = new HttpRequest();
	}

	public String inputHandler(String inputString){

		if(inputString != null){
			if(inputString.startsWith("GET")){
				System.out.println("IS A GET");
				get_array = inputString.split(" ");
				http_request_info.setMethod_name(get_array[0]);
				http_request_info.setLocal_path(get_array[1]);
				http_request_info.setProtocol_version(get_array[2]);
				temp = true;

			}else if(inputString.startsWith("POST")){
				System.out.println("IS A POST");
				get_array = inputString.split(" ");
				http_request_info.setMethod_name(get_array[0]);
				http_request_info.setLocal_path(get_array[1]);
				http_request_info.setProtocol_version(get_array[2]);

			}else if(inputString.startsWith("from=")){
				String[] message = inputString.split("&");

				try {
					from = theDecoderWithSwe(message[0].split("=")[1]);
					to = theDecoderWithSwe(message[1].split("=")[1]);
					subject = theDecoderWithSwe(message[2].split("=")[1]);
					smtp = theDecoderWithSwe(message[3].split("=")[1]);
					text = theDecoderWithSwe(message[4].split("=")[1]);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				//CHECKS IF THE EMAILS ARE VALID
				if(validateEmail(from) == false || validateEmail(to) == false){
					reLoadHTML("Invalid mail address!");
					return "";
				}
//				String potentialDNS = to.split("@")[1];
//				System.out.println("Vad som skickas in: "+ to.split("@")[1]);
//				MXlookup(to.split("@")[1]);

				http_request_info.setMailFrom(from);
				http_request_info.setMailTo(to);
				http_request_info.setMailSubject(subject);
				http_request_info.setMailSMTP(smtp);
				http_request_info.setMailText(text);
			}
		}else{
			//SOMETHING?!?	
		}
		return "";
	}

	/* Handle output */
	public String outputHandler(){
		String response = "";
		String SMTPresponse = "";
		htmlfile = "";

		if(http_request_info.getMethod_name().equals("GET")){
			//System.out.println("OUTPUTHANDLER EQUALS GET");

			reLoadHTML("This gonna be blank later!");

		}else if(http_request_info.getMethod_name().equals("POST")){
			System.out.println("OUTPUTHANDLER EQUALS POST");
			//ESTABLISH/SETUP CONNECTION TO THE SMTP SERVER HERE... I GUESS?

			//MESSAGE TO THE SMTP SERVER

			String hello = "HELO "+http_request_info.getMailSMTP()+"\r\n";
			String mail_from = "MAIL FROM: <"+http_request_info.getMailFrom()+">\r\n";			
			String rcpt_to = "RCPT TO: <" + http_request_info.getMailTo()+">\r\n";
			String data = "DATA\r\n";
			String subject = "Subject: "+ http_request_info.getMailSubject()+"\r\n";
			String mail_text = http_request_info.getMailText() + "\r\n";
			String blank_line = "\r\n";
			String new_lines = ".\r\n";
			String quit = "QUIT\r\n";
			System.out.println(SMTPresponse);

			//ESTABLISH/SETUP CONNECTION TO THE SMTP SERVER HERE... I GUESS?
			/* Socket, Outputstream % InputStream*/
			smtpsocket = null;
			os = null;
			is = null;

			try {
				smtpsocket = new Socket(ipAddress, 25);
				System.out.println("Connected to SMTP");
				os = new BufferedOutputStream(smtpsocket.getOutputStream());
				is = new BufferedReader(new InputStreamReader(smtpsocket.getInputStream()));

				if(smtpsocket != null && os != null && is != null){
					System.out.println("I ifsatsen");

					sendAndGetResponse(hello);
					sendAndGetResponse(mail_from);
					sendAndGetResponse(rcpt_to);
					sendAndGetResponse(data);
					sendAndGetResponse(subject + blank_line + mail_text + new_lines);
					sendAndGetResponse(quit);	
				
				}			
			} catch (IOException e) {
				System.out.println("Could not connect to SMTP server");
				reLoadHTML("Could not connect to the SMTP server");
				e.printStackTrace();
			}
			// Now send the email off and check the server reply.  
			// Was an OK is reached you are complete.
			String responseline;
			try {

				while((responseline = is.readLine())!=null) {  
					System.out.println(responseline);
					//	if(responseline.indexOf("Ok") != -1)
					//	break;
				}
				reLoadHTML("Mail successfully sent! :D");
			} catch (IOException e) {e.printStackTrace();}

			try {
				smtpsocket.close();
			} catch (IOException e) {
				System.out.println("Failed to close socket");
				reLoadHTML("Failed to close socket.");
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
		return response;
	}	
	
	//================================================================================
	public String theDecoderWithSwe(String input) throws UnsupportedEncodingException{
		System.out.println("<BEFORE: " + input);

		if(input.contains("%E4")==true) {
			input = input.replace("%E4", "=E4");
		}
		if(input.contains("%E5") == true){
			input = input.replace("%E5", "=E5");			
		}
		if(input.contains("%F6") == true){
			input = input.replace("%F6", "=F6");
		}
		if(input.contains("%C4")==true) {
			input = input.replace("%C4", "=C4");
		}
		if(input.contains("%C5") == true){
			input = input.replace("%C5", "=C5");			
		}
		if(input.contains("%D6") == true){
			input = input.replace("%D6", "=D6");
		}

		String result = URLDecoder.decode(input, "ASCII");

		System.out.println("<AFTER: " +result);
		return result;
	}

	//================================================================================
	private String reLoadHTML(String counterMeasure){
		String line;
		htmlfile = "";
		
		try {
			input = new BufferedReader(new FileReader("index.html"));
		} catch (FileNotFoundException e) {e.printStackTrace();}
		try {
			while((line = input.readLine()) != null){
				if(line.contains("</form>")){
					line = "</form>\r\n<br><br><center><h2>"+counterMeasure+"</h2></center>";
				}
				htmlfile += line + "\r\n";
			}
		} catch (IOException e) {e.printStackTrace();}
		return "";
	}
	
	//================================================================================
	private String MXlookup(String potentialDNS){
		String DNSserver = "";
		String attrbuteToString = "";
		Attributes attributes = null;
		String[] MXattributes = {"MX"};
		InitialDirContext dircontext = null;
		Properties property = new Properties();
		property.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");

		try {
			dircontext = new InitialDirContext(property);
		} catch (NamingException e) {
			e.printStackTrace();
		}

		try {
			attributes = dircontext.getAttributes(potentialDNS, MXattributes);
		} catch (NamingException e) {
			e.printStackTrace();
		}

		Attribute attribute = attributes.get("MX");

		if(attribute != null) {
			try{
				attrbuteToString = (String) attribute.get(0);
			} catch (NamingException ex) {
				ex.printStackTrace();
			}
			String[] parts = attrbuteToString.split(" ");
			DNSserver = parts[parts.length-1];
			System.out.println("DNSserver lookup: " + DNSserver);
		}
		return DNSserver;
	}
	//================================================================================
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
	
	//================================================================================
	public void sendAndGetResponse(String statement){
		try {
			System.out.println("sendAndGetResponse: " + statement);
			os.write(statement.getBytes());
			os.flush();
		} catch (IOException e) {e.printStackTrace();}
		try {
			System.out.println("readline: " + is.readLine());
		} catch (IOException e) {e.printStackTrace();}

		// L€GG IN ERROR HANTERING OM EJ OK
	}
}
