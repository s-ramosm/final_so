package logica;

import java.awt.Color;

public class Proceso {
	
	public Proceso() {
	}
	
	public Proceso( String nombre, int llegada,int ejecucion, int interrupcion, int duracion_interupcion) {
	
		this.nombre = nombre;
		this.tamano = ejecucion;
                this.interrupcion = interrupcion;
                this.duración_interrupcion = duracion_interupcion;
                this.llegada = llegada;
                
	}
        
        public Proceso( String nombre, int ejecucion) {
	
		this.nombre = nombre;
		this.tamano = ejecucion;
                
	}

        public String estado = "por entrar";
        public int llegada;
        public int cordy = 0;
	private int PID = -1;
	private String nombre = "";
	private int tamano = 0;
	private Color color = new Color(86, 186, 7);
	
        public int tiempo_esperando = 0;
        public int tiempo_ejecutandose = 0;
        public int interrupcion;
        public int duración_interrupcion;
	//Variables para la segmentacion
	private Object[] segDatos = {"Segmento de Datos", 0, new Color(0, 0, 0)};
	private Object[] segCodigo = {"Segmento de Codigo", 0, new Color(0, 0, 0)};
	private Object[] segPila = {"Segmento Pila", 0, new Color(0, 0, 0)};
	
	// Agregar tama�o de segmento de Datos
	public void addSegmentoDatos(int tamano) {
		segDatos[1] = tamano;
	}
	
	// Agregar tama�o de segmento de Codigo
	public void addSegmentoCodigo(int tamano) {
		segCodigo[1] = tamano;
	}
	
	// Agregar tama�o de segmento de Pila
	// Se calcula como el 10% del tama�o del proceso (Propuesta propia)
	public void addSegmentoPila(int tamano) {
		segPila[1] = tamano;
	}
	
	// Getter y setter
	public int getPID() {
		return PID;
	}

	public void setPID(int pID) {
		PID = pID;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getTamano() {
		return tamano;
	}

	public void setTamano(int tamano) {
		this.tamano = tamano;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Object[] getSegDatos() {
		return segDatos;
	}

	public void setSegDatos(Object[] segDatos) {
		this.segDatos = segDatos;
	}

	public Object[] getSegCodigo() {
		return segCodigo;
	}

	public void setSegCodigo(Object[] segCodigo) {
		this.segCodigo = segCodigo;
	}

	public Object[] getSegPila() {
		return segPila;
	}

	public void setSegPila(Object[] segPila) {
		this.segPila = segPila;
	}
	
}
