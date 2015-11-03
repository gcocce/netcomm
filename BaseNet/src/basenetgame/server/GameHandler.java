package basenetgame.server;

import java.util.ArrayList;
import java.util.logging.Logger;

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
		
		Logger logger = Logger.getLogger("ServerLog");  
		logger.info("Se inicia el cierre del GameHandler");
		
		// Cerramos la conexion de los clientes
		for(int x=0; x < listaClientes.size(); x++) {
			
			ClientHandler cHandler= listaClientes.get(x);
			
			cHandler.closeConnection();
		}		
	}
	
	public void run(){
		
		while(continuar){
			
			// Comprobar si hay paquetes �de qu� manera?
			for(int x=0; x < listaClientes.size(); x++) {
				
				ClientHandler cHandler= listaClientes.get(x);
				
				try {
					Thread.sleep(0);
				} catch (InterruptedException e) {}
				
				//TODO: CHEKEAR QUE EL CLIENTHANDLER ESTE LISTO HANTES DE EJECUTAR LA SIGUIENTE LINEA
				Packet packet=cHandler.getPacket();
				while(packet!=null){
					
					Logger logger = Logger.getLogger("ServerLog");  
					logger.info("GameHandler. Paquete recibido.");
					
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
				
				Logger logger = Logger.getLogger("ServerLog");  
				logger.info("GameHandler. Se reenv�a un paquete al cliente " + x);				
				
				cHandler.sendPacket(packet);	
			}
		}		
	}

}
