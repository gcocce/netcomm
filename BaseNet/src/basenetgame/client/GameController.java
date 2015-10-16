package basenetgame.client;

import java.util.Scanner;

import basenetgame.common.ChatPacket;
import basenetgame.common.Packet;

// GC: �Por qu� tiene que ser un Thread el GameController? 
// GC: Porqu� el programa principal termina luego de generar los objetos,
// y estos dejar�an de existir si al menos uno de ellos no contin�a ejecutandose
// con las referencias de los otros para que el Recolector de Basura de Java no los elimine.
public class GameController extends Thread implements PacketListener, ChatMessageCreatedListener{

	private GameModel gameModel;
	private GameView gameView;
	private CommModule comModule;
	private boolean continuar;
	
	public GameController(GameModel gm, GameView gv){
		
		System.out.println("GameController. Se crea el controlador.");
		
		this.gameModel=gm;
		this.gameView=gv;
		
		// Agregamos el Controlador como Observador de la Vista para el evento
		// OnChatCreated
		gameView.addChatMessageCreatedListener(this);
		
		// Creamos el M�dulo de Comunicaciones
		comModule = new CommModule(this);
		
		continuar=true;
	}
	
	// Este m�todo tiene que ser usado por la vista para iniciar la comunicaci�n
	// con el servidor
	public boolean initComm(String host, int puerto){
		
		if (comModule.iniciarConexion( host, puerto)){
			return true;
		}else{
			return false;
		}		
	}
	

	public void run(){
		
		System.out.println("GameController. Se intenta iniciar la conexi�n...");
		
		// Iniciamos la conexi�n con el servidor
		if (!initComm("localhost",5000)){
			continuar=false;
		}
		
		while(continuar){
			try {

				
				Scanner teclado=new Scanner(System.in);
				
				// Capturar lo que se ingresa por teclado y finalizar al recibir la palabra "terminar"
				while (continuar){

					String chatStr = null;
					
					chatStr=teclado.nextLine();

					Message msg=new Message("Cliente", chatStr);
					
					comModule.enviarMensajeChat(msg);
				}				
				
				// Sleep de 0 milisegundos para dejar que el sistema operativo
				// de paso a otro proceso o thread en este punto
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
				
				continuar=false;
			}
		}
		
		// Cerrar el m�dulo de comunicaciones y todo lo que haga falta
		comModule.cerrarConexion();
	}
	
	// A este metodo hay que llamar cuando se quiera cerrar el programa cliente
	public void finish(){
		continuar=false;
	}

	// Cuando el M�dulo de comunicaci�n recibe un paquete por la red genera este evento.
	// El GameController captura el evento y debe decidir 
	// que hacer aqu� seg�n el tipo de paquete.
	@Override
	public void OnPacketReceived(Packet p) {
		
		// Debug
		System.out.println("GameController. Llego paquete: " + p.toString());
		
		// Si el paquete es de tipo CHAT agregamos el Mensaje al GameModel
		if(p.getType()==Packet.Tipo.CHAT){
			
			ChatPacket cp=(ChatPacket)p;
			
			//TODO: deserializar el paquete invocando al m�todo que corresponda al tipo de paquete
			
			Message m=new Message(cp.getUser(), cp.getMessage());
			gameModel.addMessage(m);
		}
		
		
		if(p.getType()==Packet.Tipo.GAME_MOVE){

		}
		
		// TODO: Completar con otros tipos de paquetes
		
		
	}

	// OnChatMessageCreated se ejecuta cuando el usuario genera un nuevo
	// mensaje de chat en el cliente.
	// El controlador captura este evento y pasa el mensaje al Modulo de Comunicaci�n.
	@Override
	public void OnChatMessageCreated(Message m) {
		comModule.enviarMensajeChat(m);
	}
}
