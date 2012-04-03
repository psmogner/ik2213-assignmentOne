import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// The server class that listens to port 8000
public class server {

	public static void main(String[] args) throws IOException {
		
		// Creating necessary variables
		boolean listening = true;
		ServerSocket serverSocket = null;

		// We try to create a new ServerSocket
		try {
			serverSocket = new ServerSocket(8000);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 8000.");
			System.exit(1);
		}

		// After creating a new ServerSocket successfully we start to listen to that port and do so 
		// until we find an attemped to connect.
		while(listening) {
			Socket clientSocket = serverSocket.accept();
			(new serverHandler(clientSocket)).start(); 
		}
		serverSocket.close();
	}
}
