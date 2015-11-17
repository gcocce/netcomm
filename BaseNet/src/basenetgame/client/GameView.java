package basenetgame.client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class GameView extends Thread implements ChatMessageReceivedListener{

	// Atributos propios de la vista
	private JFrame frame;
	private Chat chatframe=null;
	
	// Atributos del modelo
	private GameModel gameModel;
	private GameController gameController;
	boolean continuar;

	// Lista de observadores del evento OnChatMessageCreated
	private List<ChatMessageCreatedListener> listeners = new ArrayList<ChatMessageCreatedListener>();	
    public void addChatMessageCreatedListener(ChatMessageCreatedListener toAdd) {
        listeners.add(toAdd);
    }
    
    public void setGameController(GameController gcontroller){
    	gameController=gcontroller;
    }
      
    
	public GameView(GameModel gm){
		
		Logger logger = Logger.getLogger("ClientLog");  
		logger.info("Se crea la Vista.");			
		
		this.gameModel=gm;
		
		// Agregamos la vista como observador del modelo en lo que respecta a los mensajes de chat
		gameModel.addChatMessageReceivedListener(this);
	}
	
	public void finish(){
		continuar=false;
	}
	
	public void run(){
		continuar=true;
		
		Logger logger = Logger.getLogger("ClientLog");  
		logger.info("Se inicializa la vista.");	
		
		initialize();
		
		while(continuar){
			try {
				
				
				
				// Sleep de 0 milisegundos para dejar que el sistema operativo
				// de paso a otro proceso o thread en este punto				
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}		
		}
	}	

	/**
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.ORANGE);
		frame.getContentPane().setLayout(new GridLayout(3, 3, 0, 0));
		
		int width=800;
		int height=600;

		int window_width=(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int window_height=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		
		frame.setBounds((int)Math.round(window_width/2-width/2),
				(int)Math.round(window_height/2-height/2), width, height );
		
		//frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnJuego = new JMenu("Juego");
		menuBar.add(mnJuego);
		
		JMenuItem mntmConectar = new JMenuItem("Conectar");
		mntmConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DialogoConectar dialog=new DialogoConectar();
				dialog.setModal(true);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);				
				
				String host=dialog.getHost();
				int port = dialog.getPort();
				
				dialog.dispose();
				
				if(gameController.initComm(host, port)){
					showConnected(true);
				}else{
					showConnected(false);
				}
			}
		});
		mnJuego.add(mntmConectar);
		
		JMenuItem mntmDesconectar = new JMenuItem("Desconectar");
		mntmDesconectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Desconectar del servidor
			}
		});
		mnJuego.add(mntmDesconectar);
		
		JMenuItem mntmSalir = new JMenuItem("Salir");
		mntmSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mnJuego.add(mntmSalir);
		
		JMenu mnOpciones = new JMenu("Opciones");
		menuBar.add(mnOpciones);
		
		JMenuItem mntmConfigurar = new JMenuItem("Configurar");
		mnOpciones.add(mntmConfigurar);
		
		JMenu mnChat = new JMenu("Chat");
		menuBar.add(mnChat);
		
		JMenuItem mntmMostrar = new JMenuItem("Mostrar");
		mntmMostrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Mostrar la ventana de chat
				if (chatframe==null){
					chatframe = new Chat();
					chatframe.setVisible(true);
				}else{
					chatframe.setVisible(true);
				}
			}
		});
		mnChat.add(mntmMostrar);
		
		frame.setVisible(true);

	}	

	@Override
	public void OnChatMessageReceived(Message m) {
		mostrarMensaje(m);
	}
	
	protected void buildChatMessage(String message){
		
		Message msg=new Message(gameModel.getUserName(), message);
		
		// Informar que el usuario creo un mensaje de chat a los observadores
        for (ChatMessageCreatedListener hl : listeners)
            hl.OnChatMessageCreated(msg);
	}
	
	protected void mostrarMensaje(Message m){
		
		if (chatframe!=null){
			chatframe.addMessage("CHAT | " + m.getUser() + " : " + m.getMessage());
			//System.out.println("CHAT | " + m.getUser() + " : " + m.getMessage());
		}else{
			System.out.println("CHAT | " + m.getUser() + " : " + m.getMessage());
		}
	}
	
	public void showConnected(boolean connected){
	
		if (connected){
			JOptionPane.showMessageDialog(null, "Connection Successful",
					"Connection", JOptionPane.INFORMATION_MESSAGE);
		}else{
			JOptionPane.showMessageDialog(null, "Connection Fail",
					"Connection", JOptionPane.ERROR_MESSAGE);
		}
		
		
	}

}
