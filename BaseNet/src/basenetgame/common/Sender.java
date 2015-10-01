package basenetgame.common;

import java.net.Socket;

public class Sender extends Thread{
	Socket s;
	
	// agregar collection
	
	
	public Sender (Socket s){
		this.s=s;
	}
	
	public void sendPacket(Packet p){
		//Agregar paquete a la collection
		// Este metodo debe bloquear el recurso
		
	}
	
	public void run(){

	
		try {
			//Si hay paquetes en la colection obtenerlo y enviarlo
			// Aquì se debe bloquear el recurso
			
			
			
			// Sleep de 0 milisegundos para dejar que el sistema operativo
			// de paso a otro proceso o thread en este punto
			Thread.sleep(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
