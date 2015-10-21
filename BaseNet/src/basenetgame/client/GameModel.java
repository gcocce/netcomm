package basenetgame.client;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
	
	private String UserName;

	private ArrayList<Message> chatMessages;
	
	// Lista de observadores del evento OnChatMessageReceived
	private List<ChatMessageReceivedListener> listeners = new ArrayList<ChatMessageReceivedListener>();

    public void addChatMessageReceivedListener(ChatMessageReceivedListener toAdd) {
        listeners.add(toAdd);
    }
	
	public GameModel (){
		
		this.UserName="";
		
		System.out.println("GameModel. Se crea el Modelo.");
		
		chatMessages=new ArrayList<Message>();
	}

	
	public void setUserName(String name){
		this.UserName=name;
	}
	
	public String getUserName(){
		return this.UserName;
	}
	
	public void addMessage(Message m){
		chatMessages.add(m);
		
		// Informar que hay un nuevo mensaje de chat a los observadores
        for (ChatMessageReceivedListener hl : listeners)
            hl.OnChatMessageReceived(m);	
	}
}
