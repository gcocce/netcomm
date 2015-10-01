package basenetgame.common;

import java.net.Socket;

public class Receiver extends Thread{

	Socket s;
	// Agregar collection
	
	public Receiver (Socket s){
		this.s=s;
	}
	
	public Packet receivePacket(){
		// Si hay paquetes en la collection devolver el paquete sino devolver null
		// Bloquear la collection
		
		
		return null;
	}
	
	public void run(){

	
		try {
			// Si se puede recibir algo agregar el paquete a la collection (bloquear recurso)
			
			
			
			
			// Sleep de 0 milisegundos para dejar que el sistema operativo
			// de paso a otro proceso o thread en este punto
			Thread.sleep(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}