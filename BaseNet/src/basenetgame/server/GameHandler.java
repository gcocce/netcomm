package basenetgame.server;

import java.util.ArrayList;
import java.util.logging.Logger;

import basenetgame.common.LostClientPacket;
import basenetgame.common.Packet;
import basenetgame.common.StartGamePacket;

public class GameHandler extends Thread{
	
	public enum Status{DEFAULT, WATTING, STARTED, BROKEN};
	
	Status status;
	
	private ArrayList<ClientHandler> listaClientes;
	
	private int GAME_PLAYERS = 2;
	
	private boolean continuar=true;
	private boolean complete=false;
	
	public GameHandler(){
		status=Status.WATTING;
		listaClientes=new ArrayList<ClientHandler>();
	}
	
	public void setGamePlayers(int size){
		this.GAME_PLAYERS=size;
	}
	
	public boolean addClient(ClientHandler cH){
		
		listaClientes.add(cH);
		
		if (isComplete()){
			complete=true;			
		}
		
		return true;
	}
	
	public boolean isComplete(){
		
		if(listaClientes.size()< GAME_PLAYERS){
			return false;
		}else{
			return true;
		}
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
		
		Logger logger = Logger.getLogger("ServerLog");
		
		while(continuar){
			
			if (complete && status==Status.WATTING){
				boolean ready=true;
				
				for(int x=0; x < listaClientes.size(); x++) {
					
					ClientHandler cHandler= listaClientes.get(x);
					
					if (cHandler.getStatus()!=ClientHandler.Status.READY){
						ready=false;
					}
				}
				
				if (ready){
					
					StartGamePacket sgp=new StartGamePacket();
					
					boadcastPacket(sgp);
					
					logger.info("Paquete recibido. ");
					
					status=Status.STARTED;
				}
				
			}
			
			// Comprobar si hay paquetes ¿de qué manera?
			for(int x=0; x < listaClientes.size(); x++) {
				
				ClientHandler cHandler= listaClientes.get(x);
							
				if (cHandler.getStatus()==ClientHandler.Status.READY){
					
					Packet packet=cHandler.getPacket();
					while(packet!=null){
						
  						logger.info("Paquete recibido. " + packet.getType());
						
						procesarPacket(packet, x);
						
						packet=cHandler.getPacket();
					}
				}

				if (cHandler.getStatus()==ClientHandler.Status.BROKEN){

					
					
					//TODO: Informar a los otros clientes y esperar por la conexion de un nuevo cliente
					LostClientPacket lcp=new LostClientPacket();
					lcp.setUser("User "+x);
					
					procesarPacket(lcp, x);
					
					listaClientes.remove(x);
					
					System.out.println("Se perdió la conexion de un cliente");
					
					cHandler.closeConnection();
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

		Logger logger = Logger.getLogger("ServerLog"); 
		
		// En principio reenviamos todos lo paquetes a los clientes que forman parte del juego
		for(int x=0; x < listaClientes.size(); x++) {
			
			ClientHandler cHandler= listaClientes.get(x);
			
			if (x != clientPos){
				
				// Chequeamos el estado del cliente
				if (cHandler.getStatus()==ClientHandler.Status.READY){
					
					logger.info("Se reenvía un paquete al cliente " + x);				
					
					cHandler.sendPacket(packet);	
				}
			}
		}		
	}
	
	public void boadcastPacket(Packet packet){

		Logger logger = Logger.getLogger("ServerLog"); 
		
		// En principio reenviamos todos lo paquetes a los clientes que forman parte del juego
		for(int x=0; x < listaClientes.size(); x++) {
			
			ClientHandler cHandler= listaClientes.get(x);
				
			// Chequeamos el estado del cliente
			if (cHandler.getStatus()==ClientHandler.Status.READY){
				
				logger.info("Se envía un paquete al cliente " + x);				
				
				cHandler.sendPacket(packet);	
			}
		}		
	}	
}
