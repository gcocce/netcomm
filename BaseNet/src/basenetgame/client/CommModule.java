package basenetgame.client;

import java.net.Socket;

public class CommModule {
	
	GameController gc;
	int estado;
	Socket s;
	PacketManager pktmgr;
	
	public CommModule(GameController g){
		estado=0;
		this.gc=g;
	}
	
	public boolean IniciarConexion(String host, int puerto){
		
		// Si se conecta correctamente
		pktmgr= new PacketManager();
		pktmgr.addListener(gc);		
		pktmgr.start();

		
		return true;
	}
	
	public int getEstado(){
		return estado;
	}
	
	public void EnviarMensajeChat(String mensaje){
		
		
		
		//Fabricar paquete correspondiente y enviarlo usando Protocol.enviarPaquete(pkt);
	}
	
}
