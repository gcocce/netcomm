package basenetgame.client;

public class Message {

	String message;
	String user;
	
	public Message(String user, String message){
		this.message=message;
		this.user=user;
	}
	
	public String getMessage(){
		return message;
	}
	
	public String getUser(){
		return user;
	}
	
}
