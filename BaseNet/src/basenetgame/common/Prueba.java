package basenetgame.common;

public class Prueba {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		RequestConnectionPacket rcp=new RequestConnectionPacket();
		
		System.out.println(rcp.toString());
		String rcstr=rcp.toString();
		
		AcceptConnectionPacket acp=new AcceptConnectionPacket();
		
		System.out.println(acp.toString());
		String acpstr=acp.toString();
		
		String header=rcstr.substring(0, 10);
		
		System.out.println(header);
		
		Packet p=new Packet();
		
		p.deserialize(rcstr);
		
		
		if (p.tipo==Packet.Tipo.SOLICITA_CONEXION){
			System.out.println("El paquete es del tipo SOLICITA CONEXION");
		}
		
	}

}
