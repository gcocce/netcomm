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
		
		// El gestor de la conexi�n debe comprobar si se cumplen las condicienes 
		// para aceptar una conexi�n. En tal caso se debe usar el Protocolo para 
		// iniciar la conexi�n.
		Logger logger = Logger.getLogger("ServerLog");  
		
		Protocol prot=new Protocol();
		
		if (prot.AceptarConexion(socket)){
			
			logger.info("ConnectionHandler. Conexi�n aceptada.");
			
			// Crear un Cliente y agregar el objeto en alguna Sala
			ClientHandler cHandler= new ClientHandler(socket);

			// Agregamos el cliente a la sala
			gamehandler.addClient(cHandler);
		}else{
			logger.info("ConnectionHandler. Conexi�n no aceptada: " +  prot.getError());
		}
	}
}
