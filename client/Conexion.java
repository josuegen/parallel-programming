import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class Conexion{
	private int numeroPuerto;
	private String host;
	private Socket cl;
	private boolean cn=true;
	public Conexion(){

	}

	public Conexion(String host,int numeroPuerto){
		this.numeroPuerto=numeroPuerto;
		this.host=host;
	}

	public int getPuerto() {
	    return numeroPuerto;
	}
	public String getHost(){
		return host;
	}
	public boolean setConexion() {
	    JFrame frame = new JFrame("Establecer Conexión a Servidor");      
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      
	    frame.setLayout(new FlowLayout());
	    //labels
	    JLabel lblPuerto = new JLabel("Número de puerto");
	    lblPuerto.setHorizontalAlignment(SwingConstants.CENTER);
    	lblPuerto.setVerticalAlignment(SwingConstants.CENTER);
    	JLabel lblHost = new JLabel("Dirección del servidor");
    	lblHost.setHorizontalAlignment(SwingConstants.CENTER);
    	lblHost.setVerticalAlignment(SwingConstants.CENTER);


    	//text field
    	JTextField txtHost = new JTextField(15);
    	JTextField txtPuerto = new JTextField(15);
		txtPuerto.addKeyListener(new KeyAdapter(){
			   public void keyTyped(KeyEvent e)
			   {
			      char caracter = e.getKeyChar();

			      // Verificar si la tecla pulsada no es un digito
			      if(((caracter < '0') ||
			         (caracter > '9')) &&
			         (caracter != '\b' /*corresponde a BACK_SPACE*/))
			      {
			         e.consume();  // ignorar el evento de teclado
			      }
			   }
			});
		JButton btnAceptar = new JButton();
		btnAceptar.setText("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			btnAceptar.setMnemonic(KeyEvent.VK_ENTER);
    			numeroPuerto=Integer.parseInt(txtPuerto.getText());
    			host=txtHost.getText();
    			try{
    				cl = new Socket(host, numeroPuerto);
    				cn=true;
    				cl.close();
    			}
    			catch(Exception ex){
    				cn=false;
    				System.out.println(ex);
    			}
    			frame.setVisible(false);
				frame.dispose();
    		}
		});
		frame.add(lblHost);
	    frame.add(txtHost);
	    frame.add(lblPuerto);
	    frame.add(txtPuerto);
	    frame.add(btnAceptar);
	    frame.setSize(250, 150);
	    frame.setVisible(true);
	    return cn;
	}
}