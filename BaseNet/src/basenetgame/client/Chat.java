package basenetgame.client;

import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Chat extends JFrame {

	private JPanel contentPane;
	private JTextArea chatText;
	/**
	 * Create the frame.
	 */
	public Chat() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		int width=300;
		int height=600;

		int window_width=(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int window_height=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		
		setBounds((int)Math.round(window_width/2-width/2),
				(int)Math.round(window_height/2-height/2), width, height );
		
		//setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scrollPane);
		
		chatText = new JTextArea();
		chatText.setLineWrap(true);
		chatText.setRows(29);
		chatText.setColumns(23);
		scrollPane.setViewportView(chatText);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		contentPane.add(scrollPane_1);
		
		JTextArea chatMessage = new JTextArea();
		chatMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				
				// Comprobar si la tecla "ENTER" y enviar el mensaje de chat
				
			}
		});
		chatMessage.setLineWrap(true);
		chatMessage.setColumns(15);
		chatMessage.setRows(4);
		scrollPane_1.setViewportView(chatMessage);
		
		JButton chatButton = new JButton("Enviar");
		chatButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Enviar mensaje de texto
			}
		});
		contentPane.add(chatButton);
	}
	
	public void addMessage(String message){
		chatText.setText(message);
	}

}
