package basenetgame.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Protocol {
	Socket s;
	String error;
	
	public Protocol(Socket s){
		this.s=s;
		error="";
	}
	
	// Devuelve true si la conexión es aceptada por el servidor
	// Devuelve false si el servidor no acepta la conexión, 
	// en este último caso se guarda el mensaje de error en el atributo error
	public boolean SolicitarConexion(String nick){
		
		RequestConnectionPacket rcp=new RequestConnectionPacket(nick);
		
		if (s==null){
			error="El socket es nulo";
			return false;
		}
		
		try {
			// Aqui hay que hacer uso de los metodos enviar y recibir paquete
			OutputStream os=s.getOutputStream();
			DataOutputStream outs = new DataOutputStream(os);
			outs.writeUTF(rcp.toString()); 

			InputStream is = s.getInputStream();
	        DataInputStream ins = new DataInputStream(is);
	         
	        String resp=ins.readUTF();
	        
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	public boolean AceptarConexion(){
		
		
		
		return true;
	}

	public boolean EnviarPaquete(Packet p){
		
		
		
		return true;
	}
	
	public Packet RecibirPaquete(){
	
		
		return null;
	}	
}
