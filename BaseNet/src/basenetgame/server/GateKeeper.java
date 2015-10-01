package basenetgame.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GateKeeper extends Thread{

	ServerSocket ss;
	boolean detener;
	
	public GateKeeper(ServerSocket ss){
		this.ss=ss;
		detener=false;
	}
	
	public void run(){
		
		
		while (!detener){
		
			try {
				
				Socket s= ss.accept();
				
				// 
				
				
				
				
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void Detener(){
		detener=true;
	}
	
	
}
