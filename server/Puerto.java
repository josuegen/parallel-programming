import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Puerto{
	private int numeroPuerto;


	public Puerto(){

	}

	public Puerto(int numeroPuerto){
		this.numeroPuerto=numeroPuerto;
	}

	public int getPuerto() {
	    return numeroPuerto;
	}
	public void setPuerto() {
	    JFrame frame = new JFrame("Establecer Puerto");      
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      
	    frame.setLayout(new FlowLayout());
	    JLabel lblPuerto = new JLabel("Número de puerto");
	    lblPuerto.setHorizontalAlignment(SwingConstants.CENTER);
    	lblPuerto.setVerticalAlignment(SwingConstants.CENTER);
    	JTextField txtPuerto = new JTextField(10);
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
    			frame.setVisible(false);
				frame.dispose();
    		}
		});
	    frame.add(lblPuerto);
	    frame.add(txtPuerto);
	    frame.add(btnAceptar);
	    frame.setSize(250, 100);
	    frame.setVisible(true);
	}
}