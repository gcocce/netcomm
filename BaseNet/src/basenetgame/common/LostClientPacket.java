package basenetgame.common;

public class LostClientPacket extends Packet {

	final private String SEP = "-%%-";
	
	private String user;
	
	public LostClientPacket (){
		super(Tipo.LOST_CLIENT);
	}
	
	public void setUser(String user){
		this.user=user;
		super.setContenido(this.serializar());
	}
	
	public String getUser(){
		return user;
	}
	
	public void deserializar(String contenido){
			
		this.user=contenido;
		
		super.setContenido(contenido);
	}
	
	public String serializar(){
		return user;
	}
}