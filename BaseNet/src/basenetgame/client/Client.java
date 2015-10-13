package basenetgame.client;

public class Client{
	
	public static void main(String[] args) {
		
		// Creamos el Modelo
		GameModel gameModel= new GameModel();
		
		// Creamos el GameView pas�ndole el Modelo como par�metro
		GameView gameView= new GameView(gameModel);
		
		// Creamos el GameController pas�ndole el Modelo y la Vista como par�metros
		GameController gamec=new GameController(gameModel, gameView);
			
		// Iniciamos el hilo del GameController
		gamec.start();
	}
}