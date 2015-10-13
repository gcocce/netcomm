package basenetgame.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class Server {

	static ServerSocket serversocket=null;
	static GateKeeper gatekeeper=null;
	
	public static void main(String[] args) {
		
		boolean continuar=true;
		int puerto = Integer.parseInt(args[0]);
		
		if (puerto>1000 && puerto<65000){
			
			try {
				serversocket=new ServerSocket(puerto);
				
				gatekeeper=new GateKeeper(serversocket);
				gatekeeper.start();
				
				Scanner teclado=new Scanner(System.in);
				
				// Capturar lo que se ingresa por teclado y finalizar al recibir la palabra "terminar"
				while (continuar){

					String comando = null;
					
					comando=teclado.nextLine();
					
					if (comando.compareTo("salir")==0){
						System.out.println("Intenta salir");
						continuar=false;
						cerrar();
					}
				}
				
				teclado.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("El puerto ingresado está fuera del rango permitido.");
		}
	}
	
	private static void cerrar(){
		// Matar el thread del gatekeeper
		
		gatekeeper.interrupt();
		
		try {
			gatekeeper.join();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
