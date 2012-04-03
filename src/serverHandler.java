import java.io.*;
import java.net.Socket;

// Taken from homework 1 in ID2212 by Niklas Johnson and Peter Smogner
public class serverHandler extends Thread {
	// Initialize necessary variables, arrays, strings etc.
	private Socket clientSocket;
	private ProtocolHandler newHandler;

	public serverHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	// What the newly created thread is going to do 
	public void run(){
		
		// Status messages from the server and initialization of reader and writer.
		// A char array is also created to hold the incoming message and a new ProtocolHandler 
		// is created which acts as the protocol.
		System.out.println("Connection established.");
		BufferedReader in = null;
		PrintWriter out = null;
		char[] incomingData = new char[5000];
		newHandler = new ProtocolHandler();
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream());
			int numberOfCharsInData=0, lengthOfData;
			String inputOfData="";
			String outputOfData, linesRead;
			
			// While we have something to read from the clients browser request we do so and store it.
			while(in.ready() != false){
				numberOfCharsInData = in.read(incomingData, 0, 5000);
				inputOfData = inputOfData + String.valueOf(incomingData, 0, numberOfCharsInData);
			}
			// Store the length of the data for later use.
			lengthOfData = inputOfData.split("\r\n|\r|\n").length;

			// First a security check to make sure that no variable is null and responsible to a nullpoiner exception.
			if(inputOfData != null && (inputOfData.startsWith("GET") == true || inputOfData.startsWith("POST") == true)){
				
				// For the data earlier read we read it linewise and send it to our ProtocolHandler.
				for(int i=0; i<lengthOfData; i++){
					linesRead = inputOfData.split("\r\n|\r|\n")[i];
					newHandler.inputHandler(linesRead);
				}
				
				// We get the appropriate response from the protocol handler after that.
				outputOfData = newHandler.outputHandler();
				out.print(outputOfData);
			}else{
				System.out.println("<There is no data to read.");
			}
		} catch (IOException e) {
			System.out.println(e.toString());
			return;
		}	
		//Closing the socket
		try {
			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}