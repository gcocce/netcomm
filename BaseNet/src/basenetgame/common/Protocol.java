package basenetgame.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class Protocol {
	
	String error;
	
	// TODO: Agregar variables para controlar la comunicación
	// Ejemplos: 
	// claves para encriptar la comunicación
	// numeración de los paquetes (si se quiere controlar una secuencia)
	
	public Protocol(){
		error="";
	}
	
	// Devuelve true si la conexión es aceptada por el servidor
	// Devuelve false si el servidor no acepta la conexión, 
	// en este último caso se guarda el mensaje de error en el atributo error
	public boolean SolicitarConexion(Socket s){
		
		RequestConnectionPacket rcp=new RequestConnectionPacket();
		
		if (s==null){
			error="El socket es nulo";
			return false;
		}
		
		try {
			// TODO: Aqui hay que hacer uso de los metodos enviar y recibir paquete
			// Enviamos el paquete
			OutputStream os=s.getOutputStream();
			DataOutputStream outs = new DataOutputStream(os);
			outs.writeUTF(rcp.toString()); 

			// Se espera recibir un paquete
			InputStream is = s.getInputStream();
	        DataInputStream ins = new DataInputStream(is);
	         
	        // Comprobamos el tipo del paquete que se recibe
	        String resp=ins.readUTF();
	        
	        System.out.print(resp);
	        
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean AceptarConexion(Socket s){
		
        try {		
        	// TODO: Aqui hay que hacer uso de los metodos enviar y recibir paquete
			// Se espera recibir un paquete
			InputStream is = s.getInputStream();
	        DataInputStream ins = new DataInputStream(is);
         
	        // Comprobamos el tipo del paquete que se recibe

			String resp=ins.readUTF();
			
			System.out.println(resp);
			
			// TODO: Comprobamos el paquete
			
			// Enviamos el paquete de ACEPTACIÓN O RECHAZO de conexión
			AcceptConnectionPacket acp=new AcceptConnectionPacket();
			OutputStream os=s.getOutputStream();
			DataOutputStream outs = new DataOutputStream(os);
			outs.writeUTF(acp.toString()); 				
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}		
		
		return true;
	}
	
	public boolean RechazarConexion(Socket s, String motivo){
		RejectConnectionPacket rp= new RejectConnectionPacket(motivo);

		try {
			// TODO: Aqui hay que hacer uso de los metodos enviar y recibir paquete
			
			// Se espera recibir un paquete
			InputStream is = s.getInputStream();
	        DataInputStream ins = new DataInputStream(is);
			String resp=ins.readUTF();
			
			System.out.println(resp);
			
			// Enviamos el paquete de rechazo de conexión
			OutputStream os=s.getOutputStream();
			DataOutputStream outs = new DataOutputStream(os);
			outs.writeUTF(rp.toString()); 			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}		
		
		
		return true;
	}	

	public boolean EnviarPaquete(Socket s, Packet p){
		
		
		
		return true;
	}
	
	public Packet RecibirPaquete(Socket s){
	
		
		return null;
	}	
}
