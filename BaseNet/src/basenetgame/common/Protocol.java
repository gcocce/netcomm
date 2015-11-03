package basenetgame.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.channels.IllegalBlockingModeException;
import java.util.logging.Logger;

public class Protocol {
	
	String error;
	
	// Agregar variables para controlar la comunicación
	// Ejemplos: 
	// claves para encriptar la comunicación
	// numeración de los paquetes (si se quiere controlar una secuencia)
	
	public Protocol(){
		error="";
	}
	
	public String getError(){
		return error;
	}
	
	// Devuelve true si se cumple el protocolo y la conexión es aceptada por el servidor
	// Devuelve false si no se cumple le protocolo o el servidor no acepta la conexión,
	// En todos los casos casos se guarda el mensaje de error en el atributo error
	public boolean SolicitarConexion(Socket s){
		
		// Creamos un paquete de tipo RequestConnection
		RequestConnectionPacket rcp=new RequestConnectionPacket();
		
		if (s==null){
			error="El socket es nulo";
			return false;
		}
		
		if(!EnviarPaquete(s, rcp)){
			
			error="Se produjo un error al enviar el paquete de Solicitud de la Conexión.";
			return false;
		} 

		// Se espera recibir un paquete de tipo AcceptConnection o RejectConnection
		Packet p=RecibirPaquete(s);
		//System.out.println(p.toString());

		// Comprobamos si el cliente envía el paquete esperado
		if (p.tipo==Packet.Tipo.ACEPTA_CONEXION){
			
			//System.out.println("Se recibe un paquete de tipo ACEPTA CONEXION");
			return true;
		}
			
		// Comprobamos si el cliente envía el paquete esperado
		if (p.tipo==Packet.Tipo.RECHAZA_CONEXION){
			
			RejectConnectionPacket rjcp=(RejectConnectionPacket)p;
			
			error=rjcp.getMotivo();
			
			return false;
		}

		error="El Servidor no responde de acuerdo al protocolo";
		
		return false;
	}
	
	// Devuelve true si se cumple con el protocolo, false en otro caso
	public boolean AceptarConexion(Socket s){
		
		// Se espera recibir un paquete RequestConnection
		Packet p=RecibirPaquete(s);
		//System.out.println(p.toString());

		// Comprobamos si el cliente envía el paquete esperado
		if (p.tipo!=Packet.Tipo.SOLICITA_CONEXION){
			
			// Enviamos el paquete de RECHAZO de la conexión			
			RejectConnectionPacket rp= new RejectConnectionPacket("No cumple con el protocolo!");
			EnviarPaquete(s, rp);
			
			error="El paquete recibido no es es del tipo SOLICITA CONEXION";			
			
			return false;
		}else{
			//System.out.println("Se recibe un paquete de tipo SOLICITA CONEXION");
			
			// Enviamos el paquete de ACEPTACIÓN de la conexión
			AcceptConnectionPacket acp=new AcceptConnectionPacket();
			
			if (EnviarPaquete(s, acp)){
				return true;
			}else{
				error="Se produjo un error inesperado al Aceptar la Conexión";
				return false;
			}	
		}
	}
	
	public void RechazarConexion(Socket s, String motivo){
		
		// Se espera recibir un paquete RequestConnection
		Packet p=RecibirPaquete(s);
		System.out.println(p.toString());

		// Comprobamos si el cliente envía el paquete esperado
		if (p.tipo!=Packet.Tipo.SOLICITA_CONEXION){
			error="El paquete no es es del tipo SOLICITA CONEXION";
		}

		// Se envía un paquete de tipo RejectConnection
		RejectConnectionPacket rp= new RejectConnectionPacket(motivo);
		EnviarPaquete(s, rp);
	}	

	public boolean EnviarPaquete(Socket s, Packet p){
		
		// En esta sección la ejecución se bloquea hasta que el paquete haya sido enviado
		try {
			OutputStream os = s.getOutputStream();
			DataOutputStream outs = new DataOutputStream(os);
			outs.writeUTF(p.toString());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalBlockingModeException e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public Packet RecibirPaquete(Socket s){

		// En esta sección la ejecución se bloquea hasta que se haya recibido un paquete
		try {
			InputStream is = s.getInputStream();
	        DataInputStream ins = new DataInputStream(is);
			String packetString=ins.readUTF();

			Packet p=new Packet();
			
			p.deserialize(packetString);

			return p;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}catch (IllegalBlockingModeException e){
			e.printStackTrace();
			return null;
		}
		
	}	
}
