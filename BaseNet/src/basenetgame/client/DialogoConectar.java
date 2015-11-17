package basenetgame.client;

import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DialogoConectar extends JDialog {
	
	private JTextField txtLocalhost;
	private JTextField textField_port;
	
	private String host;
	private int port;
	
	private boolean cancelar=false;
	
	public boolean isCancelar() {
		return cancelar;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	private DialogoConectar me=this;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		try {
//			DialogoConectar dialog = new DialogoConectar();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public DialogoConectar() {
		int width=450;
		int height=236;

		int window_width=(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int window_height=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		
		setBounds((int)Math.round(window_width/2-width/2),
				(int)Math.round(window_height/2-height/2), width, height );
		
		getContentPane().setLayout(null);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 162, 434, 33);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						host=txtLocalhost.getText();
						port=Integer.valueOf(textField_port.getText());
						me.setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						host="";
						port=0;
						cancelar=true;
						me.setVisible(false);
						//me.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		JLabel lblServerIp = new JLabel("Server IP");
		lblServerIp.setBounds(79, 33, 67, 14);
		getContentPane().add(lblServerIp);
		
		txtLocalhost = new JTextField();
		txtLocalhost.setText("localhost");
		txtLocalhost.setBounds(197, 30, 143, 20);
		getContentPane().add(txtLocalhost);
		txtLocalhost.setColumns(10);
		
		JLabel lblServerPort = new JLabel("Server Port");
		lblServerPort.setBounds(79, 92, 67, 14);
		getContentPane().add(lblServerPort);
		
		textField_port = new JTextField();
		textField_port.setText("5000");
		textField_port.setBounds(197, 89, 86, 20);
		getContentPane().add(textField_port);
		textField_port.setColumns(10);
	}
}
