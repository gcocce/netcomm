package basenetgame.server;

import java.util.ArrayList;

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
		// Cerramos la conexion de los clientes
		for(int x=0; x < listaClientes.size(); x++) {
			
			ClientHandler cHandler= listaClientes.get(x);
			
			cHandler.closeConnection();
		}		
		
		continuar=false;
	}
	
	public void run(){
		
		
		while(continuar){
			
			
			
			try {
				// Sleep de 0 milisegundos para dejar que el sistema operativo
				// de paso a otro proceso o thread en este punto
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}
	}

}
