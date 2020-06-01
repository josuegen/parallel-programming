import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap; 
import java.util.Map; 
import java.util.Map.Entry; 
import java.net.URL;
import java.awt.Dimension;

public class Compra implements Serializable{
	private double total;
	private ArrayList<String[]> compra = new ArrayList<String[]>();
	private Map<String, Integer> hm = new HashMap<String, Integer>();
	public Compra(){

	}

	public Compra(ArrayList<Videojuego> vjs){
		for (int k=0;k<vjs.size();k++) { 
			total+=vjs.get(k).getPrecio();
			String i=String.valueOf(vjs.get(k).getSKU());
            Integer j = hm.get(i); 
            hm.put(i, (j == null) ? 1 : j + 1); 
        }
        for (Map.Entry<String, Integer> val : hm.entrySet()) { 
        	String[] productoxcantidad = {val.getKey(),String.valueOf(val.getValue())};
        	compra.add(productoxcantidad);
        }
	}

	public double getTotal(){
		return total;
	}
	public ArrayList<String[]> getCompra(){
		return compra;
	}
}