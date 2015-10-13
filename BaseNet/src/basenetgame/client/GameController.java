package basenetgame.client;

import basenetgame.common.ChatPacket;
import basenetgame.common.Packet;

// GC: ¿Por qué tiene que ser un Thread el GameController? 
// GC: Porqué el programa principal termina luego de generar los objetos,
// y estos dejarían de existir si al menos uno de ellos no continúa ejecutandose
// con las referencias de los otros para que el Recolector de Basura de Java no los elimine.
public class GameController extends Thread implements PacketListener, ChatMessageCreatedListener{

	private GameModel gameModel;
	private GameView gameView;
	private CommModule comModule;
	private boolean continuar;
	
	public GameController(GameModel gm, GameView gv){
		
		this.gameModel=gm;
		this.gameView=gv;
		
		// Agregamos el Controlador como Observador de la Vista para el evento
		// OnChatCreated
		gameView.addChatMessageCreatedListener(this);
		
		// Creamos el Módulo de Comunicaciones
		comModule = new CommModule(this);
		
		continuar=true;
	}
	
	private boolean initComm(String host, int puerto){
		
		if (comModule.iniciarConexion( host, puerto)){
			return true;
		}else{
			return false;
		}		
	}
	

	public void run(){
		do{
			
			// Iniciamos la conexión con el servidor
			if (!initComm("localhost",5000)){
				System.out.println("Error en la conexión...");
			}
			
			try {			
				// Sleep de 0 milisegundos para dejar que el sistema operativo
				// de paso a otro proceso o thread en este punto
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}while(continuar);
		
		// Cerrar el módulo de comunicaciones y todo lo que haga falta
		comModule.cerrarConexion();
	}
	
	// A este metodo hay que llamar cuando se quiera cerrar el programa cliente
	public void finish(){
		continuar=false;
	}

	// Cuando el Módulo de comunicación recibe un paquete por la red genera este evento.
	// El GameController captura el evento y debe decidir 
	// que hacer aquí según el tipo de paquete.
	@Override
	public void OnPacketReceived(Packet p) {
		
		// Debug
		System.out.println("GameController llego paquete: " + p.toString());
		
		// Si el paquete es de tipo CHAT agregamos el Mensaje al GameModel
		if(p.getType()==Packet.Tipo.CHAT){
			ChatPacket cp=(ChatPacket)p;
			Message m=new Message(cp.getUser(), cp.getMessage());
			gameModel.addMessage(m);
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
}
