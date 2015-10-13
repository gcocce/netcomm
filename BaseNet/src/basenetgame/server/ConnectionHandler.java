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
		
		// El gestor de la conexi�n debe comprobar si se cumplen las condicienes 
		// para aceptar una conexi�n. En tal caso se debe usar el Protocolo para 
		// iniciar la conexi�n.
		
		Protocol prot=new Protocol();
		
		if (prot.AceptarConexion(s)){
			
			System.out.println("ConnectionHandler. Conexi�n aceptada.");
			
			// Crear un Cliente y agregar el objeto en alguna Sala
			ClientHandler cHandler= new ClientHandler(s);
			
			// TODO: Agregar el Gestor del Cliente a una Sala
	
			
			// TODO: REMOVER. Momentaneamente cerramos la conexi�n aqu�
			cHandler.closeConnection();
			
		}else{
			System.out.println("ConnectionHandler. Conexi�n no aceptada: " +  prot.getError());
		}
	}
}
