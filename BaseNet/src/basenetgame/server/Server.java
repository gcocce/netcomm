package basenetgame.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/* Para ejecutar desde la consola:
 * 
 * 1) Ir al directorio bin y usar el siguiente comando:
 * 
 * java basenetgame.server.Server [Puerto]
 * 
 * 2) O bien usar el archivo server.bat que está en el directorio bin
 * 
 * server [Puerto]
 * 
 * (Donde Puerto es un número mayor a 1000 y menor a 65000) 
 * */
public class Server {
	// Server Socket usado para recibir conexiones
	static ServerSocket serversocket=null;
	
	// Portero encargado de escuchar conexiones en el serversocket
	static GateKeeper gatekeeper=null;
	
	// Manejador encargado de gestionar el juego
	static GameHandler gameHandler=null;
	
	public static void main(String[] args) {
		
		boolean continuar=true;
			
		//***********************************************************************
		// Establecemos un log para registrar la actividad del servidor
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
	        return;
	    } catch (IOException e) {  
	        e.printStackTrace();
	        return;
	    }  
	    
		//***********************************************************************
		// Comprobamos los parametros
		if (args.length<1){
			System.out.println("\nFaltan parametros. Se esperaba el número de puerto!");
			logger.info("Faltan parametros. Se esperaba el número de puerto!");
			return;
		}
		
		int puerto = Integer.parseInt(args[0]);	    

		//***********************************************************************
		// Se inicia el servidor	
	    logger.info("Se inicia el Servidor con puerto: " + puerto);
		
	    // Comprobamos el numero del puerto
		if (puerto>1000 && puerto<65000){
			
			try {
				serversocket=new ServerSocket(puerto);
				
				if (serversocket!=null){

					System.out.println("Server iniciado con puerto: " + puerto);
					logger.info("ServerSocket creado con puerto: " + puerto);
					
					iniciarServidor(serversocket);
					
					Scanner teclado=new Scanner(System.in);
					
					// Capturar lo que se ingresa por teclado y finalizar al recibir la palabra "salir"
					while (continuar){

						String comando = null;
						
						comando=teclado.nextLine();
						
						if (comando.compareTo("salir")==0){
							
							System.out.println("Servidor finalizando...");
							logger.info("Servidor finalizando...");
							
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
	
	//***********************************************************************
	// En este método se inicializan todos los objetos y threads usados por el servidor 
	// (GateKeeper, GameHandlers)
	private static void iniciarServidor(ServerSocket serversocket){
		
		gameHandler=new GameHandler();
		
		gameHandler.start();
		
		gatekeeper=new GateKeeper(serversocket, gameHandler);
		
		gatekeeper.start();
	}
	
	//***********************************************************************
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
		Logger logger = Logger.getLogger("ServerLog");  
		logger.info("Servidor Finalizado.");
	}
}
