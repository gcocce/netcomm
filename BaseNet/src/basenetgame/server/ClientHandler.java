package basenetgame.server;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import basenetgame.common.Packet;
import basenetgame.common.Protocol;
import basenetgame.common.Receiver;
import basenetgame.common.Sender;

// Referencia al cliente del lado del servidor 
public class ClientHandler {

	public enum Status{DEFAULT, CREATED, READY, BROKEN};
	
	private Socket socket;
	private Sender sender;
	private Receiver receiver;
	
	private Status estado;
	
	private String Nick;
	
	public ClientHandler(Socket s){
		this.socket=s;
		
		estado=Status.DEFAULT;
		
		// Crear el Reciever y el Sender
		sender= new Sender(socket);
		
		sender.start();
		
		receiver= new Receiver(socket);
		
		receiver.start();
		
		estado=Status.CREATED;
	}
	
	public Status getStatus(){
		 
		// DEBUG
		if (estado==Status.DEFAULT){
			return estado;
		}
		
		// DEBUG
		if(receiver==null){
			System.out.println("receiver is null");
		}
		
		// DEBUG
		if(sender==null){
			System.out.println("sender is null");
		}		
		
		if (receiver.getStatus()==Protocol.Status.CONNECTED && sender.getStatus()==Protocol.Status.CONNECTED){
			estado=Status.READY;
		}else if (receiver.getStatus()==Protocol.Status.BROKEN || sender.getStatus()==Protocol.Status.BROKEN){
			estado=Status.BROKEN;
		}
		
		return estado;
	}
	
	public String getNick() {
		return Nick;
	}

	public void setNick(String nick) {
		Nick = nick;
	}

	public Packet getPacket(){
		return receiver.receivePacket();
	}
	
	public void sendPacket(Packet packet){
		sender.sendPacket(packet);
	}
	
	// En este m�todo que se debe llamar obligatoriamente cerramos todos los recursos utilizados por el ClientHandler
	public void closeConnection(){
		
		Logger logger = Logger.getLogger("ServerLog");  
		logger.info("Se inicia el cierre del ClientHandler.");
		
		try {
			sender.finish();
			
			receiver.finish();
			
			socket.close();			
			
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
