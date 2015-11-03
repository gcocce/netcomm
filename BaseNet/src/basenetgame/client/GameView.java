package basenetgame.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class GameView extends Thread implements ChatMessageReceivedListener {

	private GameModel gameModel;
	
	Scanner scanner;
	
	boolean continuar;
	
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
	
	public void finish(){
		continuar=false;
	}
	
	public void run(){
		
		continuar=true;
		
		while(continuar){
			try {
			
				
				
				// Sleep de 0 milisegundos para dejar que el sistema operativo
				// de paso a otro proceso o thread en este punto				
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}		
		}
		
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
        
	}
	
	public void mostrarMensaje(Message m){
		
		System.out.println("CHAT | " + m.getUser() + " : " + m.getMessage());
	}
	
}
