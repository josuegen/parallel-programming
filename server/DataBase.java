import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class DataBase{
	private String usuario;
	private String password;


	public DataBase(){

	}

	public DataBase(String usuario,String password){
		this.usuario=usuario;
		this.password=password;
	}

	public String getUsuario() {
	    return usuario;
	}
	public String getPassword() {
	    return password;
	}
	public void setDataBase() {
		//elementos del frame
	    JFrame frame = new JFrame("Conexión a base de datos");      
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      
	    frame.setLayout(new FlowLayout());
	    JLabel lblUsuario = new JLabel("Usuario");
	    JLabel lblPassword = new JLabel("Contraseña");
    	JTextField txtUsuario = new JTextField(20);
    	final JPasswordField txtPassword = new JPasswordField(20);
		JButton btnAceptar = new JButton();
		btnAceptar.setText("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			usuario=txtUsuario.getText();
    			password=new String(txtPassword.getPassword());
    			frame.setVisible(false);
				frame.dispose();
    		}
		});

		//añadiendo los elementos al flujo del frame
	    frame.add(lblUsuario);
	    frame.add(txtUsuario);
	    frame.add(lblPassword);
	    frame.add(txtPassword);
	    frame.add(btnAceptar);
	    frame.setSize(250, 160);
	    frame.setVisible(true);
	}
}