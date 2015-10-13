package basenetgame.server;

import java.io.IOException;
import java.net.Socket;

// Referencia al cliente del lado del servidor 
public class ClientHandler {

	private Socket socket;
	
	public ClientHandler(Socket s){
		this.socket=s;
		
		//TODO: crear el Reciever y el Sender
		
	}
	
	public void closeConnection(){
		try {
			
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
