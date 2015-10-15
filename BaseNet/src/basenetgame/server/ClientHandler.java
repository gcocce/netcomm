package basenetgame.server;

import java.io.IOException;
import java.net.Socket;

import basenetgame.common.Packet;
import basenetgame.common.Receiver;
import basenetgame.common.Sender;

// Referencia al cliente del lado del servidor 
public class ClientHandler {

	private Socket socket;
	private Sender sender;
	private Receiver receiver;
	
	public ClientHandler(Socket s){
		this.socket=s;
		
		// Crear el Reciever y el Sender
		sender= new Sender(socket);
		
		sender.start();
		
		receiver= new Receiver(socket);
		
		receiver.start();
	}
	
	public Packet getPacket(){
		return receiver.receivePacket();
	}
	
	public void sendPacket(Packet packet){
		sender.sendPacket(packet);
	}
	
	// En este método que se debe llamar obligatoriamente cerramos todos los recursos utilizados por el ClientHandler
	public void closeConnection(){
		
		System.out.println("ClientHandler. Se inicia el cierre del ClientHandler.");
		
		try {
			socket.close();
			
			sender.finish();
			
			receiver.finish();
			
			try {
				sender.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			try {
				receiver.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
