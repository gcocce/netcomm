package basenetgame.common;

public class RequestConnectionPacket extends Packet{

	public RequestConnectionPacket(String nick){
		super(Tipo.SOLICITA_CONEXION);
		
		this.setContenido(nick);
	}
}
