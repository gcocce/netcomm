package basenetgame.server;

import java.net.Socket;

public class ConnectionHandler extends Thread {

	Socket s;
	
	public ConnectionHandler(Socket s){
		this.s=s;
	}
	
	
	// Buscar si hay lugar en alguna sala o juego o crear uno y agregarlo a la lista junto con el cliente
	public void run(){
		
	}
}
