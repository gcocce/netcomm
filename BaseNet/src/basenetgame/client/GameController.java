package basenetgame.client;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Logger;

import basenetgame.common.ChatPacket;
import basenetgame.common.LostClientPacket;
import basenetgame.common.Packet;

// GC: ¿Por qué tiene que ser un Thread el GameController? 
// GC: Porqué el programa principal termina luego de generar los objetos,
// y estos dejarían de existir si al menos uno de ellos no continúa ejecutandose
// con las referencias de los otros para que el Recolector de Basura de Java no los elimine.
public class GameController extends Thread implements PacketListener, LostConnectionListener, ChatMessageCreatedListener{

	private GameModel gameModel;
	private GameView gameView;
	private CommModule comModule;
	private boolean continuar;
	
	Scanner teclado=null;
	
	public GameController(GameModel gm, GameView gv){
		
		this.gameModel=gm;
		this.gameView=gv;
		
		// Agregamos el Controlador como Observador de la Vista para el evento
		// OnChatCreated
		gameView.addChatMessageCreatedListener(this);
		
		// Creamos el Módulo de Comunicaciones
		comModule = new CommModule(this);
		
		teclado=new Scanner(System.in);
		
		continuar=true;
	}
	
	// Este método tiene que ser usado por la vista para iniciar la comunicación
	// con el servidor
	public boolean initComm(String host, int puerto){
		
		if (comModule.iniciarConexion( host, puerto)){
			return true;
		}else{
			return false;
		}		
	}
	
	public void run(){
		
		Logger logger = Logger.getLogger("ClientLog");  
		logger.info("GameController. Se intenta iniciar la conexión...");		
		
		// Iniciamos la conexión con el servidor
		if (!initComm("localhost",5000)){
			continuar=false;
		}
		
		while(continuar){
			try {
							
				// Capturar lo que se ingresa por teclado y finalizar al recibir la palabra "terminar"
				while (continuar){

					String chatStr = null;
					
					chatStr=teclado.nextLine();
					
					
					if (continuar && chatStr!=null){
						if(chatStr.compareToIgnoreCase("salir")==0){
							
							System.out.println("El Usuario finaliza el programa.");
							
							continuar=false;
						}else{
							
							Message msg=new Message(gameModel.getUserName(), chatStr);
							
							logger.info("Se envía un mensaje de chat: "+ msg.getMessage());
							
							comModule.enviarMensajeChat(msg);						
						}						
					}
				}				
				
				// Sleep de 0 milisegundos para dejar que el sistema operativo
				// de paso a otro proceso o thread en este punto
				Thread.sleep(0);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
				continuar=false;
			} catch (NoSuchElementException nsee){
				continuar=false;
			}
		}
		
		// Cerrar el view
		gameView.finish();
		
		// Cerrar el módulo de comunicaciones y todo lo que haga falta
		comModule.cerrarConexion();
	}
	
	// A este metodo hay que llamar cuando se quiera cerrar el programa cliente
	public void finish(){
		continuar=false;
			
		// Cerrar el view
		gameView.finish();
		
		// Cerrar el módulo de comunicaciones y todo lo que haga falta
		comModule.cerrarConexion();
		
		System.exit(0);
	}

	// Cuando el Módulo de comunicación recibe un paquete por la red genera este evento.
	// El GameController captura el evento y debe decidir 
	// que hacer aquí según el tipo de paquete.
	@Override
	public void OnPacketReceived(Packet p) {
		
		// Debug
		Logger logger = Logger.getLogger("ClientLog");  
		logger.info("Llego paquete: " + p.toString());		
		
		
		// Si el paquete es de tipo CHAT agregamos el Mensaje al GameModel
		if(p.getType()==Packet.Tipo.CHAT){
			
			ChatPacket cp=(ChatPacket)p;
			
			Message m=new Message(cp.getUser(), cp.getMessage());
			
			gameModel.addMessage(m);
		}
	
		if(p.getType()==Packet.Tipo.LOST_CLIENT){
			
			LostClientPacket lcp=(LostClientPacket)p;
			
			String userLost= lcp.getUser();
			
			System.out.println("Se desconectó " + userLost);
		}
		
		if(p.getType()==Packet.Tipo.GAME_MOVE){

		}
		
		// TODO: Completar con otros tipos de paquetes
		
	}

	// OnChatMessageCreated se ejecuta cuando el usuario genera un nuevo
	// mensaje de chat en el cliente.
	// El controlador captura este evento y pasa el mensaje al Modulo de Comunicación.
	@Override
	public void OnChatMessageCreated(Message m) {
		comModule.enviarMensajeChat(m);
	}

	@Override
	public void OnLostConnection() {
		this.finish();		
	}
}


