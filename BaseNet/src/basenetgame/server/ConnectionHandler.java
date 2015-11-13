package basenetgame.server;

import java.net.Socket;
import java.util.logging.Logger;

import basenetgame.common.Protocol;
/*
 * Esta clase se encarga de manejar inicialmente la conexi�n de un cliente.
 * 
 *  Si se cumplen las condiciones agrega el cliente al Juego (GameHandler).
 *  
 */
public class ConnectionHandler extends Thread {

	Socket socket;
	GameHandler gamehandler;
	
	public ConnectionHandler(Socket s, GameHandler gHandler){
		this.gamehandler=gHandler;
		this.socket=s;
	}
	
	// Buscar si hay lugar en alguna sala o juego o crear uno y agregarlo a la lista junto con el cliente
	public void run(){
		
		// El gestor de la conexi�n debe comprobar si se cumplen las condiciones 
		// para aceptar una conexi�n. En tal caso se debe usar el Protocolo para 
		// iniciar la conexi�n.
		Logger logger = Logger.getLogger("ServerLog");  
		
		Protocol prot=new Protocol();
		
		// Comprobamos si hay espacio en la sala
		if(gamehandler.isComplete()){
			
			prot.RechazarConexion(socket, "Juego Completo, no hay espacio para otro jugador!");
			logger.info("Conexi�n rechazada: Juego Completo.");
			
		}else if (prot.AceptarConexion(socket)){
			
			logger.info("Conexi�n aceptada.");
			
			// Crear un Cliente y agregar el objeto en alguna Sala
			ClientHandler cHandler= new ClientHandler(socket);

			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
			
			gamehandler.addClient(cHandler);
				
		}else{
			logger.info("Conexi�n no aceptada: " +  prot.getError());
		}
	}
}
