package basenetgame.client;

public class Message {

	String message;
	String user;
	
	Message(String user, String message){
		this.message=message;
		this.user=user;
	}
	
	public String getMessage(){
		return message;
	}
	
	public String getUser(){
		return user;
	}
	
	public String toString(){
		
		return user+message;
	}
}
