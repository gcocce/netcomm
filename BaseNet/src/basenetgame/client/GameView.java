package basenetgame.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class GameView extends Thread implements ChatMessageReceivedListener {

	private GameModel gameModel;
	
	Scanner scanner;
	
	// Lista de observadores del evento OnChatMessageCreated
	private List<ChatMessageCreatedListener> listeners = new ArrayList<ChatMessageCreatedListener>();	
	
    public void addChatMessageCreatedListener(ChatMessageCreatedListener toAdd) {
        listeners.add(toAdd);
    }
    
	public GameView(GameModel gm){
		
		Logger logger = Logger.getLogger("ClientLog");  
		logger.info("Se crea la Vista.");			
		
		
		this.gameModel=gm;
		
		scanner = new Scanner(System.in);
		
		// Agregamos la vista como observador del modelo en lo que respecta a los mensajes de chat
		gameModel.addChatMessageReceivedListener(this);
	}
	
	public void run(){
		
		String cadena=null;
		boolean seguir=true;
		
		do{
			cadena = scanner.nextLine();
			
			if(cadena.compareToIgnoreCase("salir")==0){
				seguir=false;
			}else{
				buildChatMessage(cadena);
			}
			
		}while(seguir);
		
	}

	@Override
	public void OnChatMessageReceived(Message m) {
		
		mostrarMensaje(m);
	}
	
	public void buildChatMessage(String message){
		
		Message msg=new Message(gameModel.getUserName(), message);
		
		// Informar que el usuario creo un mensaje de chat a los observadores
        for (ChatMessageCreatedListener hl : listeners)
            hl.OnChatMessageCreated(msg);
        
        mostrarMensaje(msg);
	}
	
	public void mostrarMensaje(Message m){
		
		System.out.println("CHAT | " + m.getUser() + " : " + m.getMessage());
	}
	
}
