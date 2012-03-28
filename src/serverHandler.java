import java.io.*;
import java.net.Socket;

// Taken from homework 1 in ID2212
public class serverHandler extends Thread {
	//Initialize necessary variables, arrays, strings etc.
	private Socket clientSocket;
	private ProtocolHandler newHandler;

	public serverHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	//What the newly created thread is going to do 
	public void run(){
		
		
		//Status messages from the server.
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
			while(in.ready() != false){
				numberOfCharsInData = in.read(incomingData, 0, 5000);
				inputOfData = inputOfData + String.valueOf(incomingData, 0, numberOfCharsInData);
			}
			lengthOfData = inputOfData.split("\r\n|\r|\n").length;

			if(inputOfData != null && (inputOfData.startsWith("GET") == true || inputOfData.startsWith("POST") == true)){
				for(int i=0; i<lengthOfData; i++){
					linesRead = inputOfData.split("\r\n|\r|\n")[i];
					newHandler.inputHandler(linesRead);
				}
				outputOfData = newHandler.outputHandler();
				out.print(outputOfData);
			}else{
				System.out.println("<NO DATA TO READ!");
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