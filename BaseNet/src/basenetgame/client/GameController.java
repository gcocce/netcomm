package basenetgame.client;

import basenetgame.common.Packet;

public class GameController extends Thread implements PacketListener{

	public GameController(){

	}
	
	private boolean initComm(String host, int puerto){
		CommModule comModule = new CommModule(this);	
		
		if (comModule.IniciarConexion( host, puerto)){
			System.out.println("Se inicio correctamente la comunicación");
			return true;
		}else{
			System.out.println("Error al iniciar la comunicación");
			return false;
		}		
	}
	
	public void run(){
		
		do{
			initComm("localhost",5000);

			
			try {			
				// Sleep de 0 milisegundos para dejar que el sistema operativo
				// de paso a otro proceso o thread en este punto
				Thread.sleep(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}while(true);
	}

	@Override
	public void OnPacketReceived(Packet p) {
		
		System.out.println("GameController llego paquete: " + p.toString());
		
	}
}
