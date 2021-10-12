package logica;

import java.awt.Color;

import javax.swing.table.DefaultTableModel;

public class Paginacion {
	
	public Paginacion() {
		dividirMemoria();
	}
	
	private IntAHex conversorHex = new IntAHex();
	
	//Contador que permite asignar PID a cada proceso
	int contadorPID = 1;
	//Calcular cantidad de paginas
	int noPaginas = 0;
	
	private int memoriaPpal = 16384;
	private int memTotalLibre = 0;
	private int tamMarco = 256;
	private Proceso SO = new Proceso(0, "S.O.", 2048, new Color(215, 215, 84));
	private Particion particiones[] = new Particion[((memoriaPpal-SO.getTamano())/tamMarco)+1];
	
	//Variables para la creacion de la tabla de procesos
	private DefaultTableModel modeloTabla;
	String[] nombreColumna = {"Proceso", "PID", "Pagina", "Marco"};
	Object[][] procesoModelo = new Object[1][4];
	
	//Metodo que divide la memoria en particion del S.O. y las memoria libre
	public void dividirMemoria() {
			
		//Definicion del tamaño de la particion del S.0.
		particiones[0] = new Particion(0, false, SO.getTamano(), SO, 0);
		memTotalLibre = memoriaPpal - SO.getTamano();
		
		//Agrega S.O. al modelo de la tabla
		procesoModelo[0][0] = SO.getNombre();
		procesoModelo[0][1] = SO.getPID();
		procesoModelo[0][2] = particiones[0].getId();
		procesoModelo[0][3] = conversorHex.toHex(particiones[0].getInicio());
		modeloTabla = new DefaultTableModel(procesoModelo,  nombreColumna);
		
		//Creacion de particiones disponibles, en un arreglo
		for(int i=1; i<particiones.length; i++) {
			particiones[i]= new Particion(i, true, tamMarco, null, 
										  particiones[i-1].getInicio()+particiones[i-1].getTamano());
		}
		
		//imprimir();
		
	}
	
	/**
	 * Añade los procesos a matriz memoria:
	 * @param proceso
	 * @param asignacion:
	 *  1-Primer ajuste | 2-Mejor ajuste | 3-Peor Ajuste
	 */
	public boolean añadirProceso(Proceso proceso) {
		
		boolean insertado = false;
		//Si hay memoria suficiente libre se agrega el proceso
		if(memTotalLibre >= proceso.getTamano()) {
			
			//Obtiene el numero de paginas(segmentos de memoria) que ocupara el proceso
			noPaginas = calcularNoPaginas(proceso);
			
			//Lleva el numera de pagina del procesos para ser hubicado en la tabla de procesos
			int pagina = 0;
			
			int posicion = 0;
			proceso.setPID(contadorPID);
			
			while(noPaginas > 0) {
				for(int i=1; i<particiones.length; i++) {
					if(particiones[i].getDisponible() == true) {
						posicion = i;
						break;
					}
				}
				if(posicion != 0) {
					particiones[posicion].setProceso(proceso);
					particiones[posicion].setDisponible(false);
					noPaginas--;
					//Insertando en la tabla de procesos
					procesoModelo[0][0] = particiones[posicion].getProceso().getNombre();
					procesoModelo[0][1] = particiones[posicion].getProceso().getPID();
					procesoModelo[0][2] = pagina;
					procesoModelo[0][3] = conversorHex.toHex(particiones[posicion].getInicio()*1024);
					modeloTabla.addRow(procesoModelo[0]);
					pagina++;
					insertado = true;
				}else {
					insertado = false;
				}
			}
			
			if(insertado ==true) {
				contadorPID++;
				memTotalLibre = memTotalLibre - proceso.getTamano();
			}
		}
		
		
		return insertado;
		
	}
	
	/**
	 * Eliminar proceso
	 * @param PID
	 * @return verdadero si se realia el proceso
	 */
	public boolean eliminarProceso(int PID) {
		
		boolean eliminado = false;
		
		for(int i=1; i<particiones.length; i++) {
			if(particiones[i].getDisponible() == false) {
				if(particiones[i].getProceso().getPID() == PID) {
					particiones[i].setDisponible(true);
					particiones[i].setProceso(null);
					memTotalLibre = memTotalLibre + particiones[i].getTamano();
					eliminado = true;
					
				}
			}
		}
		
		
		int removidos = 0;	//lleva el control de los removidos para reposicionar "i".
		int filas = modeloTabla.getRowCount();
		//Eliminando de la tabla de particiones
		for (int i = 0; i < filas; i++) {
			if((int)modeloTabla.getValueAt(i-removidos, 1) == PID) {
				modeloTabla.removeRow(i-removidos);
				removidos++;
			}
		}
		
		return eliminado;	
	}

	//Calcula el numero de paginas que se deben asignar al proceso dependiendo del tamaño de los Marcos
	public int calcularNoPaginas(Proceso proceso) {
		
		int noPaginas = 0;
		//Tamaño del proceso en KB
		int tamProceso = proceso.getTamano(); 
		
		while(tamProceso > 0){
			tamProceso = tamProceso - tamMarco;
			noPaginas++;
		}
		
		return noPaginas;
	}
	
	public void imprimir() {
		System.out.println("///////////////");
		System.out.println("Tamaño: " + particiones.length);
		for(int i=0; i<particiones.length; i++) {
			System.out.print("i: " + i + " | ");
			System.out.print("ID: " + particiones[i].getId());
			System.out.print(" | Disp: " + particiones[i].getDisponible());
			if(particiones[i].getDisponible()==false) {
				System.out.print(" | PID: " + particiones[i].getProceso().getPID());
				System.out.print(" | Proc: " + particiones[i].getProceso().getNombre());
			}
			System.out.print(" | Tam: " + particiones[i].getTamano());
			System.out.print(" | Ini: " + particiones[i].getInicio());
			System.out.println();
		}
	}

	
	public int getMemoriaPpal() {
		return memoriaPpal;
	}

	public void setMemoriaPpal(int memoriaPpal) {
		this.memoriaPpal = memoriaPpal;
	}

	public int getMemTotalLibre() {
		return memTotalLibre;
	}

	public void setMemTotalLibre(int memTotalLibre) {
		this.memTotalLibre = memTotalLibre;
	}

	public int getTamMarco() {
		return tamMarco;
	}

	public void setTamMarco(int tamMarco) {
		this.tamMarco = tamMarco;
	}

	public Proceso getSO() {
		return SO;
	}

	public void setSO(Proceso sO) {
		SO = sO;
	}

	public Particion[] getParticiones() {
		return particiones;
	}

	public void setParticiones(Particion[] particiones) {
		this.particiones = particiones;
	}

	public DefaultTableModel getModeloTabla() {
		return modeloTabla;
	}

	public void setModeloTabla(DefaultTableModel modeloTabla) {
		this.modeloTabla = modeloTabla;
	}
	
}
