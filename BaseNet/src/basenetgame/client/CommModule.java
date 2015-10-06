package basenetgame.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import basenetgame.common.ChatPacket;
import basenetgame.common.Packet;
import basenetgame.common.Receiver;
import basenetgame.common.Sender;

public class CommModule {
	
	public enum Estado{DEFAULT, CONECTADO, ERROR_CONEXION};
	
	Estado estado;
	Socket socket;
	Sender sender;
	Receiver receiver;
	GameController gc;	
	PacketManager pktmgr;
	
	public CommModule(GameController g){
		estado=Estado.DEFAULT;
		socket=null;
		sender=null;
		receiver=null;
		pktmgr=null;
		this.gc=g;
	}
	
	public boolean iniciarConexion(String host, int puerto){
		
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
			
			receiver=new Receiver(socket);
			receiver.start();
			
			sender=new Sender(socket);
			sender.start();
			
			// TODO: Debería recibir el socket o un receiver
			pktmgr= new PacketManager(receiver);
			pktmgr.addListener(gc);		
			pktmgr.start();
			
			estado=Estado.CONECTADO;
			
			return true;
		}else{
			estado=Estado.ERROR_CONEXION;
			return false;
		}
	}
	
	public void cerrarConexion(){
		
		if (socket!=null){
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Estado getEstado(){
		return estado;
	}
	
	public void enviarMensajeChat(Message m){
		
		ChatPacket cp=new ChatPacket();
		cp.setMessage(m.getMessage());
		
		enviarPacket(cp);
	}
	
	public void enviarPacket(Packet p){
		sender.sendPacket(p);
	}
	
}
