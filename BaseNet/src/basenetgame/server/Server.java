package basenetgame.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class Server {

	static ServerSocket ss=null;
	static GateKeeper gk=null;
	
	public static void main(String[] args) {
		
		boolean continuar=true;
		int puerto = Integer.parseInt(args[0]);
		
		if (puerto>1000 && puerto<65000){
			
			try {
				ss=new ServerSocket(puerto);
				
				gk=new GateKeeper(ss);
				gk.start();
				

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
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("El puerto ingresado está fuera del rango permitido.");
		}

	}
	
	private static void cerrar(){
		// Matar el thread gk
		
		gk.interrupt();
		
		try {
			gk.join();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
