package basenetgame.client;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import basenetgame.common.ChatPacket;
import basenetgame.common.LostClientPacket;
import basenetgame.common.Packet;
import basenetgame.common.Protocol;
import basenetgame.common.Receiver;
import basenetgame.common.StartGamePacket;

public class PacketManager extends Thread {

	Receiver receiver;
	
	boolean continuar;
	
	private List<PacketListener> packetlisteners = new ArrayList<PacketListener>();
    public void addPacketListener(PacketListener toAdd) {
    	packetlisteners.add(toAdd);
    }
    
	private List<LostConnectionListener> lostconnlisteners = new ArrayList<LostConnectionListener>();
    public void addLostConnectionListener(LostConnectionListener toAdd) {
    	lostconnlisteners.add(toAdd);
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

						//logger.info("Contenido del paquete de chat: "+ p.getContenido());
						
						cp.deserializar(p.getContenido());

						p=(ChatPacket)cp;
					}
					
					if(p.getType()==Packet.Tipo.LOST_CLIENT){
						LostClientPacket lcp= new LostClientPacket();

						lcp.deserializar(p.getContenido());

						p=(LostClientPacket)lcp;
					}
					
					if(p.getType()==Packet.Tipo.START_GAME){
						StartGamePacket scp= new StartGamePacket();

						p=(StartGamePacket)scp;
					}					

					// TODO: Completar con otros tipos de paquetes		
					
					if(p.getType()==Packet.Tipo.GAME_MOVE){

					}
					
					// Notify everybody that may be interested.
			        for (PacketListener hl : packetlisteners)
			            hl.OnPacketReceived(p);
			        
			        p=receiver.receivePacket();
				}
				
				if(continuar && receiver.getStatus()==Protocol.Status.BROKEN){
					
					logger.info("PacketManager. Conexión cerrada en el otro extremo.");
					continuar = false;
					
					// Notify everybody that may be interested.
			        for (LostConnectionListener hl : lostconnlisteners)
			            hl.OnLostConnection();					
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
