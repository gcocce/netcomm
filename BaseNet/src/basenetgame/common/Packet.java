package basenetgame.common;

public class Packet {

	public enum Tipo{SOLICITA_CONEXION, ACEPTA_CONEXION, RECHAZA_CONEXION, CHAT, GAME_MOVE};
	
	public int SIZE_HEADER=10;

	Tipo tipo;

	String contenido;
	int longitud;
	
	public Packet(Tipo tipo){
		this.tipo=tipo;
		longitud=0;
		contenido="";
	}
	
	public void setContenido(String cont){
		contenido=cont;
		longitud=contenido.length();
	}
	
	public String toString(){
		String strp="";
		
		int tp=tipo.ordinal();
		
		String formatted_header = String.format("%0"+SIZE_HEADER+"d", tp);
		
		strp=formatted_header;
		
		strp+=contenido;
		
		return strp;
	}
}
