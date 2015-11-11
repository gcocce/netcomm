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
	
	public enum Status{DEFAULT, CONNECTION_ERROR, CONNECTED, BROKEN};
	
	String error;
	Status status;
	
	// Agregar variables para controlar la comunicaci�n
	// Ejemplos: 
	// claves para encriptar la comunicaci�n
	// numeraci�n de los paquetes (si se quiere controlar una secuencia)
	
	public Protocol(){
		error="";
		status = Protocol.Status.DEFAULT;
	}
	
	public String getError(){
		return error;
	}
	
	public Status getStatus(){
		return status;
	}
	
	// Devuelve true si se cumple el protocolo y la conexi�n es aceptada por el servidor
	// Devuelve false si no se cumple le protocolo o el servidor no acepta la conexi�n,
	// En todos los casos casos se guarda el mensaje de error en el atributo error
	public boolean SolicitarConexion(Socket s){
		
		// Creamos un paquete de tipo RequestConnection
		RequestConnectionPacket rcp=new RequestConnectionPacket();
		
		if (s==null){
			error="El socket es nulo";
			status=Protocol.Status.CONNECTION_ERROR;
			return false;
		}
		
		if(!EnviarPaquete(s, rcp)){
			
			error="Se produjo un error al enviar el paquete de Solicitud de la Conexi�n.";
			status=Protocol.Status.CONNECTION_ERROR;
			return false;
		} 

		// Se espera recibir un paquete de tipo AcceptConnection o RejectConnection
		Packet p=RecibirPaquete(s);
		//System.out.println(p.toString());

		// Comprobamos si el cliente env�a el paquete esperado
		if (p.tipo==Packet.Tipo.ACEPTA_CONEXION){
			
			//System.out.println("Se recibe un paquete de tipo ACEPTA CONEXION");
			status=Protocol.Status.CONNECTED;
			return true;
		}
			
		// Comprobamos si el cliente env�a el paquete esperado
		if (p.tipo==Packet.Tipo.RECHAZA_CONEXION){
			
			RejectConnectionPacket rjcp= new RejectConnectionPacket();

			rjcp.deserializar(p.getContenido());

			//p=(RejectConnectionPacket)rjcp;

			error=rjcp.getMotivo();
			status=Protocol.Status.CONNECTION_ERROR;
			return false;
		}

		error="El Servidor no responde de acuerdo al protocolo";
		status=Protocol.Status.CONNECTION_ERROR;
		
		return false;
	}
	
	// Devuelve true si se cumple con el protocolo, false en otro caso
	public boolean AceptarConexion(Socket s){
		
		// Se espera recibir un paquete RequestConnection
		Packet p=RecibirPaquete(s);
		//System.out.println(p.toString());

		// Comprobamos si el cliente env�a el paquete esperado
		if (p.tipo!=Packet.Tipo.SOLICITA_CONEXION){
			
			// Enviamos el paquete de RECHAZO de la conexi�n			
			RejectConnectionPacket rp= new RejectConnectionPacket("No cumple con el protocolo!");
			EnviarPaquete(s, rp);
			
			error="El paquete recibido no es es del tipo SOLICITA CONEXION";
			status=Protocol.Status.CONNECTION_ERROR;
			
			return false;
		}else{
			//System.out.println("Se recibe un paquete de tipo SOLICITA CONEXION");
			
			// Enviamos el paquete de ACEPTACI�N de la conexi�n
			AcceptConnectionPacket acp=new AcceptConnectionPacket();
			
			if (EnviarPaquete(s, acp)){
				status=Protocol.Status.CONNECTED;
				return true;
			}else{
				error="Se produjo un error inesperado al Aceptar la Conexi�n";
				status=Protocol.Status.CONNECTION_ERROR;
				return false;
			}	
		}
	}
	
	public void RechazarConexion(Socket s, String motivo){
		
		// Se espera recibir un paquete RequestConnection
		Packet p=RecibirPaquete(s);
		
		//System.out.println(p.toString());

		// Comprobamos si el cliente env�a el paquete esperado
		if (p.tipo!=Packet.Tipo.SOLICITA_CONEXION){

			error="El paquete no es es del tipo SOLICITA CONEXION";
			status=Protocol.Status.CONNECTION_ERROR;
			
			// Enviamos el paquete de RECHAZO de la conexi�n			
			RejectConnectionPacket rp= new RejectConnectionPacket("No cumple con el protocolo!");
			EnviarPaquete(s, rp);
			
		}else{
			
			error=motivo;
			
			// Se env�a un paquete de tipo RejectConnection
			RejectConnectionPacket rp= new RejectConnectionPacket(motivo);
			EnviarPaquete(s, rp);
		}

	}	

	public boolean EnviarPaquete(Socket s, Packet p){
		
		// En esta secci�n la ejecuci�n se bloquea hasta que el paquete haya sido enviado
		try {
			OutputStream os = s.getOutputStream();
			DataOutputStream outs = new DataOutputStream(os);
			outs.writeUTF(p.toString());
		} catch (IOException e) {
			e.printStackTrace();
			status=Protocol.Status.BROKEN;
			return false;
		} catch (IllegalBlockingModeException e){
			e.printStackTrace();
			status=Protocol.Status.BROKEN;
			return false;
		}
		
		return true;
	}
	
	public Packet RecibirPaquete(Socket s){

		// En esta secci�n la ejecuci�n se bloquea hasta que se haya recibido un paquete
		try {
			InputStream is = s.getInputStream();
	        DataInputStream ins = new DataInputStream(is);
			String packetString=ins.readUTF();

			Packet p=new Packet();
			
			p.deserialize(packetString);

			return p;
		} catch (IOException e) {
			status=Protocol.Status.BROKEN;
			//e.printStackTrace();
			return null;
		}catch (IllegalBlockingModeException e){
			//e.printStackTrace();
			status=Protocol.Status.BROKEN;
			return null;
		}
		
	}	
}
