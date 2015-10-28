package basenetgame.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
		
		if (args.length<1){
			System.out.println("\nFaltan parametros. Se esperaba el número de puerto!");
			return;
		}
		
		int puerto = Integer.parseInt(args[0]);
		
		//***********************************************************************
		// Establecemos un log
		Logger logger = Logger.getLogger("ServerLog");  
	    FileHandler fh;  

	    try {  

	    	// Archivo de salida del log
	        fh = new FileHandler("ServerLog.log");  
	        logger.addHandler(fh);
	        // Formato de las lineas
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);
	        
	        // Para que no escriba en la consola
	        logger.setUseParentHandlers(false);

	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  

	    logger.info("Se inicia el Servidor con puerto: " + puerto);
		
		if (puerto>1000 && puerto<65000){
			
			try {
				serversocket=new ServerSocket(puerto);
				
				if (serversocket!=null){

					System.out.println("Server iniciado con puerto: " + puerto);
					logger.info("ServerSocket creado con puerto: " + puerto);
					
					iniciarServidor(serversocket);
					
					Scanner teclado=new Scanner(System.in);
					
					// Capturar lo que se ingresa por teclado y finalizar al recibir la palabra "terminar"
					while (continuar){

						String comando = null;
						
						comando=teclado.nextLine();
						
						if (comando.compareTo("salir")==0){
							
							System.out.println("Se inicia la finalización del Servidor...");
							logger.info("Se inicia la finalización del Servidor...");
							
							continuar=false;
							cerrarServidor();
						}
					}
					
					teclado.close();
					
				}else{
					logger.severe("Error al crear el socket en el puerto: " + puerto);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("El puerto ingresado está fuera del rango permitido.");
			logger.severe("El puerto ingresado está fuera del rango permitido.");
		}
	}
	
	// En este método se inicializan todos los objetos y threads usados por el servidor 
	// (GateKeeper, GameHandlers)
	private static void iniciarServidor(ServerSocket serversocket){
		
		gameHandler=new GameHandler();
		
		gameHandler.start();
		
		gatekeeper=new GateKeeper(serversocket, gameHandler);
		
		gatekeeper.start();
	}
	
	// En este método se finalizan todos los objetos y threads usados por el servidor
	// (GateKeeper, GameHandlers)
	private static void cerrarServidor(){
		

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
		
		if (gameHandler!=null){
			
			try {
				gameHandler.finish();
				
				gameHandler.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
		
		System.out.println("Servidor Finalizado.");
	}
}
