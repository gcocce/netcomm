package basenetgame.client;

import java.util.ArrayList;
import java.util.List;

import basenetgame.common.Packet;
import basenetgame.common.Packet.Tipo;

public class PacketManager extends Thread {

	private List<PacketListener> listeners = new ArrayList<PacketListener>();

    public void addListener(PacketListener toAdd) {
        listeners.add(toAdd);
    }
    
	// Genera eventos cuando llegan paquetes
	public PacketManager(){
		
		
	}
	
	
	public void run(){

		try {

			
			
			// Se crea el paquete o se obtiene del receiver
			//TODO: REVISAR EL TIPO DE PAQUETE CREADO ACÁ
			Packet p=new Packet(Tipo.ACEPTA_CONEXION);
			
			// Notify everybody that may be interested.
	        for (PacketListener hl : listeners)
	            hl.OnPacketReceived(p);
			
			
			// Sleep de 0 milisegundos para dejar que el sistema operativo
			// de paso a otro proceso o thread en este punto			
			Thread.sleep(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
