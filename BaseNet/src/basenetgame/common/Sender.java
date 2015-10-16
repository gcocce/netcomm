package basenetgame.common;

import java.net.Socket;
import java.util.ArrayList;

public class Sender extends Thread{
	
	Socket socket;
	Protocol protocol;
	
	// Agregar Collection para guardar temporalmente los paquetes a enviar
	ArrayList<Packet> paquetes;

	// Indica si el thread debe seguir trabajando
	boolean continuar;

	public Sender (Socket s){
		continuar=true;
		
		this.socket=s;
		protocol = new Protocol();
		
		paquetes= new ArrayList<Packet>();
	}
	
	public synchronized void sendPacket(Packet packet){
		//Agregar paquete a la collection
		
		paquetes.add(packet);
	}
	
	private synchronized Packet getPacket(){
		
		if(!paquetes.isEmpty()){
			
			return paquetes.remove(0);
		}else{
			return null;	
		}
	}
	
	public void finish(){
		continuar=false;
	}
	
	public void run(){

		try {

			while(continuar){
				
				//Si hay paquetes en la colection obtenerlo usando el método getPacket() y enviarlo
				Packet packet=getPacket();
				while(continuar && packet!=null){
					
					System.out.println("Sender. Se envía un paquete por la red.");
					
					// Enviar el paquete
					protocol.EnviarPaquete(socket, packet);
					
					System.out.println("Sender. Paquete enviado.");
					
					packet=getPacket();
				}

				// Sleep de 0 milisegundos para dejar que el sistema operativo
				// de paso a otro proceso o thread en este punto
				Thread.sleep(0);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
