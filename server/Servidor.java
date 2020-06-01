import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.*;
import java.util.ArrayList;


public class Servidor{   
	private static boolean iniciado;
    private static int conexiones;
    private static Catalogo catalogue=new Catalogo();
    private static ArrayList<Videojuego> videojuegos = new ArrayList<Videojuego>();
    public static void main(String args[]) {
    	//Variables y objetos
    	Puerto port = new Puerto(); 
    	DataBase database = new DataBase();

        JFrame frame = new JFrame("Servidor");      
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
        frame.setLayout(new BorderLayout());
        frame.setSize(400, 300);

        // Creando MenuBar y agregando componentes 
        //CONEXIÓN
        JMenuBar barraSuperior = new JMenuBar();       
        JMenu conexion = new JMenu("Conexión");       
        JMenuItem puerto = new JMenuItem("Configurar Puerto");
        JMenuItem bdd = new JMenuItem("Configurar Base de datos");
        conexion.add(puerto);
        conexion.add(bdd);

        //CATÁLOGO
        JMenu catalogo = new JMenu("Catálogo");
        JMenuItem addProducto = new JMenuItem("Añadir producto");
        JMenuItem edProducto = new JMenuItem("Modificar producto");
        JMenuItem delProducto = new JMenuItem("Eliminar producto");

        catalogo.add(addProducto);
        catalogo.add(edProducto);
        catalogo.add(delProducto);

        //Añadiendo los menús a la barra superior
        barraSuperior.add(conexion);       
       	barraSuperior.add(catalogo);

        //Creando la estadística de conexiones
        JLabel lblConexiones = new JLabel();
        lblConexiones.setText("Conexiones activas : "+conexiones);
        lblConexiones.setHorizontalAlignment(JLabel.CENTER);
        lblConexiones.setVerticalAlignment(JLabel.CENTER);

        //Creando la barra inferior
        JLabel barraInferior = new JLabel();
        barraInferior.setText("Ingrese los datos de conexión");
        barraInferior.setHorizontalAlignment(JLabel.CENTER);
        barraInferior.setVerticalAlignment(JLabel.CENTER);

        //Creando botón de inicio/detener de servidor
        JButton btnAccionServidor = new JButton();
        btnAccionServidor.setBackground(Color.GREEN);
        btnAccionServidor.setText("Iniciar Servidor");
        btnAccionServidor.setContentAreaFilled(false);
        btnAccionServidor.setOpaque(true);
        btnAccionServidor.setPreferredSize(new Dimension(300, 100));
        btnAccionServidor.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if(!iniciado){
                    //cambios en el especto del botón
    				iniciado=true;
    				btnAccionServidor.setBackground(Color.RED);
    				btnAccionServidor.setText("Detener Servidor");
    				btnAccionServidor.setForeground(Color.WHITE);
                    //
                    catalogue=new Catalogo(database.getUsuario(),database.getPassword());
                    if(catalogue.conectarBDD() && port.getPuerto()!=0){
                        barraInferior.setText("Puerto: "+Integer.toString(port.getPuerto())+"|       DB:   Conectado");
                        videojuegos.clear();
                        videojuegos=catalogue.obtenerVideojuegos(database.getUsuario(),database.getPassword());
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try(ServerSocket listener = new ServerSocket(port.getPuerto())){

                                    System.out.println("Socket iniciado");
                                    ExecutorService pool = Executors.newFixedThreadPool(10);
                                    while(true){
                                        pool.execute(new aCliente(listener.accept()));
                                    }
                                }
                                catch(Exception ex){
                                    System.out.println("No fue posible iniciar el socket");
                                }
                            }
                        });
                        t.start();
                    }
                    else
                        barraInferior.setText("Puerto: No establecido  |  DB:  Sin conexión");
    			}
    			else{
    				iniciado=false;
    				btnAccionServidor.setBackground(Color.GREEN);
    				btnAccionServidor.setText("Iniciar Servidor");
    				btnAccionServidor.setForeground(Color.BLACK);
    			}
    			
    		}
		});

        //listeners de acciones de menú Conexión
        puerto.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			try{
    				port.setPuerto();
    				
    			}
    			catch(Exception ex){
    				System.out.println(ex);
    			}
    		}
		});
        bdd.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			try{
    				database.setDataBase();
    			}
    			catch(Exception ex){
    				System.out.println(ex);
    			}
    			finally{
    				
    			}
    		}
		});

        //listeners de acciones de menú de catálogos
         addProducto.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			try{
    				catalogue.addProducto();
                    videojuegos=catalogue.obtenerVideojuegos(database.getUsuario(),database.getPassword());
    			}
    			catch(Exception ex){
    				System.out.println(ex);
    			}
    		}
		});
        edProducto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    catalogue.editProducto();
                    videojuegos=catalogue.obtenerVideojuegos(database.getUsuario(),database.getPassword());
                }
                catch(Exception ex){
                    System.out.println(ex);
                }
            }
        });
        delProducto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    catalogue.delProducto();
                    videojuegos=catalogue.obtenerVideojuegos(database.getUsuario(),database.getPassword());
                }
                catch(Exception ex){
                    System.out.println(ex);
                }
            }
        });

        //Añadiendo los elementos al flujo del frame
    	frame.getContentPane().add(btnAccionServidor,BorderLayout.CENTER);
        frame.getContentPane().add(lblConexiones, BorderLayout.PAGE_START);
    	frame.getContentPane().add(barraInferior, BorderLayout.PAGE_END);
       	frame.setJMenuBar(barraSuperior);
        frame.setVisible(true);
    }

    //clase anidada para los hilos
    private static class aCliente implements Runnable{
        private Socket socket;

        public aCliente(Socket socket){
            this.socket=socket;
        }
        @Override
        public void run(){
            conexiones++;
            try{
                DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
                ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
                salida.writeObject(videojuegos);
                salida.close();   

            }
            catch(Exception ex){
                
            }
        }
    }

    
}