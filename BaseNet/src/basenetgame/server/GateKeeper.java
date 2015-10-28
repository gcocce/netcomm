package basenetgame.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Logger;

public class GateKeeper extends Thread{

	ServerSocket serversocket;
	GameHandler gamehandler;
	boolean detener;
	
	public GateKeeper(ServerSocket sSocket, GameHandler gHandler){
		this.serversocket=sSocket;
		this.gamehandler=gHandler;
		detener=false;
	}
	
	public void run(){
		
		Logger logger = Logger.getLogger("ServerLog");  
		logger.info("GateKeeper iniciado. Escuchando conexiones...");
		
		while (!detener){
		
			try {
				// El GateKeeper se bloquea en este punto esperando conexiones entrantes
				Socket socket= serversocket.accept();
				
				System.out.println("GateKeeper. Se recibe una conexión: " + socket.getRemoteSocketAddress());
				logger.info("Se recibe una conexión: " + socket.getRemoteSocketAddress());
				
				// Luego de recibir una conexión creamos un Gestor para la Conexión
				ConnectionHandler conHandler=new ConnectionHandler(socket, gamehandler);
			
				// Iniciamos el Gestor para que resuelva que hacer con la conexión
				conHandler.start();
				
			}catch (SocketException se){
				logger.severe("GateKeeper. Error en el ServerSocket: "+ se.getMessage());
			} catch (IOException e) {
				logger.severe("GateKeeper. Error al aceptar conexion: "+ e.getMessage());
				detener=true;
			} 
		}
		
		logger.info("Finaliza el GateKeeper");
	}
	
	public void finish(){
		detener=true;
	}
}
