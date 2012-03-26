import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
		
		public static void main(String[] args) throws IOException {
			boolean listening = true;
			ServerSocket serverSocket = null;
		
		try {
			//Creating a socket that listens to a specific port
			serverSocket = new ServerSocket(8000);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 80.");
			System.exit(1);
		}

		//Listening stage
		while(listening) {
			//Listening to the port for incoming connection
			Socket clientSocket = serverSocket.accept();
			//Accepting the socket connection and
			//create a new Thread in serverHandler
			(new serverHandler(clientSocket)).start(); 
		}
		serverSocket.close();
	}
}
