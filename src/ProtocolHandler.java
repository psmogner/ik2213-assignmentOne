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

	}


	/* Handle input */

	public String inputHandler(String inputString){

		/* Get Req */
		if(inputString.startsWith("GET")){
			
			System.out.println("inputHandler I get metoden \n");
			get_array = inputString.split(" ");
			http_request_info = new HttpRequest(get_array[0], get_array[1], get_array[2]);

		}
		/*Post Req*/ 
		else if(inputString.startsWith("POST")){
			System.out.println("inputHandler I post metoden ");
		}
		return "";
	}


	/* Handle output */
	public String outputHandler(){

		String response = "";
		//System.out.println("Test" + http_request_info.getMethod_name());
		//if(http_request_info.getMethod_name().equalsIgnoreCase("GET") == true){
		
		if(true){
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
			response = "HTTP/1.0 200 OK\r\n";
			response += "Content-Type: text/html\r\n";
			response += "Content-Length " + htmlfile.length() + "\r\n";
			response += "Server: IK2213A1 Server \r\n";
			response += "Connection: Close \r\n";
			response += "\r\n";
			response += htmlfile;
			
			System.out.println(response);
			return response;
		}


		//		else if(){
		//			
		//		}



		return "";


	}	
}


