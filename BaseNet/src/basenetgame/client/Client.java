package basenetgame.client;

public class Client{
	
	public static void main(String[] args) {
		
		GameModel gameModel= new GameModel();
		
		GameView gameView= new GameView(gameModel);
		
		// GC: �Pasarle gamev como parametro al GameController?
		GameController gamec=new GameController(gameModel);
		
		// GC: Tambi�n podr�a haber un m�todo addView en el GameController
		// Si es que el controlador necesita la vista...		
		gamec.addView(gameView);
		
		gamec.start();
	}
}