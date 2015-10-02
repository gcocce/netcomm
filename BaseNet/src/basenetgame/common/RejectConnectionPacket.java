package basenetgame.common;

public class RejectConnectionPacket extends Packet{

	private String motivo;
	
	public RejectConnectionPacket(String motivo){
		super(Tipo.RECHAZA_CONEXION);
		this.motivo=motivo;
	}
	
	public String getMotivo(){
		return motivo;
	}
	
}
