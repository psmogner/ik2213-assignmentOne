import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProtocolHandler {

	// Initialize necessary variables needed.
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
	private Pattern pattern;
	private Matcher matcher;

	// The static final String EMAIL_PATTERN gives the possibility to type in an email 
	// that can look like x@x.x to x.x@x.x.x 
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private boolean temp = false;

	public ProtocolHandler(){
		http_request_info = new HttpRequest();
	}

	// The inputHandler method takes care of the incoming requests from the client browsers and 
	// stores necessary data from the request.
	public String inputHandler(String inputString){

		// First we check if the string is null since there can be empty data sent. 
		// At least with Google Chrome since it preloads data while typing in the address.
		if(inputString != null){

			// Looking if the message is a GET or POST request and if it is a 
			// POST request we have to catch the variables/info that is sent 
			// along with the request (which starts with from=......).
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
			}

			// If the message contains data that the client have written we need to 
			// split up the message since it appears in a single string and store the
			// variables accordingly.
			else if(inputString.startsWith("from=")){
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

				// After the variables have been stored we have to set them
				// which is done in our HTTP request object that holds the necessary
				// variables.
				http_request_info.setMailFrom(from);
				http_request_info.setMailTo(to);
				http_request_info.setMailSubject(subject);
				http_request_info.setMailSMTP(smtp);
				http_request_info.setMailText(text);
			}
		}
		// Doing nothing.
		else{
		}
		// Since this method only gathers information about an incoming request 
		// and we have gathered all the necessary data from that request we return nothing.
		return "";
	}

	// The outputHandler method is taking care the treatment of the data and processes
	// it accordingly so the right data is sent back to both the client and to the 
	// SMTP server. Due to this the method also contains error handling.
	public String outputHandler(){

		//Initializing  variables and resetting them
		String response = "";
		String SMTPresponse = "";
		htmlfile = "";
		String responseline;
		os = null;
		smtpsocket = null;
		is = null;


		// If the data handled in the inputHandler was and GET request
		// this outputHandler-method checks for it and answer accordingly.
		if(http_request_info.getMethod_name().equals("GET")){

			// For the GET request response the "standard" web page is loaded and 
			// sent back to the clients browser through the reLoadHTML method.
			reLoadHTML(" ");
		}

		// If the data handled in the inputHandler was and POST request
		// this outputHandler-method checks for it and answer accordingly.
		else if(http_request_info.getMethod_name().equals("POST")){

			// The variables stored in the inputHandler method are here used to
			// create a SMTP message that later is going to be sent to the SMTP server.
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

			// Before sending data to the SMTP server we have to check if the email
			// for both sender and receiver are valid addresses through the 
			// validateEmail method. If they are not, the data to the server are not
			// sent and instead the client will get an error message.
			if(validateEmail(from) == true && validateEmail(to) == true && http_request_info.getMailSubject() != null){
				
				// Removing unusual characters from the subject. 
				http_request_info.setMailSubject(http_request_info.getMailSubject().replaceAll("^\\p{L}\\p{N}]", ""));
				try {
					// Initializing necessary variables to make connection to the
					// SMTP server.

					smtpsocket = new Socket(ipAddress, 25);
					System.out.println("Connected to SMTP");
					os = new BufferedOutputStream(smtpsocket.getOutputStream());
					is = new BufferedReader(new InputStreamReader(smtpsocket.getInputStream()));

					// For prevention of errors we first check so the socket, reader
					// and writer is not empty/null.
					if(smtpsocket != null && os != null && is != null){

						// For each line that was stored earlier to be sent to the SMTP
						// server they are here sent to the sendAndGetResponse method
						// that does that and handles eventual errors that can occur.
						sendAndGetResponse(hello);
						sendAndGetResponse(mail_from);
						sendAndGetResponse(rcpt_to);
						sendAndGetResponse(data);
						sendAndGetResponse(subject + blank_line + mail_text + new_lines);
						sendAndGetResponse(quit);
					}			
				} 

				// If we can not establish a connection to the SMTP server
				// the client gets an error message as an answer.
				catch (IOException e) {
					reLoadHTML("Could not connect to the SMTP server");
					e.printStackTrace();
				}

				// ?????????????????????????????????????  
				// ?????????????????????????????????????
				try {
					while((responseline = is.readLine())!=null) {  
						System.out.println(responseline);
					}
				} catch (IOException e) {e.printStackTrace();}

				// We try to close the socket connection
				// and if it does not succeed we send an
				// error message back to the client.
				try {
					smtpsocket.close();
				} catch (IOException e) {
					reLoadHTML("Failed to close socket connection to the SMTP server.");
					e.printStackTrace();
				}
			}

			// This is where we end up if one of the email addresses 
			// are invalid and we answer the client with a message.
			else {
				reLoadHTML("Mail address invalid! Please try again.");
			}
		} 

		// This is the last stage of the outputHandler where we summarize all the data
		// that is going to be sent back to the client in one single String and last we
		// add the modified HTML file.
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

	// ????????????????????????????????????? 
	public String callNsLookup(String adr){

		String [] dnsAdr = adr.split("@");
		System.out.println(dnsAdr[1]);

		//NSlookup ns = new NSlookup();
		//String result = ns.mxLookup(dnsAdr[1]);
		//System.out.println(result);
		//return result;
		return "hej";
	}
	//================================================================================

	// The theDecoderWithSwe method decodes the message from HTML to ASCII so 
	// space do not become + and so on.
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

	// The reLoadHTML method is loading the index.html file and adds a line in it if
	// we want to "send" messages to the client
	private String reLoadHTML(String counterMeasure){

		// Initialize and resetting variables
		String line;
		htmlfile = "";

		// We read in the HTML file.
		try {
			input = new BufferedReader(new FileReader("index.html"));
		} catch (FileNotFoundException e) {e.printStackTrace();}
		try {

			// While reading one line at the time from the index.html file
			// we check if the line contains the place where we want to place the
			// modified code. When we find it we replace it and keep on doing so.
			while((line = input.readLine()) != null){
				if(line.contains("</form>")){
					line = "</form>\r\n<br><br><center><h2>"+counterMeasure+"</h2></center>";
				}
				htmlfile += line + "\r\n";
			}
		} catch (IOException e) {e.printStackTrace();}

		// Since the htmlfile-string is a global string we do not return 
		// it since we reseted it in the beginning of the method.
		return "";
	}
	//================================================================================

	// The validateEmail method validates the email of both the sender and receiver
	// through pattern checking with a certain pattern string.
	public Boolean validateEmail(String emailForValidation){

		// Initialize variable for handling if the email is valid or not.
		Boolean valid = false;
		// We start with compiling the regular expression in to a pattern.
		// Further we match the incoming string with the given pattern in EMAIL_PATTERN.
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(emailForValidation);

		// Sets the valid to true or false depending on 
		valid = matcher.matches();
		return valid;
	}

	//================================================================================

	// The sendAndGetResponse method is sending the SMTP message to the SMTP server
	// and receives messages from that SMTP server. Based on the response from the 
	// server we send the appropriate error message to the clients browser.
	public void sendAndGetResponse(String statement){
		try {
			// Here we send one line at the time to the SMTP server.
			System.out.println("<Sent: " + statement);
			os.write(statement.getBytes());
			os.flush();
		} catch (IOException e) {e.printStackTrace();}
		try {
			// Here we read one line at the time from the SMTP server.
			String response = is.readLine();
			System.out.println("<Answer: " + response);

			// Depending on the answer from the server we know that answers 
			// containing or starting with 220, 250, 354 and 221 are valid 
			// answers during a simple SMTP conversation and therefore we
			// check them. And if the response starts with 221 we know that 
			// it is a "Thanks goodbye message" that is sent when the SMTP server
			// has queued the mail and therefore we modify the page that we
			// want to return to the client.
			if(response.contains("220") == true || response.contains("250") == true || response.contains("354") == true || response.contains("221") == true){
				if(response.contains("221") == true){
					reLoadHTML("Mail was sent to "+ http_request_info.getMailTo());
					return;
				}
			} 

			// If the message do not contain 220, 250, 354 or 221 there is something
			// wrong and we send the error message to the clients browser with an 
			// apology and the encouragement to try again :D 
			else {
				reLoadHTML("Sorry, "+response+". Please try again!");
			} 
		} 

		// If we can not read the response from the SMTP server we send the error message to
		// the clients browser.
		catch (IOException e) {
			e.printStackTrace();
			reLoadHTML("Error: " + e.getMessage());
		}
	}
}
