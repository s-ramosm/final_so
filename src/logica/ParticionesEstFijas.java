package logica;

import java.awt.Color;

import javax.swing.table.DefaultTableModel;

public class ParticionesEstFijas {
	
	//Constructor por defecto
	public ParticionesEstFijas(int asignacion) {
		dividirMemoria();
	}
	
	private IntAHex conversorHex = new IntAHex();
	
	//Contador que permite asignar PID a cada proceso
	int contadorPID = 1;
	//Metodo de asignacion
	int asignacion = 0;
	
	//Definicion de tamaño de la memoria, el sistema operativo
	//y de las particiones
	private int memoriaPpal = 16384;
	private Proceso SO = new Proceso(0, "S.O.", 2048, new Color(215, 215, 84));
	private int tamanoParticion = 1024;
	private Particion particiones[] = new Particion[((memoriaPpal-SO.getTamano())/tamanoParticion)+1];
	
	//Variables para la creacion de la tabla de procesos
	private DefaultTableModel modeloTabla;
	String[] nombreColumna = {"Proceso", "PID", "Tamaño", "Ocupado", "Inicio", "Fin"};
	Object[][] procesoModelo = new Object[1][6];
	
	//Metodo que divide la memoria en partes iguales sun el tamaño mas la particion del S.O.
	public void dividirMemoria() {
			
		//Definicion del tamaño de la particion del S.0.
		particiones[0] = new Particion(0, false, SO.getTamano(), SO, 0);
		
		//Agrega S.O. al modelo de la tabla
		procesoModelo[0][0] = SO.getNombre();
		procesoModelo[0][1] = SO.getPID();
		procesoModelo[0][2] = particiones[0].getTamano();
		procesoModelo[0][3] = particiones[0].getProceso().getTamano();
		procesoModelo[0][4] = conversorHex.toHex(particiones[0].getInicio()*1204);
		procesoModelo[0][5] = conversorHex.toHex(particiones[0].getInicio()*1204+particiones[0].getTamano()*1204);
		modeloTabla = new DefaultTableModel(procesoModelo,  nombreColumna);
		
		//Creacion de particiones disponibles, en un arreglo
		for(int i=1; i<particiones.length; i++) {
			particiones[i]= new Particion(i, true, tamanoParticion, null, 
										  particiones[i-1].getInicio()+particiones[i-1].getTamano());
		}
		
	}
	
	/**
	 * Añade los procesos a matriz memoria:
	 * @param proceso
	 * @param asignacion:
	 *  1-Primer ajuste | 2-Mejor ajuste | 3-Peor Ajuste
	 */
	public boolean añadirProceso(Proceso proceso, int asignacion) {
		
		int posicion = 0;
		proceso.setPID(contadorPID);
		
		//Primer ajuste: Asignar el primer fragmento libre que tenga el tamaño suficiente.
		if(asignacion==1){									
			for(int i=1; i<particiones.length; i++) {
				if(particiones[i].getDisponible() == true & particiones[i].getTamano() >= proceso.getTamano() ) {
					posicion = i;
					break;
				}
			}
			if(posicion != 0) {
				particiones[posicion].setProceso(proceso);
				particiones[posicion].setDisponible(false);
				contadorPID++;
				
				//Insertando en la tabla de procesos
				procesoModelo[0][0] = particiones[posicion].getProceso().getNombre();
				procesoModelo[0][1] = particiones[posicion].getProceso().getPID();
				procesoModelo[0][2] = particiones[posicion].getTamano();
				procesoModelo[0][3] = particiones[posicion].getProceso().getTamano();
				procesoModelo[0][4] = conversorHex.toHex(particiones[posicion].getInicio()*1024);
				procesoModelo[0][5] = conversorHex.toHex((particiones[posicion].getInicio()*1024+particiones[posicion].getTamano()*1024)-1);
				modeloTabla.addRow(procesoModelo[0]);
				
				return true;
			}else {
				return false;
			}
		}
		
		//Mejor ajuste: Asignar el fragmento más pequeño que tenga el tamaño suficiente.
		if(asignacion==2){									
			for(int i=1; i<particiones.length; i++) {
				if(particiones[i].getDisponible() == true & particiones[i].getTamano() >= proceso.getTamano() ) {
					if(posicion != 0) {
						if(particiones[posicion].getTamano() > particiones[i].getTamano())
							posicion = i;
					}else {
						posicion = i;
					}
					
				}
			}
			if(posicion != 0) {
				particiones[posicion].setProceso(proceso);
				particiones[posicion].setDisponible(false);
				contadorPID++;
				
				//Insertando en la tabla de procesos
				procesoModelo[0][0] = particiones[posicion].getProceso().getNombre();
				procesoModelo[0][1] = particiones[posicion].getProceso().getPID();
				procesoModelo[0][2] = particiones[posicion].getTamano();
				procesoModelo[0][3] = particiones[posicion].getProceso().getTamano();
				procesoModelo[0][4] = conversorHex.toHex(particiones[posicion].getInicio()*1024);
				procesoModelo[0][5] = conversorHex.toHex((particiones[posicion].getInicio()*1024+particiones[posicion].getTamano()*1024)-1);
				modeloTabla.addRow(procesoModelo[0]);
				
				return true;
			}else {
				return false;
			}
		}		
		
		//Peor ajuste: Asignar el fragmento más grande.
		if(asignacion==3){									
			for(int i=1; i<particiones.length; i++) {
				if(particiones[i].getDisponible() == true & particiones[i].getTamano() >= proceso.getTamano() ) {
					if(posicion != 0) {
						if(particiones[posicion].getTamano() < particiones[i].getTamano())
							posicion = i;
					}else {
						posicion = i;
					}
					
				}
			}
			if(posicion != 0) {
				particiones[posicion].setProceso(proceso);
				particiones[posicion].setDisponible(false);
				contadorPID++;
				
				//Insertando en la tabla de procesos
				procesoModelo[0][0] = particiones[posicion].getProceso().getNombre();
				procesoModelo[0][1] = particiones[posicion].getProceso().getPID();
				procesoModelo[0][2] = particiones[posicion].getTamano();
				procesoModelo[0][3] = particiones[posicion].getProceso().getTamano();
				procesoModelo[0][4] = conversorHex.toHex(particiones[posicion].getInicio()*1024);
				procesoModelo[0][5] = conversorHex.toHex((particiones[posicion].getInicio()*1024+particiones[posicion].getTamano()*1024)-1);
				modeloTabla.addRow(procesoModelo[0]);
				
				return true;
			}else {
				return false;
			}
		}	
		
		return false;
		
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

					eliminado = true;	
				}
			}
		}
		
		//Eliminando de la tabla de particiones
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
	
	//getter y setter
	public int getMemoriaPpal() {
		return memoriaPpal;
	}

	public void setMemoriaPpal(int memoriaPpal) {
		this.memoriaPpal = memoriaPpal;
	}

	public Proceso getSO() {
		return SO;
	}

	public void setSO(Proceso SO) {
		this.SO = SO;
	}

	public int getTamanoParticion() {
		return tamanoParticion;
	}

	public void setTamanoParticion(int tamanoParticion) {
		this.tamanoParticion = tamanoParticion;
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
