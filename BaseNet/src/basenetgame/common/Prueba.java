package basenetgame.common;

public class Prueba {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		RequestConnectionPacket rcp=new RequestConnectionPacket("Pepe");
		
		System.out.println(rcp.toString());
		
		AcceptConnectionPacket acp=new AcceptConnectionPacket();
		
		System.out.println(acp.toString());
	}

}
