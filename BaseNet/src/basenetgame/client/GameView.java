package basenetgame.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameView extends Thread implements ChatReceivedListener {

	private GameModel gameModel;
	Scanner scanner;
	
	private List<ChatCreatedListener> listeners = new ArrayList<ChatCreatedListener>();	
	
    public void addListener(ChatCreatedListener toAdd) {
        listeners.add(toAdd);
    }
    
	public GameView(GameModel gm){
		this.gameModel=gm;
		
		scanner = new Scanner(System.in);
		
		// Agregamos la vista como observador del modelo en lo que respecta a los mensajes de chat
		gameModel.addListener(this);
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
		System.out.println("Chat recibido: "+ m.getMessage());
	}
	
	public void buildChatMessage(String s){
		Message msg=new Message("cliente",s);
		
		// Informar que el usuario creo un mensaje de chat a los observadores
        for (ChatCreatedListener hl : listeners)
            hl.OnChatMessageCreated(msg);
	}
}
