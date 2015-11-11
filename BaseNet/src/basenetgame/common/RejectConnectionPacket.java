package basenetgame.common;

public class RejectConnectionPacket extends Packet{

	final private String SEP = "-%%-";
	private String motivo;

	public RejectConnectionPacket(){
		super(Tipo.RECHAZA_CONEXION);
		motivo="";
	}
	
	public RejectConnectionPacket(String motivo){
		super(Tipo.RECHAZA_CONEXION);
		this.motivo=motivo;
		super.setContenido(this.serializar());
	}
	
	public String getMotivo(){
		return motivo;
	}
	
	public void deserializar(String contenido){
		this.motivo=contenido;
		super.setContenido(contenido);
	}
	
	public String serializar(){
		return motivo;
	}	
}
