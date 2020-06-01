import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.*;
import java.util.Collections;
import java.util.ArrayList;
import java.net.URL;
import java.awt.Dimension;


public class Cliente{   
	private static ArrayList<Videojuego> videojuegos = new ArrayList<Videojuego>();
	private static Icon portada = new ImageIcon();
    private static ObjectInputStream entrada;
	public static void main(String args[]) {
		Carrito carrito1 = new Carrito();
		String[] columnNames = {"SKU","Nombre", "Año lanzamiento","Jugadores","Categoría","Portada","Precio(MXN)","Stock","Añadir al carrito"};
		Conexion host = new Conexion();
        JFrame frame = new JFrame("Cliente");      
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
        frame.setLayout(new BorderLayout());   

        //Creando la barra inferior
        JLabel barraInferior = new JLabel();
        barraInferior.setText("Ingrese los datos de conexión");
        barraInferior.setHorizontalAlignment(JLabel.CENTER);
        barraInferior.setVerticalAlignment(JLabel.CENTER);

        // Creando MenuBar y agregando componentes 
        //CONEXIÓN
        JMenuBar barraSuperior = new JMenuBar();       
        JMenu conexion = new JMenu("Conexión");       
        JMenuItem server = new JMenuItem("Configurar conexión servidor");
        JMenuItem conectar = new JMenuItem("Inicar conexión");
        JMenu carrito = new JMenu("Carrito de compras");
        JMenuItem vCarrito = new JMenuItem("Ver carrito");
        carrito.add(vCarrito);
        conexion.add(server);
        conexion.add(conectar);
        //Añadiendo los menús a la barra superior     
        barraSuperior.add(conexion);       
       	barraSuperior.add(carrito);

       	//modelo que usará la tabla
        DefaultTableModel dtm = new DefaultTableModel(0, 0)
        {
    		public boolean isCellEditable(int row, int column){
      			return false;
    		}
 		};
        dtm.setColumnIdentifiers(columnNames);

        //listeners
        server.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                host.setConexion();
            }
        });

        conectar.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
                videojuegos.clear();
                dtm.setRowCount(0);
        	    try{
        	        Socket cl=new Socket(host.getHost(),host.getPuerto());
        	        entrada = new ObjectInputStream(cl.getInputStream());
                    videojuegos = (ArrayList<Videojuego>) entrada.readObject();
                    cl.close();
        	    }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(null, "No fue posible descargar el catálogo: "+ex);
                }
                for(int i=0;i<videojuegos.size();i++){
                    ImageIcon portadax = new ImageIcon(videojuegos.get(i).getPortada());
                    Image image = portadax.getImage();
                    Image newimg = image.getScaledInstance(80, 90,  java.awt.Image.SCALE_SMOOTH);
                    portada = new ImageIcon(newimg);
                    Object[] newRow = {videojuegos.get(i).getSKU(),videojuegos.get(i).getNombre(),videojuegos.get(i).getFechaLanzamiento(),
                    videojuegos.get(i).getNJugadores(),videojuegos.get(i).getCategoria(),portada,
                    videojuegos.get(i).getPrecio(),videojuegos.get(i).getExistencias(),"+"};
                    dtm.addRow(newRow);
                }
                barraInferior.setText("Conectado a "+host.getHost()+":"+String.valueOf(host.getPuerto()));
                carrito1.setServidor(host.getHost());
                carrito1.setPuerto(host.getPuerto());
        	}
        });

        vCarrito.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                carrito1.verCarrito();
            }
        });


        //Tabla de catálogo y sus eventos

        JTable table = new JTable(){
      			public Class getColumnClass(int column) {
        			return (column == 5) ? Icon.class : Object.class;
     			 }
    		};;
    	table.setRowHeight(100);
        table.setModel(dtm);
       	table.setPreferredScrollableViewportSize(table.getPreferredSize());
       	table.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		        int col = table.columnAtPoint(evt.getPoint());
		        int row = table.rowAtPoint(evt.getPoint());
		        if (col ==8) {
		        	Videojuego vj= new Videojuego();
		        	vj.setSKU((Integer)table.getModel().getValueAt(row,0));
		        	vj.setNombre(String.valueOf(table.getModel().getValueAt(row,1)));
		        	vj.setPortada(String.valueOf(table.getModel().getValueAt(row,5)));
		        	vj.setPrecio((Double)table.getModel().getValueAt(row,6));
		        	carrito1.addProducto(vj);
		        	JOptionPane.showMessageDialog(null,"¡"+vj.getNombre()+" añadido al carrito!");
		        }
		    }
		});
        JScrollPane scrollPane = new JScrollPane(table);
        
        //Añadiendo los elementos al flujo del frame);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
    	frame.getContentPane().add(barraInferior, BorderLayout.PAGE_END);
       	frame.setJMenuBar(barraSuperior);
        frame.setVisible(true);
        frame.setSize(840, 400);
    }
}

   
    
