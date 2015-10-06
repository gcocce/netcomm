package basenetgame.client;

import java.util.ArrayList;
import java.util.List;

public class GameModel {

	private ArrayList<Message> chatMessages;
	
	private List<ChatReceivedListener> listeners = new ArrayList<ChatReceivedListener>();

    public void addListener(ChatReceivedListener toAdd) {
        listeners.add(toAdd);
    }
	
	public GameModel (){
		chatMessages=new ArrayList<Message>();
	}
	
	public void addMessage(Message m){
		chatMessages.add(m);
		
		// Informar que hay un nuevo mensaje de chat a los observadores
        for (ChatReceivedListener hl : listeners)
            hl.OnChatMessageReceived(m);	
        
	}
}
