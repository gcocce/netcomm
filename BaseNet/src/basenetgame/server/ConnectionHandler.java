package basenetgame.server;

import java.net.Socket;
import java.util.logging.Logger;

import basenetgame.common.Protocol;

public class ConnectionHandler extends Thread {

	Socket socket;
	GameHandler gamehandler;
	
	public ConnectionHandler(Socket s, GameHandler gHandler){
		this.gamehandler=gHandler;
		this.socket=s;
	}
	
	// Buscar si hay lugar en alguna sala o juego o crear uno y agregarlo a la lista junto con el cliente
	public void run(){
		
		// El gestor de la conexión debe comprobar si se cumplen las condicienes 
		// para aceptar una conexión. En tal caso se debe usar el Protocolo para 
		// iniciar la conexión.
		Logger logger = Logger.getLogger("ServerLog");  
		
		Protocol prot=new Protocol();
		
		if (prot.AceptarConexion(socket)){
			
			logger.info("ConnectionHandler. Conexión aceptada.");
			
			// Crear un Cliente y agregar el objeto en alguna Sala
			ClientHandler cHandler= new ClientHandler(socket);

			// Agregamos el cliente a la sala
			gamehandler.addClient(cHandler);
		}else{
			logger.info("ConnectionHandler. Conexión no aceptada: " +  prot.getError());
		}
	}
}
