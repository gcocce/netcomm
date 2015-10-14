package basenetgame.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

/* Para ejecutar desde la consola:
 * 
 * Ir al directorio bin
 * 
 * Y usar el siguiente comando:
 * 
 * java basenetgame.server.Server Puerto  
 * 
 * (Donde Puerto es un número mayor a 1000 y menor a 65000) 
 * */
public class Server {

	static ServerSocket serversocket=null;
	
	static GateKeeper gatekeeper=null;
	
	static GameHandler gameHandler=null;
	
	public static void main(String[] args) {
		
		boolean continuar=true;
		int puerto = Integer.parseInt(args[0]);
		
		System.out.println("Se inicia el Servidor con puerto: " + puerto);
		
		if (puerto>1000 && puerto<65000){
			
			try {
				serversocket=new ServerSocket(puerto);
				
				if (serversocket!=null){
					System.out.println("ServerSocket creado con puerto: " + puerto);
					
					iniciarServidor(serversocket);
					
					Scanner teclado=new Scanner(System.in);
					
					// Capturar lo que se ingresa por teclado y finalizar al recibir la palabra "terminar"
					while (continuar){

						String comando = null;
						
						comando=teclado.nextLine();
						
						if (comando.compareTo("salir")==0){
							System.out.println("Se inicia la finalización del Servidor...");
							continuar=false;
							cerrarServidor();
						}
					}
					
					teclado.close();
					
				}else{
					System.out.println("Error al crear el socket en el puerto: " + puerto);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("El puerto ingresado está fuera del rango permitido.");
		}
	}
	
	private static void iniciarServidor(ServerSocket serversocket){
		
		gameHandler=new GameHandler();
		
		gameHandler.start();
		
		gatekeeper=new GateKeeper(serversocket, gameHandler);
		
		gatekeeper.start();
	}
	
	private static void cerrarServidor(){
		
		//TODO: Finalizar todos los procesos pendientes (GateKeeper, GameHandlers, Senders, Receivers)
		gameHandler.finish();
		
		if (gatekeeper!=null){
			
			// Matar el thread del gatekeeper
			try {
				gatekeeper.finish();
				
				// Se cierra el ServerSocket para forzar la finalizacion del GateKeeper
				serversocket.close();
				
				// Esperamos que finalice el Thread del GateKeeper
				gatekeeper.join();
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}
		
		System.out.println("Servidor Finalizado.");
	}
}
