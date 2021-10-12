package logica;

import java.awt.Color;

public class ProcesosDisponibles {
	
	public ProcesosDisponibles() {
		cargarProcesos();
	}
	
	private Proceso disponibles[] = new Proceso[8];

	/**
	 * Crea la lista de procesos diponibles para las pruebas
	 */
	private void cargarProcesos() {
		disponibles[0] = new Proceso( "VLC", 10);
		disponibles[1] = new Proceso( "Chrome", 9);
		disponibles[2] = new Proceso( "Word", 8);
		disponibles[3] = new Proceso( "Excel", 7);
		disponibles[4] = new Proceso( "Eclipse", 6);
		disponibles[5] = new Proceso( "Spider", 5);
		disponibles[6] = new Proceso( "PDFRead", 4);
		disponibles[7] = new Proceso( "Block", 3);

		
		
	}
	
	public Proceso[] getDisponibles() {
		return disponibles;
	}

	public void setDisponibles(Proceso[] disponibles) {
		this.disponibles = disponibles;
	}

}
