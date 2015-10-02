package basenetgame.common;

public class RequestConnectionPacket extends Packet{

	public RequestConnectionPacket(){
		super(Tipo.SOLICITA_CONEXION);
	}
}
