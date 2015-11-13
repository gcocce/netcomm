package basenetgame.server;

import java.util.ArrayList;
import java.util.logging.Logger;

import basenetgame.common.LostClientPacket;
import basenetgame.common.Packet;
import basenetgame.common.StartGamePacket;

public class GameHandler extends Thread{
	
	public enum Status{DEFAULT, WATTING, STARTED, BROKEN};
	
	Status status;
	
	// Lista de jugadores
	private ArrayList<ClientHandler> listaClientes;
	
	// Cantidad de jugadores del juego o la sala
	private int GAME_PLAYERS = 2;
	
	// Variable usada para detener el juego
	private boolean continuar=true;
	
	// Variable usada para determinar si el juego o la sala tiene el numero de jugadores esperado
	private boolean complete=false;
	
	public GameHandler(){
		status=Status.WATTING;
		listaClientes=new ArrayList<ClientHandler>();
	}
	
	public void setGamePlayers(int size){
		this.GAME_PLAYERS=size;
	}
	
	public boolean addClient(ClientHandler cH){
		
		if(listaClientes.size()< GAME_PLAYERS){
			complete=false;
			listaClientes.add(cH);
		}
		
		if(listaClientes.size()== GAME_PLAYERS){
			complete=true;
		}
		
		return true;
	}
	
	public boolean isComplete(){
		return complete;
	}
	
	//***********************************************************************
	// Metodo llamado al finalizar el servidor	
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
			
			//***********************************************************************
			// Chequeamos que estén todos los jugadores para iniciar el juego
			if (complete && status==Status.WATTING){
				
				boolean ready=true;
				
				for(int x=0; x < listaClientes.size(); x++) {
					
					ClientHandler cHandler= listaClientes.get(x);
					
					if (cHandler.getStatus()!=ClientHandler.Status.READY){
						ready=false;
					}
				}
				
				// Se informa a todos los jugadores cuando la sala esta completa
				if (ready){
					
					StartGamePacket sgp=new StartGamePacket();
					
					boadcastPacket(sgp);
					
					status=Status.STARTED;
					
					logger.info("Se informa el inicio del juego. ");
					System.out.println("Juego Iniciado.");
				}
			}
			
			//***********************************************************************
			// Comprobar si hay paquetes recibidos para procesar
			for(int x=0; x < listaClientes.size(); x++) {
				
				ClientHandler cHandler= listaClientes.get(x);

				try{
					
					// Debug
					if (cHandler==null){
						System.out.println("Client Null!");
					}
					
					// Aveces tira error NullPointerException en la siguiente linea
					// porque el cliente es null
					if (cHandler.getStatus()==ClientHandler.Status.READY){

						Packet packet=cHandler.getPacket();
						while(packet!=null){

							logger.info("Paquete recibido. " + packet.getType());

							procesarPacket(packet, x);

							packet=cHandler.getPacket();
						}
					}

					if (cHandler.getStatus()==ClientHandler.Status.BROKEN){

						LostClientPacket lcp=new LostClientPacket();
						lcp.setUser("Jugador " + x);

						procesarPacket(lcp, x);

						listaClientes.remove(x);

						complete=false;
						status=Status.WATTING;

						//TODO: Reinicializar el juego si fuera necesario

						System.out.println("Se perdió la conexion del jugador " + x);
						logger.info("Se perdió la conexion del jugador " + x);

						cHandler.closeConnection();
					}

				}
				
				catch (NullPointerException e){
					e.printStackTrace();
					logger.severe("NullPointerException" + e.getStackTrace().toString());
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
		
		logger.info("Finaliza el GameHandler");
	}
	
	//***********************************************************************
	// Metodo para procesar los tistintos paquetes intercambiados por los jugadores
	// Parametros: paquete, indice del jugador que envió el paquete en la lista de jugadores
	public void procesarPacket(Packet packet, int clientPos){

		Logger logger = Logger.getLogger("ServerLog"); 
		
		//TODO: Comprobar el tipo de paquete y determinar si hay que hacer cambios
		// al modelo del juego
		
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
	
	//***********************************************************************
	// Metodo para enviar un paquete a todos los jugadores
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
