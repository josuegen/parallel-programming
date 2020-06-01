import java.net.*;
import java.io.*;

public class Videojuego implements Serializable{
	private int sku;
	private String nombre;
	private int fechaLanzamiento;
	private int nJugadores;
	private String categoria;
	private String desarrollador;
	private Boolean online;
	private String portada;
	private int existencias;
	private double precio;

	public Videojuego(){

	}
	public Videojuego(String nombre){
		this.nombre=nombre;
	}

	public Videojuego(int sku,String nombre,int fechaLanzamiento,int nJugadores,String categoria,
						String desarrollador,Boolean online,String portada,int existencias,double precio){
		this.sku=sku;
		this.nombre=nombre;
		this.fechaLanzamiento=fechaLanzamiento;
		this.nJugadores=nJugadores;
		this.categoria=categoria;
		this.desarrollador=desarrollador;
		this.online=online;
		this.portada=portada;
		this.existencias=existencias;
		this.precio=precio;
	}

	public Videojuego(int sku,String nombre,String portada,double precio){
		this.sku=sku;
		this.nombre=nombre;
		this.portada=portada;
		this.precio=precio;
	}
	//getters
	public int getSKU(){
		return sku;
	}
	public String getNombre(){
		return nombre;
	}
	public int getFechaLanzamiento(){
		return fechaLanzamiento;
	}
	public int getNJugadores(){
		return nJugadores;
	}
	public String getCategoria(){
		return categoria;
	}
	public String getDesarrollador(){
		return desarrollador;
	}
	public boolean getOnline(){
		return online;
	}
	public String getPortada(){
		return portada;
	}
	public int getExistencias(){
		return existencias;
	}
	public double getPrecio(){
		return precio;
	}

	//setters
	public void setSKU(int sku){
		this.sku=sku;
	}
	public void setNombre(String nombre){
		this.nombre=nombre;
	}
	public void setFechaLanzamiento(int fechaLanzamiento){
		this.fechaLanzamiento=fechaLanzamiento;
	}
	public void setNJugadores(int nJugadores){
		this.nJugadores=nJugadores;
	}
	public void setCategoria(String categoria){
		this.categoria=categoria;
	}
	public void setDesarrollador(String desarrollador){
		this.desarrollador=desarrollador;
	}
	public void setOnline(boolean online){
		this.online=online;
	}
	public void setPortada(String portada){
		this.portada=portada;
	}
	public void setExistencias(int existencias){
		this.existencias=existencias;
	}
	public void setPrecio(double precio){
		this.precio=precio;
	}
}