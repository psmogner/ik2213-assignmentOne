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
		
		
		System.out.println("INPUT: " + inputString);


		/* Get Req */
		if(inputString.startsWith("GET")){

			System.out.println("inputHandler I get metoden \n");
			get_array = inputString.split(" ");
			System.out.println(get_array[0] + " 1: " + get_array[1] + " 2: " + get_array[2]);

			http_request_info.setMethod_name(get_array[0]);
			http_request_info.setLocal_path(get_array[1]);
			http_request_info.setProtocol_version(get_array[2]);

		}
		/*Post Req*/ 
		else if(inputString.startsWith("POST")){
			System.out.println("inputHandler I post metoden ");

			get_array = inputString.split(" ");
			System.out.println(get_array[0] + " 1: " + get_array[1] + " 2: " + get_array[2]);

			http_request_info.setMethod_name(get_array[0]);
			http_request_info.setLocal_path(get_array[1]);
			http_request_info.setProtocol_version(get_array[2]);
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

			System.out.println(" I outputHandler EQUALS get");

			try {
				input = new BufferedReader(new FileReader("index.html"));
				System.out.println("Filen is read");
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
		}
		else if(http_request_info.getMethod_name().equalsIgnoreCase("POST") == true && http_request_info.getLocal_path().equalsIgnoreCase("/submitmail") == true){
			System.out.println("I post output");

		}



		response = "HTTP/1.0 200 OK\r\n";
		response += "Content-Type: text/html\r\n";
		response += "Content-Length " + htmlfile.length() + "\r\n";
		response += "Server: IK2213A1 Server \r\n";
		response += "Connection: Close \r\n";
		response += "\r\n";
		response += htmlfile;

		System.out.println(response);
		return response;



		//		else if(){
		//			
		//		}





	}	
}


