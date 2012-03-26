import java.io.*;
import java.net.Socket;

public class serverHandler extends Thread {
	//Initialize necessary variables, arrays, strings etc.
	private Socket clientSocket;
	boolean running = false;
	boolean correctGuess = false;
	int nrOfGuessesLeft = 3;
	int lengthOfIncomingWord = 0;
	String convertStandings, convertAttempts;
	char [] guessFromClientInToStringCheck;

	public serverHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	//What the newly created thread is going to do 
	public void run(){
		//Status messages from the server.
		System.out.println("Connection established.");
		BufferedReader in;
		BufferedOutputStream out;

		//Creating the in- and output stream.
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new BufferedOutputStream(clientSocket.getOutputStream());
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