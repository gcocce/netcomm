package basenetgame.server;

import java.net.Socket;

import basenetgame.common.Protocol;

public class ConnectionHandler extends Thread {

	Socket s;
	
	public ConnectionHandler(Socket s){
		this.s=s;
	}
	
	// Buscar si hay lugar en alguna sala o juego o crear uno y agregarlo a la lista junto con el cliente
	public void run(){
		
		// El gestor de la conexión debe comprobar si se cumplen las condicienes 
		// para aceptar una conexión. En tal caso se debe usar el Protocolo para 
		// iniciar la conexión.
		
		Protocol prot=new Protocol();
		
		if (prot.AceptarConexion(s)){
			
			System.out.println("ConnectionHandler. Conexión aceptada.");
			
			// Crear un Cliente y agregar el objeto en alguna Sala
			ClientHandler cHandler= new ClientHandler(s);
			
			// TODO: Agregar el Gestor del Cliente a una Sala
	
			
			// TODO: REMOVER. Momentaneamente cerramos la conexión aquí
			cHandler.closeConnection();
			
		}else{
			System.out.println("ConnectionHandler. Conexión no aceptada: " +  prot.getError());
		}
	}
}
