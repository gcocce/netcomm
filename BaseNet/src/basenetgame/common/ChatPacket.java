package basenetgame.common;

public class ChatPacket extends Packet {

	public ChatPacket (){
		super(Tipo.CHAT);
	}
	
	public void setMessage(String message){
		super.setContenido(message);
	}
	
	public String getMessage(){
		return super.toString();
	}
	
	public String getUser(){
		return "Somebody";
	}
	
	public void deserializar(String contenido){
		super.setContenido(contenido);
	}
}
