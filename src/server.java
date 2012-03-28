import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class server {

	public static void main(String[] args) throws IOException {
		boolean listening = true;
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(8000);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 8000.");
			System.exit(1);
		}

		
		while(listening) {
			Socket clientSocket = serverSocket.accept();
			(new serverHandler(clientSocket)).start(); 
		}
		serverSocket.close();
	}
}
