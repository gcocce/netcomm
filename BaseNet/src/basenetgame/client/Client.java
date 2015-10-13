package basenetgame.client;

public class Client{
	
	public static void main(String[] args) {
		
		// Creamos el Modelo
		GameModel gameModel= new GameModel();
		
		// Creamos el GameView pasándole el Modelo como parámetro
		GameView gameView= new GameView(gameModel);
		
		// Creamos el GameController pasándole el Modelo y la Vista como parámetros
		GameController gamec=new GameController(gameModel, gameView);
			
		// Iniciamos el hilo del GameController
		gamec.start();
	}
}