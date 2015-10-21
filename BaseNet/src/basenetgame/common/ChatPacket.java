package basenetgame.common;

public class ChatPacket extends Packet {

	final private String SEP = "-%%-";
	
	private String user;
	private String message;
	
	public ChatPacket (){
		super(Tipo.CHAT);
	}
	
	public void setMessage(String message){
		this.message=message;
		super.setContenido(this.serializar());
	}
	
	public String getMessage(){
		return message;
	}
	
	public String getUser(){
		return user;
	}
	
	public void setUser(String user){
		this.user=user;
		super.setContenido(this.serializar());
	}
	
	public void deserializar(String contenido){
		
		String[] lista=contenido.split(SEP);
		
		this.user=lista[0];
		this.message=lista[1];
		
		super.setContenido(contenido);
	}
	
	public String serializar(){
		return user + SEP + message;
	}
}
