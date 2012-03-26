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

		
		try {
			//Creating necessary variables
			String guessFromClientInToString;
			String rightWord;
			
			while(true){
				//Creating necessary variables
				guessFromClientInToString = in.readLine();	
				rightWord = newWord();
				rightWord = rightWord.toLowerCase();
				boolean checkIfStart;
				checkIfStart= guessFromClientInToString.equals("START GAME");
				
				//Status messages from the server.
				System.out.println("Right word: " + rightWord);
				System.out.println("Value of checkIfStart: " + checkIfStart);

				//Checking if the client starts a new game
				if(checkIfStart == true){
					for(int i=0; i<rightWord.length(); i++) {
						out.write('-');
					}
					//Writing necessary output to client
					out.write("\n".getBytes());
					nrOfGuessesLeft = 3;
					convertStandings = Integer.toString(nrOfGuessesLeft);
					out.write(convertStandings.getBytes());
					out.write("\n".getBytes());
					out.flush();
					running=true;
					
					//Status messages from the server.
					System.out.println("Length of the word is: " + rightWord.length());
					System.out.println("------------------------------");
				}

				// Initialize current game array s
				String resultMessage = "Congratulations";
				char[] progressFromClient = new char[rightWord.length()];
				for(int i=0; i<rightWord.length(); i++){
					progressFromClient[i] = '-';
				}
				
				while(running){
					//Creating necessary variables
					byte[] rightWordArray = rightWord.getBytes();
					if(nrOfGuessesLeft==0){
						break;
					}
					try{
					guessFromClientInToString = in.readLine();
					lengthOfIncomingWord = guessFromClientInToString.length();
					guessFromClientInToString = guessFromClientInToString.toLowerCase();
					
					} catch (NullPointerException e){
						System.out.println(e.toString());
					}
					//Status messages from the server.
					System.out.println("Length of guessed word is: " + lengthOfIncomingWord);
					System.out.println("Right word is: " + rightWord);
					System.out.println("Guess from client is: " + guessFromClientInToString);
					System.out.println("Attempts left: "+ nrOfGuessesLeft);
					System.out.println("------------------------------");
					
					//Handling the guess of one single letter from the client
					if(lengthOfIncomingWord == 1){
						guessFromClientInToStringCheck = guessFromClientInToString.toCharArray();
						
						//Checking if the client guessed write
						for(int i=0; i<rightWordArray.length; i++){
							if(rightWordArray[i] == guessFromClientInToStringCheck[0]){
								progressFromClient[i] = guessFromClientInToStringCheck[0];
								out.write(guessFromClientInToStringCheck[0]);
								correctGuess = true;
							} else {
								out.write(progressFromClient[i]);
							}
						}
						
						//Changing the attemptes remaining to the client
						if(correctGuess != true){
							nrOfGuessesLeft--;
							convertStandings = Integer.toString(nrOfGuessesLeft);
							resultMessage = "Wrong letter";
						} 
						
						out.write("\n".getBytes());
						String progressFromClientInToString = new String(progressFromClient);
						
						//If the client guesses right word through a single letter.
						if(rightWord.equals(progressFromClientInToString)){
							convertStandings = "666";							
							out.write(convertStandings.getBytes());
							out.write("\n".getBytes());
							out.write("You guessed the whole word! Congratulations!!\n".getBytes());					
						}
						//Writing necessary output to client
						out.write(convertStandings.getBytes());
						out.write("\n".getBytes());
						out.write(resultMessage.getBytes());
						out.write("\n".getBytes());
						correctGuess = false;
						resultMessage = "Congratulatons";
						out.flush();

						//Check if the client guesses for the whole word
					} else if(lengthOfIncomingWord == rightWord.length()) {
						//Checks if its right
						if(rightWord.equals(guessFromClientInToString)){
							out.write(rightWord.getBytes());
							out.write("\n".getBytes());
							convertStandings ="666";
							out.write(convertStandings.getBytes());
							out.write("\n".getBytes());
							out.write("You guessed the whole word! Congratulations!!\n".getBytes());
							running = false;
						//If it was wrong the necessary info is sent to the client
						} else {
							for(int i =0; i<rightWord.length(); i++){
								out.write(progressFromClient[i]);
							}
							out.write("\n".getBytes());
							nrOfGuessesLeft--;
							convertStandings = Integer.toString(nrOfGuessesLeft);
							out.write(convertStandings.getBytes());
							out.write("\n".getBytes());
							out.write("Wrong guess! =(\n".getBytes());
						}
						out.flush();
					//If the input from the client isn't right.
					} else {
						for(int i = 0; i<rightWord.length(); i++){
							out.write(progressFromClient[i]);
						}
						//Writing necessary output to client
						out.write("\n".getBytes());
						out.write(convertStandings.getBytes());
						out.write("\n".getBytes());
						out.write("Wrong input! You can only type 1 or ".getBytes());

						convertAttempts = Integer.toString(rightWord.length());
						out.write(convertAttempts.getBytes());
						out.write(" letters".getBytes());
						out.write("\n".getBytes());
						out.flush();
					}
				}
			}
		}
		catch(IOException e) {
			System.out.println(e.toString());
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
	//Method to randomize a word from the dictionary.
	private String newWord(){
		try {
			BufferedReader theReader = null;
			InputStream fstream = serverHandler.class.getResourceAsStream("lista.txt");
			DataInputStream data = new DataInputStream(fstream);
			theReader = new BufferedReader(new InputStreamReader(data));
			String rightWord = null;
			int randomNumber = (int)(Math.random()*25143);
			//Status messages from the server.
			System.out.println("Random number is: " + randomNumber);

			for(int i =0 ; i<=randomNumber ; i++){
				if(i!=randomNumber){
					theReader.readLine();
				} else {
					rightWord = theReader.readLine();
					break;				}
			}
			return rightWord;
		} catch(IOException e) {
			System.out.println(e.toString());
			return null;
		}
	}
}