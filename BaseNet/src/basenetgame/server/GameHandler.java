package basenetgame.server;

import java.util.ArrayList;

import basenetgame.common.Packet;

public class GameHandler extends Thread{
	
	private ArrayList<ClientHandler> listaClientes;
	private boolean continuar=true;
	
	public GameHandler(){
		listaClientes=new ArrayList<ClientHandler>();
	}
	
	public boolean addClient(ClientHandler cH){
		
		listaClientes.add(cH);
		
		return true;
	}

	
	public void finish(){

		continuar=false;
		
		System.out.println("GameHandler. Se inicia el cierre del GameHandler");
		
		// Cerramos la conexion de los clientes
		for(int x=0; x < listaClientes.size(); x++) {
			
			ClientHandler cHandler= listaClientes.get(x);
			
			cHandler.closeConnection();
		}		
	}
	
	public void run(){
		
		while(continuar){
			
			// Comprobar si hay paquetes ¿de qué manera?
			for(int x=0; x < listaClientes.size(); x++) {
				
				ClientHandler cHandler= listaClientes.get(x);
				
				Packet packet=cHandler.getPacket();
				while(packet!=null){
					
					System.out.println("GameHandler. Paquete recibido.");
					
					procesarPacket(packet, x);
					
					packet=cHandler.getPacket();
				}
			}			
			
			
			try {
				// Sleep de 0 milisegundos para dejar que el sistema operativo
				// de paso a otro proceso o thread en este punto
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}
	}
	
	public void procesarPacket(Packet packet, int clientPos){

		// En principio reenviamos todos lo paquetes a los clientes que forman parte del juego
		for(int x=0; x < listaClientes.size(); x++) {
			
			ClientHandler cHandler= listaClientes.get(x);
			
			if (x != clientPos){
				
				System.out.println("GameHandler. Se reenvía un paquete al cliente " + x);
				
				cHandler.sendPacket(packet);	
			}
		}		
	}

}
