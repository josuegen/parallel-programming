import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JFileChooser;
import java.util.ArrayList;


public class Catalogo{
	private String usuario;
	private String password;
	private String archivo;
	private Connection connect = null;
	Videojuego videojuego = new Videojuego();
	private ArrayList<String> listaVideojuegos = new ArrayList<String>();
	private ArrayList<String> listaAttVideojuego = new ArrayList<String>();
	public Catalogo(){

	}

	public Catalogo(String usuario,String password){
		this.usuario=usuario;
		this.password=password;
	}

    public boolean conectarBDD(){
    	boolean con=true;
        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
            	con=false;
                System.out.println("Error al registrar el driver de MySQL: " + ex);
            }
            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/videojuegos?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",usuario,password);
            System.out.println("Conexión exitosa");

            connect.close();
        }
        catch(java.sql.SQLException ex){
        	con=false;
        	System.out.println(ex);
        }
        return con;
    }
    public boolean ejecutarProcedimiento(int procedimiento,Videojuego vj){
    	boolean add=true;
    	String proc="";
    	CallableStatement cs = null;
    	try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
            	add=false;
                System.out.println("Error al registrar el driver de MySQL: " + ex);
            }
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/videojuegos?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",usuario,password);
		    System.out.println("Conexión exitosa");
            switch(procedimiento){
            	case 1:
			        proc = "{ call addVideojuego(?,?,?,?,?,?,?,?,?,?) }";
			        cs = connect.prepareCall(proc);
			        cs.setInt(1, vj.getSKU());
			        cs.setString(2, vj.getNombre());
			        cs.setInt(3, vj.getFechaLanzamiento());
			        cs.setInt(4, vj.getNJugadores());
			        cs.setString(5, vj.getCategoria());
			        cs.setString(6, vj.getDesarrollador());
			        cs.setInt(7, vj.getOnline()?1:0);
			        cs.setString(8, vj.getPortada());
			        cs.setInt(9, vj.getExistencias());
			        cs.setDouble(10, vj.getPrecio());
			        cs.execute();
			        break;
			    case 2:
			    	proc = "{ call delVideojuego(?) }";
			    	cs = connect.prepareCall(proc);
			        cs.setString(1, vj.getNombre());
			        cs.execute();
			        break;
			    case 3:
			    	proc = "{ call edVideojuego(?,?,?,?,?,?,?,?,?,?) }";
			        cs = connect.prepareCall(proc);
			        cs.setInt(1, vj.getSKU());
			        cs.setString(2, vj.getNombre());
			        cs.setInt(3, vj.getFechaLanzamiento());
			        cs.setInt(4, vj.getNJugadores());
			        cs.setString(5, vj.getCategoria());
			        cs.setString(6, vj.getDesarrollador());
			        cs.setInt(7, vj.getOnline()?1:0);
			        cs.setString(8, vj.getPortada());
			        cs.setInt(9, vj.getExistencias());
			        cs.setDouble(10, vj.getPrecio());
			        cs.execute();
			        break;
            }
            
            connect.close();
        }
        catch(java.sql.SQLException ex){
        	add=false;
        	System.out.println(ex);
        }
        return add;
    }
	
	public void ejecutarProcedimientoSelectNombre(){
		listaVideojuegos.clear();
    	try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error al registrar el driver de MySQL: " + ex);
            }
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/videojuegos?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",usuario,password);
		    System.out.println("Disponibilidad exitosa");
			String proc = "{ call listarVideojuego()}";
			CallableStatement cs = connect.prepareCall(proc);
			ResultSet resultados = cs.executeQuery();
			while(resultados.next()){
				listaVideojuegos.add(resultados.getString("nombre"));
			}
            connect.close();
        }
        catch(java.sql.SQLException ex){
        	System.out.println(ex);
        }
    }

    public void ejecutarProcedimientoConsulta(String nombreVideojuego){
    	listaAttVideojuego.clear();
    	try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error al registrar el driver de MySQL: " + ex);
            }
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/videojuegos?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",usuario,password);
		    System.out.println("Consulta exitosa");
			String proc = "{ call listarAttVideojuegos(?)}";
			CallableStatement cs = connect.prepareCall(proc);
			cs.setString(1, nombreVideojuego);
			ResultSet resultados = cs.executeQuery();
			while(resultados.next()){
				listaAttVideojuego.add(Integer.toString(resultados.getInt("sku")));
				listaAttVideojuego.add(resultados.getString("nombre"));
				listaAttVideojuego.add(Integer.toString(resultados.getInt("fecha_lanzamiento")));
				listaAttVideojuego.add(Integer.toString(resultados.getInt("no_jugadores")));
				listaAttVideojuego.add(resultados.getString("desarrollador"));
				listaAttVideojuego.add(Integer.toString(resultados.getInt("existencias")));
				listaAttVideojuego.add(Integer.toString(resultados.getInt("precio")));
			}
            connect.close();
        }
        catch(java.sql.SQLException ex){
        	System.out.println(ex);
        }
    }

	public void addProducto(){
		String categoria;
		//elementos del frame
		//labels
	    JFrame frame = new JFrame("Añadir videojuego");      
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    JPanel contenedor = new JPanel();
	    contenedor.setLayout(new BoxLayout(contenedor,BoxLayout.Y_AXIS));
	    frame.setLayout(new BorderLayout());
	    JLabel lblSKU = new JLabel("SKU",SwingConstants.CENTER);
	    JLabel lblNombre = new JLabel("Nombre",SwingConstants.CENTER);
	    JLabel lblFechaL = new JLabel("Año de lanzamiento (AAAA)",SwingConstants.CENTER);
	    JLabel lblNJugadores = new JLabel("Numero de jugadores",SwingConstants.CENTER);
	    JLabel lblCategoria = new JLabel("Categoría",SwingConstants.CENTER);
	    JLabel lblDesarrollador = new JLabel("Desarrolladora",SwingConstants.CENTER);
	    JLabel lblOnline = new JLabel("Juego online",SwingConstants.CENTER);
	    JLabel lblPortada = new JLabel("Seleccione imagen de portada",SwingConstants.CENTER);
	    JLabel lblExistencias = new JLabel("Existencias",SwingConstants.CENTER);
	    JLabel lblPrecio = new JLabel("Precio de venta ($)",SwingConstants.CENTER);
	    

	    //utils
    	JTextField txtSKU = new JTextField(20);
    	JTextField txtNombre = new JTextField(20);
    	JTextField txtFecha = new JTextField(8);
    	JTextField txtJugadores = new JTextField(2);
    	String[] categorias = { "Aventura", "Shooter", "Deportes", "Simulación", "Rol", "Estrategia" };
		JComboBox listaCategorias = new JComboBox(categorias);
		listaCategorias.setSelectedIndex(0);
		JTextField txtDesarrolladora = new JTextField(20);
		JCheckBox cbOnline =new JCheckBox("Permite juego online");
        cbOnline.setBounds(10,10,150,30);
		JButton btnPortada = new JButton();
		btnPortada.setText("Examinar..");
		btnPortada.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			JFileChooser jfPortada = new JFileChooser();
				int r = jfPortada.showOpenDialog(null);
    			if (r==JFileChooser.APPROVE_OPTION){
					File f = jfPortada.getSelectedFile(); //Manejador
					archivo = f.getAbsolutePath(); //Dirección
				}
		
    		}
		});
		
		JTextField txtExistencias = new JTextField(3);
		JTextField txtPrecio = new JTextField(4);

		JButton btnAceptar = new JButton();
		btnAceptar.setText("Aceptar");
		btnAceptar.setBackground(Color.GREEN);
		btnAceptar.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			videojuego = new Videojuego(Integer.parseInt(txtSKU.getText()),txtNombre.getText(),Integer.parseInt(txtFecha.getText()),
    				Integer.parseInt(txtJugadores.getText()),(String)listaCategorias.getSelectedItem(),txtDesarrolladora.getText(),
    				(cbOnline.isSelected()?true:false),archivo,Integer.parseInt(txtExistencias.getText()),Double.parseDouble(txtPrecio.getText()));
    			if(ejecutarProcedimiento(1,videojuego))
    				JOptionPane.showMessageDialog(null, "Producto añadido con éxito");
    			else
    				JOptionPane.showMessageDialog(null, "No se pudo añadir el producto");
    			frame.setVisible(false);
				frame.dispose();
    		}
		});

		//añadiendo los elementos al flujo del frame
	    contenedor.add(lblSKU);
	    contenedor.add(txtSKU);
	    contenedor.add(lblNombre);
	    contenedor.add(txtNombre);
	    contenedor.add(lblFechaL);
	    contenedor.add(txtFecha);
	    contenedor.add(lblNJugadores);
	    contenedor.add(txtJugadores);
	    contenedor.add(lblCategoria);
	    contenedor.add(listaCategorias);
	    contenedor.add(lblDesarrollador);
	    contenedor.add(txtDesarrolladora);
	    contenedor.add(lblOnline);
	    contenedor.add(cbOnline);
	    contenedor.add(lblPortada);
	    contenedor.add(btnPortada);
	    contenedor.add(lblExistencias);
	    contenedor.add(txtExistencias);
	    contenedor.add(lblPrecio);
	    contenedor.add(txtPrecio);
	    contenedor.add(btnAceptar);
	    frame.add(contenedor,BorderLayout.CENTER);
	    frame.setSize(300, 420);
	    frame.setVisible(true);
	}

	public void delProducto(){
		ejecutarProcedimientoSelectNombre();
		//labels
	    JFrame frame = new JFrame("Eliminar videojuego");      
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLayout(new FlowLayout(FlowLayout.CENTER));
	    JLabel lblNombre = new JLabel("Nombre",SwingConstants.CENTER);

	    //utils
	    JComboBox cbVideojuegos= new JComboBox();
		for(int i=0;i<listaVideojuegos.size();i++){
			cbVideojuegos.addItem(listaVideojuegos.get(i));
		}
		cbVideojuegos.setSelectedIndex(0);
		JButton btnEliminar= new JButton();
		btnEliminar.setText("Eliminar");
		btnEliminar.setBackground(Color.BLUE);
    	btnEliminar.setForeground(Color.WHITE);
		btnEliminar.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			videojuego = new Videojuego((String)cbVideojuegos.getSelectedItem());
    			if(ejecutarProcedimiento(2,videojuego)){
    				JOptionPane.showMessageDialog(null, "Producto eliminado con éxito");
    				videojuego = new Videojuego();
    				listaVideojuegos.clear();
    			}
    			else
    				JOptionPane.showMessageDialog(null, "No se pudo eliminar el producto");
    				frame.setVisible(false);
					frame.dispose();
					listaVideojuegos.clear();

    		}
		});

		//añadiendo los elementos al flujo del frame
	    frame.add(lblNombre);
	    frame.add(cbVideojuegos);
	    frame.add(btnEliminar);
	    frame.setSize(200, 150);
	    frame.setVisible(true);
	}

	public void editProducto(){
		ejecutarProcedimientoSelectNombre();
		//labels
	    JFrame frame = new JFrame("Modificar videojuego");      
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    JPanel contenedor = new JPanel();
	    contenedor.setLayout(new BoxLayout(contenedor,BoxLayout.Y_AXIS));
	    frame.setLayout(new BorderLayout());
	    JLabel lblSeleccion = new JLabel("Seleccione un videojuego",SwingConstants.CENTER);

	    //utils
	    JComboBox cbVideojuegos= new JComboBox();
		for(int i=0;i<listaVideojuegos.size();i++){
			cbVideojuegos.addItem(listaVideojuegos.get(i));
		}
		cbVideojuegos.setSelectedIndex(0);

		//campos a rellenar
    	JTextField txtSKU = new JTextField(20);
    	txtSKU.setEditable(false);
    	JTextField txtNombre = new JTextField(20);
    	JTextField txtFecha = new JTextField(8);
    	JTextField txtJugadores = new JTextField(2);
    	String[] categorias = { "Aventura", "Shooter", "Deportes", "Simulación", "Rol", "Estrategia" };
		JComboBox listaCategorias = new JComboBox(categorias);
		listaCategorias.setSelectedIndex(0);
		JTextField txtDesarrolladora = new JTextField(20);
		JCheckBox cbOnline =new JCheckBox("Permite juego online");
        cbOnline.setBounds(10,10,150,30);
		JButton btnPortada = new JButton();
		btnPortada.setText("Examinar..");
		btnPortada.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			JFileChooser jfPortada = new JFileChooser();
				int r = jfPortada.showOpenDialog(null);
    			if (r==JFileChooser.APPROVE_OPTION){
					File f = jfPortada.getSelectedFile(); //Manejador
					archivo = f.getAbsolutePath(); //Dirección
				}
		
    		}
		});
		JTextField txtExistencias = new JTextField(3);
		JTextField txtPrecio = new JTextField(4);
		//fin de campos para rellenar

		cbVideojuegos.addActionListener(new ActionListener() {
   			@Override
   			public void actionPerformed(ActionEvent e) {
      			ejecutarProcedimientoConsulta((String)cbVideojuegos.getSelectedItem());
      			//rellenando los campos
				txtSKU.setText(listaAttVideojuego.get(0));
				txtNombre.setText(listaAttVideojuego.get(1));
				txtFecha.setText(listaAttVideojuego.get(2));
				txtJugadores.setText(listaAttVideojuego.get(3));
				txtDesarrolladora.setText(listaAttVideojuego.get(4));
				txtExistencias.setText(listaAttVideojuego.get(5));
				txtPrecio.setText(listaAttVideojuego.get(6));
   			}
		});
		
		JLabel lblSKU = new JLabel("SKU",SwingConstants.CENTER);
	    JLabel lblNombre = new JLabel("Nombre",SwingConstants.CENTER);
	    JLabel lblFechaL = new JLabel("Año de lanzamiento (AAAA)",SwingConstants.CENTER);
	    JLabel lblNJugadores = new JLabel("Numero de jugadores",SwingConstants.CENTER);
	    JLabel lblCategoria = new JLabel("Categoría",SwingConstants.CENTER);
	    JLabel lblDesarrollador = new JLabel("Desarrolladora",SwingConstants.CENTER);
	    JLabel lblOnline = new JLabel("Juego online",SwingConstants.CENTER);
	    JLabel lblPortada = new JLabel("Seleccione imagen de portada",SwingConstants.CENTER);
	    JLabel lblExistencias = new JLabel("Existencias",SwingConstants.CENTER);
	    JLabel lblPrecio = new JLabel("Precio de venta ($)",SwingConstants.CENTER);

		JButton btnActualizar= new JButton();
		btnActualizar.setText("Modificar");
		btnActualizar.setBackground(Color.BLUE);
    	btnActualizar.setForeground(Color.WHITE);
		btnActualizar.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			videojuego = new Videojuego(Integer.parseInt(txtSKU.getText()),txtNombre.getText(),Integer.parseInt(txtFecha.getText()),
    				Integer.parseInt(txtJugadores.getText()),(String)listaCategorias.getSelectedItem(),txtDesarrolladora.getText(),
    				(cbOnline.isSelected()?true:false),archivo,Integer.parseInt(txtExistencias.getText()),Double.parseDouble(txtPrecio.getText()));
    				System.out.println(archivo);
    			if(ejecutarProcedimiento(3,videojuego)){
    				JOptionPane.showMessageDialog(null, "Producto modificado con éxito");
    				videojuego = new Videojuego();
    				listaVideojuegos.clear();
    			}
    			else
    				JOptionPane.showMessageDialog(null, "No se pudo modificar el producto");
    				frame.setVisible(false);
					frame.dispose();
					listaVideojuegos.clear();
    		}
		});

		//añadiendo los elementos al flujo del frame
	    contenedor.add(lblSeleccion);
	    contenedor.add(cbVideojuegos);
	    contenedor.add(lblSKU);
	    contenedor.add(txtSKU);
	    contenedor.add(lblNombre);
	    contenedor.add(txtNombre);
	    contenedor.add(lblFechaL);
	    contenedor.add(txtFecha);
	    contenedor.add(lblNJugadores);
	    contenedor.add(txtJugadores);
	    contenedor.add(lblCategoria);
	    contenedor.add(listaCategorias);
	    contenedor.add(lblDesarrollador);
	    contenedor.add(txtDesarrolladora);
	    contenedor.add(lblOnline);
	    contenedor.add(cbOnline);
	    contenedor.add(lblPortada);
	    contenedor.add(btnPortada);
	    contenedor.add(lblExistencias);
	    contenedor.add(txtExistencias);
	    contenedor.add(lblPrecio);
	    contenedor.add(txtPrecio);
	    contenedor.add(btnActualizar);
	    frame.add(contenedor,BorderLayout.CENTER);
	    frame.setSize(300,450);
	    frame.setVisible(true);
	}

	public ArrayList<Videojuego> obtenerVideojuegos(String usuario,String password){
		ArrayList<Videojuego> videojuegos = new ArrayList<Videojuego>();
		videojuegos.clear();
        Videojuego v = new Videojuego();
        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error al registrar el driver de MySQL: " + ex);
            }
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/videojuegos?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",usuario,password);
            System.out.println("Consulta exitosa");
            String proc = "{call crearListaVideojuegos()}";
            CallableStatement cs = connect.prepareCall(proc);
            ResultSet resultados = cs.executeQuery();
            while(resultados.next()){
                v = new Videojuego();
                v.setSKU(resultados.getInt("sku"));
                v.setNombre(resultados.getString("nombre"));
                v.setFechaLanzamiento(resultados.getInt("fecha_lanzamiento"));
                v.setNJugadores(resultados.getInt("no_jugadores"));
                v.setCategoria(resultados.getString("categoria"));
                v.setDesarrollador(resultados.getString("desarrollador"));
                v.setOnline(resultados.getInt("enlinea")==1?true:false);
                v.setPortada(resultados.getString("portada"));
                v.setExistencias(resultados.getInt("existencias"));
                v.setPrecio(resultados.getDouble("precio"));
                videojuegos.add(v);
            }
            connect.close();
        }
        catch(java.sql.SQLException ex){
            System.out.println(ex);
        }
        return videojuegos;
    }

    public boolean generarCompra(Compra compra){
    	boolean ok=true;
    	try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error al registrar el driver de MySQL: " + ex);
                ok=false;
            }
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/videojuegos?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",usuario,password);
            System.out.println("Consulta exitosa");
            String proc = "{call insertarVenta(?)}";
            CallableStatement cs = connect.prepareCall(proc);;
            cs.setDouble(1, compra.getTotal());
            cs.execute();
            for(int i=0;i<compra.getCompra().size();i++){
            	proc = "{call insertarDetalleventa(?,?)}";
            	cs = connect.prepareCall(proc);
            	cs.setInt(1, Integer.parseInt(compra.getCompra().get(i)[0]));
				cs.setInt(2, Integer.parseInt(compra.getCompra().get(i)[1]));
				cs.execute();
            }
            connect.close();
        }
        catch(java.sql.SQLException ex){
            System.out.println(ex);
            ok=false;
        }
        return ok;
    }

}