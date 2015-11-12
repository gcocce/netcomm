package basenetgame.common;

import java.net.Socket;
import java.util.ArrayList;

public class Receiver extends Thread{

	Socket socket;
	Protocol protocol;
	Protocol.Status status;
	
	// Agregar collection para guardar temporalmente los paquetes recibidos
	ArrayList<Packet> paquetes;
	
	// Indica si el thread debe seguir trabajando
	boolean continuar;
	
	public Receiver (Socket s){
		continuar=true;
		
		this.socket=s;
		protocol = new Protocol();
		status=protocol.getStatus();
				
		paquetes= new ArrayList<Packet>();
	}
	
	public Protocol.Status getStatus(){
		return protocol.getStatus();
	}
	
	public synchronized Packet receivePacket(){
		// Si hay paquetes en la collection devolver el paquete sino devolver null

		if(!paquetes.isEmpty()){
			
			return paquetes.remove(0);
		}else{
			return null;	
		}		
	}
	
	private synchronized void addPacket(Packet packet){
		// Agregar el paquete a la collection
		
		paquetes.add(packet);
	}
	
	public void finish(){
		continuar=false;
	}	
	
	public void run(){
		
		try {

			protocol.setStatus(Protocol.Status.CONNECTED);
			
			Packet packet = protocol.RecibirPaquete(socket);
			while (continuar && packet!=null){
				
				addPacket(packet);
				
				packet = protocol.RecibirPaquete(socket);	
			}
			
			if (continuar && protocol.getStatus()==Protocol.Status.BROKEN){
				System.out.println("Receiver. El otro extremo ha cerrado la conexión.");
			}
			
			// Sleep de 0 milisegundos para dejar que el sistema operativo
			// de paso a otro proceso o thread en este punto
			Thread.sleep(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}