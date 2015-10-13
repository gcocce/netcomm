package basenetgame.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class GateKeeper extends Thread{

	ServerSocket ss;
	boolean detener;
	
	public GateKeeper(ServerSocket ss){
		this.ss=ss;
		detener=false;
	}
	
	public void run(){
		
		System.out.println("GateKeeper iniciado. Escuchando conexiones...");
		
		while (!detener){
		
			try {
				// El GateKeeper se bloquea en este punto esperando conexiones entrantes
				Socket s= ss.accept();
				
				System.out.println("GateKeeper. Se recibe una conexión: " + s.getRemoteSocketAddress());
				
				// Luego de recibir una conexión creamos un Gestor para la Conexión
				ConnectionHandler conHandler=new ConnectionHandler(s);
			
				// Iniciamos el Gestor para que resuelva que hacer con la conexión
				conHandler.start();
				
			}catch (SocketException se){
				System.out.println("GateKeeper. Error en el ServerSocket: "+ se.getMessage());
			} catch (IOException e) {
				//e.printStackTrace();
				System.out.println("GateKeeper. Error al aceptar conexion: "+ e.getMessage());
				detener=true;
			} 
		}
		
		System.out.println("Finaliza el GateKeeper");
	}
	
	public void Detener(){
		detener=true;
	}
}
