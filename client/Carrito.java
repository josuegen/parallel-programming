import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.net.URL;
import java.awt.Dimension;
import java.util.concurrent.*;

public class Carrito{
    private double total;
    private String servidor;
    private int puerto;
    private Videojuego videojuego=new Videojuego();
    private Icon portada = new ImageIcon();
    private ArrayList<Videojuego> videoJuegos= new ArrayList <Videojuego>();
    private Compra compra = new Compra();
    private Catalogo c = new Catalogo("admin","password");

    //constructores
    public Carrito(){

    }
    public Carrito(String servidor,int puerto){
        this.servidor=servidor;
        this.puerto=puerto;
    }

    public double getTotal(){
        return total;
    }

    public void setServidor(String servidor){
        this.servidor=servidor;
    }
    public void setPuerto(int puerto){
        this.puerto=puerto;
    }

    public void addProducto(Videojuego vj){
        videoJuegos.add(vj);
        total+=vj.getPrecio();
    }

    public boolean enviarCompra(Compra compra){
        
        if(c.generarCompra(compra))
            return true;
        else
            return false;

    }

    public void verCarrito(){
        String[] columnNames = {"SKU","Nombre","Precio(MXN)","Eliminar del carrito"};
        JFrame frame = new JFrame("Carrito");      
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor,BoxLayout.Y_AXIS));
        frame.setLayout(new BorderLayout());   

        //Total
        JLabel lblTotal = new JLabel();
        lblTotal.setText("Total: "+total);
        lblTotal.setHorizontalAlignment(JLabel.CENTER);
        lblTotal.setVerticalAlignment(JLabel.CENTER);
        lblTotal.setForeground(Color.RED);


        //Tabla de productos en el carrito
        DefaultTableModel dtm = new DefaultTableModel(0, 0)
        {
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        dtm.setColumnIdentifiers(columnNames);


        //llenando la tabla
        for(int i=0;i<videoJuegos.size();i++){
            Object[] newRow = {videoJuegos.get(i).getSKU(),videoJuegos.get(i).getNombre(),
                                videoJuegos.get(i).getPrecio(),"-"};
            dtm.addRow(newRow);
        }

        JTable table = new JTable(){
                public Class getColumnClass(int column) {
                    return (column == 5) ? Icon.class : Object.class;
                 }
            };;
        table.setModel(dtm);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (col ==3) {
                    dtm.removeRow(row);
                    total-=videoJuegos.get(row).getPrecio();
                    videoJuegos.remove(row);
                    lblTotal.setText("Total: "+total);
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);

        //Boton de comprar
        JButton btnComprar = new JButton();
        btnComprar.setText("Comprar");

        btnComprar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                compra = new Compra(videoJuegos);
                if(enviarCompra(compra)){
                    JOptionPane.showMessageDialog(null,"¡Compra realizada con éxito!");
                    dtm.setRowCount(0);
                    videoJuegos.clear();
                    total=0.0;
                }
                frame.setVisible(false);
                frame.dispose();
            }
        });


        //Botón de regresar
        JButton btnRegresar = new JButton();
        btnRegresar.setText("Regresar");

        btnRegresar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame.setVisible(false);
                frame.dispose();
            }
        });

        contenedor.add(scrollPane);
        contenedor.add(btnComprar);
        contenedor.add(btnRegresar);
        frame.add(contenedor,BorderLayout.CENTER);
        contenedor.setVisible(true);
        frame.add(lblTotal,BorderLayout.PAGE_END);
        frame.setVisible(true);
        frame.setSize(500, 400);
    }

    public void delProducto(Videojuego vj){
        for(int i=0;i<videoJuegos.size();i++){
            if(vj.getSKU()==videoJuegos.get(i).getSKU())
                videoJuegos.remove(i);
        }

    }
}