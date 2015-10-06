package basenetgame.client;

import basenetgame.common.ChatPacket;
import basenetgame.common.Packet;

//GC: ¿Por qué tiene que ser un Thread el GameController?
public class GameController extends Thread implements PacketListener, ChatCreatedListener{

	private GameModel gameModel;
	private GameView gameView;
	private CommModule comModule;
	private boolean continuar;
	
	public GameController(GameModel gm){
		
		this.gameModel=gm;
		
		//Creamos el Módulo de Comunicaciones
		comModule = new CommModule(this);
		
		continuar=true;
	}
	
	public void addView(GameView gv){
		gameView=gv;
		
		gameView.addListener(this);
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

	@Override
	public void OnPacketReceived(Packet p) {
		
		// Debug
		System.out.println("GameController llego paquete: " + p.toString());
		
		if(p.getType()==Packet.Tipo.CHAT){
			ChatPacket cp=(ChatPacket)p;
			Message m=new Message(cp.getUser(), cp.getMessage());
			gameModel.addMessage(m);
		}
		
		if(p.getType()==Packet.Tipo.GAME_MOVE){

		}		
		
	}

	@Override
	public void OnChatMessageCreated(Message m) {
		comModule.enviarMensajeChat(m);
	}
}
