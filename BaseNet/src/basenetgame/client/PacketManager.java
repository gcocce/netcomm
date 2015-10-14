package basenetgame.client;

import java.util.ArrayList;
import java.util.List;

import basenetgame.common.Packet;
import basenetgame.common.Packet.Tipo;
import basenetgame.common.Receiver;

public class PacketManager extends Thread {

	Receiver receiver;
	
	private List<PacketListener> listeners = new ArrayList<PacketListener>();

    public void addListener(PacketListener toAdd) {
        listeners.add(toAdd);
    }
    
	// Genera eventos cuando llegan paquetes
	public PacketManager(Receiver rc){
		
		System.out.println("PacketManager. Se inicia el PacketManager.");
		
		receiver=rc;		
	}
	
	public void run(){

		try {
			// Obtener paquetes del receiver y despachar
			Packet p=receiver.receivePacket();
					
			while(p!=null){
				
				// Notify everybody that may be interested.
		        for (PacketListener hl : listeners)
		            hl.OnPacketReceived(p);
		        
		        p=receiver.receivePacket();
			}
			
			// Sleep de 0 milisegundos para dejar que el sistema operativo
			// de paso a otro proceso o thread en este punto			
			Thread.sleep(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
