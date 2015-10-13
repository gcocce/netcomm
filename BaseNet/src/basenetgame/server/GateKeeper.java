package basenetgame.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GateKeeper extends Thread{

	ServerSocket ss;
	boolean detener;
	
	public GateKeeper(ServerSocket ss){
		this.ss=ss;
		detener=false;
	}
	
	public void run(){
		
		while (!detener){
		
			try {
				// El GateKeeper se bloquea en este punto esperando conexiones entrantes
				Socket s= ss.accept();
				
				// Luego de recibir una conexión creamos un Gestor para la Conexión
				ConnectionHandler conHandler=new ConnectionHandler(s);
			
				// Iniciamos el Gestor para que resuelva que hacer con la conexión
				conHandler.start();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void Detener(){
		detener=true;
	}
	
	
}
