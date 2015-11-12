package basenetgame.common;

public class Packet {

	public enum Tipo{DEFAULT, SOLICITA_CONEXION, ACEPTA_CONEXION, RECHAZA_CONEXION, CHAT, LOST_CLIENT, START_GAME, GAME_MOVE};
	
	public int SIZE_HEADER=10;

	Tipo tipo;

	String contenido;
	int longitud;
	
	public Packet(){
		this.tipo=Tipo.DEFAULT;
		longitud=0;
		contenido="";
	}
	
	public Packet(Tipo tipo){
		this.tipo=tipo;
		this.longitud=0;
		this.contenido="";
	}
	
	public Tipo getType(){
		return this.tipo;
	}
	
	public void setContenido(String contenido){
		this.contenido=contenido;
		this.longitud=contenido.length();
	}
	
	public String getContenido(){
		return contenido;
	}
	
	public String serialize(){
		String strp="";
		
		int tp=tipo.ordinal();
		
		String formatted_header = String.format("%0"+SIZE_HEADER+"d", tp);

		strp=formatted_header;
		
		strp+=contenido;
		
		return strp;
	}
	
	public String toString(){
		return this.serialize();
	}
	
	public boolean deserialize(String s){

		try{
			String header=s.substring(0, SIZE_HEADER);
			
			if (s.length()> SIZE_HEADER){
				contenido=s.substring(SIZE_HEADER);
				longitud=s.length();
			}
			
			int ordinal=Integer.valueOf(header);
			
			tipo=Tipo.values()[ordinal];			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
