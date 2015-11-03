package basenetgame.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import basenetgame.common.ChatPacket;
import basenetgame.common.Packet;
import basenetgame.common.Protocol;
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
		
		Logger logger = Logger.getLogger("ClientLog");  
		logger.info("Se inicia la conexion con Server: " + host + " Puerto: " + puerto);		
		
		try {
			// Se intenta crear el socket para establecer la conexión
			socket = new Socket(host, puerto);

		} catch (UnknownHostException e) {
			
			logger.severe("CommModule. Servidor desconocido: " + e.getMessage());		

			
			e.printStackTrace();
		} catch (IOException e) {
			
			logger.severe("CommModule. Error al solicitar la conexion: " + e.getMessage());
			
			e.printStackTrace();
		}
        
		if (socket!=null){

			// Muestra la dirección remota de la conexión con el servidor
			System.out.println("Socket creado: " + socket.getRemoteSocketAddress());
			logger.severe("Socket creado: " + socket.getRemoteSocketAddress());
			
			// Usar el Protocolo para establecer la conexion 
			// y luego crear el sender y el receiver
			
			Protocol prot=new Protocol();
			
			if (prot.SolicitarConexion(socket)){
				
				System.out.println("Conexión aceptada.");	
				
				// Si se conecta correctamente creamos el PacketManager
				
				receiver=new Receiver(socket);
				receiver.start();
				
				sender=new Sender(socket);
				sender.start();
				
				pktmgr= new PacketManager(receiver);
				pktmgr.addPacketListener(gc);	
				pktmgr.addLostConnectionListener(gc);
				pktmgr.start();
				
				estado=Estado.CONECTADO;
				
				return true;
			}else{
				estado=Estado.ERROR_CONEXION;
				
				System.out.println("Conexión no aceptada: " +  prot.getError());	
				
				return false;
			}
		}else{
			estado=Estado.ERROR_CONEXION;
			
			System.out.println("No se pudo crear el Socket. Compruebe el host y el puerto.");	
			
			return false;
		}
	}
	
	public void cerrarConexion(){
		
		if(sender!=null){
			sender.finish();
		}
		
		if(receiver!=null){
			receiver.finish();
		}		
		
		if(pktmgr!=null){
			pktmgr.finish();
		}		
		
		if (socket!=null){
			
			System.out.println("CommModule. Se cierra el Socket.");
			
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			socket=null;
		}
		
		sender=null;
		receiver=null;
		pktmgr=null;		
	}
	
	public Estado getEstado(){
		return estado;
	}
	
	public void enviarMensajeChat(Message m){
		
		//System.out.println("Se envía un mensaje de chat: " + m.getMessage());
		
		ChatPacket cp=new ChatPacket();
		cp.setMessage(m.getMessage());
		cp.setUser(m.getUser());
		
		enviarPacket(cp);
	}
	
	public void enviarPacket(Packet p){
		sender.sendPacket(p);
	}
}
