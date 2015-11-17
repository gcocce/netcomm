package basenetgame.client;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Client{
	
	public static void main(String[] args) {
		
		//***********************************************************************
		// Establecemos un log
		Logger logger = Logger.getLogger("ClientLog");  
	    FileHandler fh;  

	    try {  

	    	// Archivo de salida del log
	        fh = new FileHandler("ClientLog.log");  
	        logger.addHandler(fh);
	        // Formato de las lineas
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);
	        
	        // Para que no escriba en la consola
	        logger.setUseParentHandlers(false);

	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  

	    // Actualmente tenemos harcodeado el Host y el Puerto dentro del GameController
	    logger.info("Se inicia el Cliente");
	    
		// Creamos el Modelo
		GameModel gameModel= new GameModel();
		
		//gameModel.setUserName(args[args.length-1]);
		
		// Creamos el GameView pasándole el Modelo como parámetro
		//GameView gameView= new GameView(gameModel);
		GameView gameView= new GameView(gameModel);
		
		// Creamos el GameController pasándole el Modelo y la Vista como parámetros
		GameController gamec=new GameController(gameModel, gameView);
			
		// Iniciamos el hilo del GameController
		gamec.start();
		
		gameView.start();
	}
}