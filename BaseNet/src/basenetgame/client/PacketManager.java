package basenetgame.client;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import basenetgame.common.ChatPacket;
import basenetgame.common.Packet;
import basenetgame.common.Receiver;

public class PacketManager extends Thread {

	Receiver receiver;
	
	boolean continuar;
	
	private List<PacketListener> listeners = new ArrayList<PacketListener>();

    public void addListener(PacketListener toAdd) {
        listeners.add(toAdd);
    }
    
	// Genera eventos cuando llegan paquetes
	public PacketManager(Receiver rc){
		
		Logger logger = Logger.getLogger("ClientLog");  
		logger.info("Se inicia el PacketManager.");		

		receiver=rc;
		
		continuar=true;
	}
	
	public void finish(){
		continuar=false;
	}
	
	public void run(){

		Logger logger = Logger.getLogger("ClientLog");  
		
		try {
			
			while(continuar){
				// Obtener paquetes del receiver y despachar
				Packet p=receiver.receivePacket();
						
				while(p!=null){
					
					logger.info("Paquete recibido.");

					//TODO: deserializar el paquete invocando al método que corresponda al tipo de paquete
					// Si el paquete es de tipo CHAT agregamos el Mensaje al GameModel
					if(p.getType()==Packet.Tipo.CHAT){
						
						ChatPacket cp= new ChatPacket();

						logger.info("Contenido del paquete: "+ p.getContenido());
						
						cp.deserializar(p.getContenido());

						p=(ChatPacket)cp;
					}

					// TODO: Completar con otros tipos de paquetes					
					if(p.getType()==Packet.Tipo.GAME_MOVE){

					}

					
					// Notify everybody that may be interested.
			        for (PacketListener hl : listeners)
			            hl.OnPacketReceived(p);
			        
			        p=receiver.receivePacket();
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
