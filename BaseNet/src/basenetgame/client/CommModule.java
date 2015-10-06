package basenetgame.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class CommModule {
	
	public enum Estado{DEFAULT, CONECTADO, ERROR_CONEXION};
	
	Estado estado;
	Socket socket;
	GameController gc;	
	PacketManager pktmgr;
	
	public CommModule(GameController g){
		estado=Estado.DEFAULT;
		socket=null;
		pktmgr=null;
		this.gc=g;
	}
	
	public boolean IniciarConexion(String host, int puerto){
		
		try {
			// Se intenta crear el socket para establecer la conexión
			socket = new Socket(host, puerto);
			
	        // Muestra la dirección remota de la conexión con el servidor
	        System.out.println("Conectado a " + socket.getRemoteSocketAddress());				
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		if (socket!=null){
			// Si se conecta correctamente creamos el PacketManager
			pktmgr= new PacketManager();
			pktmgr.addListener(gc);		
			pktmgr.start();
			
			estado=Estado.CONECTADO;
			
			return true;
		}else{
			estado=Estado.ERROR_CONEXION;
			return false;
		}
	}
	
	public Estado getEstado(){
		return estado;
	}
	
	public void EnviarMensajeChat(String mensaje){
		
		
		
		//Fabricar paquete correspondiente y enviarlo usando Protocol.enviarPaquete(pkt);
	}
	
}
