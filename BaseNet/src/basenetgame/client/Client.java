package basenetgame.client;


public class Client{
	
	public static void main(String[] args) {
		
		GameController gamec=new GameController();
		CommModule comModule = new CommModule(gamec);	
		
		if (comModule.IniciarConexion( "localhost", 5000)){
			System.out.println("Se inicio correctamente la comunicación");
		}else{
			System.out.println("Error al iniciar la comunicación");
		}
	}
	
}
