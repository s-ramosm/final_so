package logica;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

public class ParticionesDinamicas {
	
	//Constructor por defecto
	public ParticionesDinamicas(int asignacion) {
		dividirMemoria();
	}
	
	private IntAHex conversorHex = new IntAHex();
	
	//Contador que permite asignar PID a cada proceso 
	// En este modelo contadorPID+1 servira para identificar la particion
	int contadorPID = 1;
	//Metodo de asignacion
	int asignacion = 0;
	
	//Definicion de tamaño de la memoria, el sistema operativo
	//y de las particiones
	private int memoriaPpal = 16384;
	private int memTotalLibre = 0;
	private Proceso SO = new Proceso(0, "S.O.", 2048, new Color(215, 215, 84));
	private ArrayList<Particion> particionesAr= new ArrayList<Particion>();
	private Particion particiones[];
	
	//Variables para la creacion de la tabla de procesos
	private DefaultTableModel modeloTabla;
	String[] nombreColumna = {"Proceso", "PID", "Tamaño", "Ocupado", "Inicio", "Fin"};
	Object[][] procesoModelo = new Object[1][6];
	
	//Metodo que divide la memoria en particion del S.O. y las memoria libre
	public void dividirMemoria() {
			
		//Definicion del tamaño de la particion del S.0.
		particionesAr.add(new Particion(0, false, SO.getTamano(), SO, 0));
		
		memTotalLibre = memoriaPpal - SO.getTamano();
		particionesAr.add(new Particion(1, true, memTotalLibre, null, particionesAr.get(0).getTamano()));
		
		particiones = particionesAr.toArray(new Particion[0]);
		
		//Agrega S.O. al modelo de la tabla
		procesoModelo[0][0] = SO.getNombre();
		procesoModelo[0][1] = SO.getPID();
		procesoModelo[0][2] = particiones[0].getTamano();
		procesoModelo[0][3] = particiones[0].getProceso().getTamano();
		procesoModelo[0][4] = conversorHex.toHex(particiones[0].getInicio()*1204);
		procesoModelo[0][5] = conversorHex.toHex(particiones[0].getInicio()*1204+particiones[0].getTamano()*1204);
		modeloTabla = new DefaultTableModel(procesoModelo,  nombreColumna);
		
	}
	
	/**
	 * Añade los procesos a matriz memoria:
	 * @param proceso
	 * @param asignacion:
	 *  1-Primer ajuste | 2-Mejor ajuste | 3-Peor Ajuste
	 */
	public boolean añadirProceso(Proceso proceso, int asignacion, boolean compactacion) {
		
		int posicion = 0;
		proceso.setPID(contadorPID);
		
		//Verificar que la memoria total disponible tenga el tamaño suficiente
		if(memTotalLibre >= proceso.getTamano()) {
			
			boolean procesoAgregado = false;
			//Primer ajuste: Asignar el primer fragmento libre que tenga el tamaño suficiente.
			if(asignacion==1){									
				for(int i=1; i<particiones.length; i++) {
					if(particiones[i].getDisponible() == true & particiones[i].getTamano() >= proceso.getTamano() ) {
						posicion = i;
						break;
					}
				}
				if(posicion != 0) {
					//Si el tamaño de la particion es igual al tamaño proceso se hace la asignacion directa
					if(particiones[posicion].getTamano() == proceso.getTamano()) {
						particionesAr.get(posicion).setProceso(proceso);
						particionesAr.get(posicion).setDisponible(false);
					}else {
						//Agrega la particion en la posicion donde hay espacio y le asigna el tamaño del proceso ("divide la particion")
						particionesAr.add(posicion,
								new Particion(contadorPID+1, false, proceso.getTamano(), proceso, particionesAr.get(posicion).getInicio()));
						//la particion desplazada ("Sobrante que queda libre de la particion dividida") se le asigna nuevo tamaño he inicio.
						particionesAr.get(posicion+1).setTamano(particionesAr.get(posicion+1).getTamano()-proceso.getTamano());
						particionesAr.get(posicion+1).setInicio(particionesAr.get(posicion).getInicio()+proceso.getTamano());
					}
					contadorPID++;
					memTotalLibre = memTotalLibre - proceso.getTamano();
					arrayListToArray();
					
					//Insertando en la tabla de procesos
					procesoModelo[0][0] = particiones[posicion].getProceso().getNombre();
					procesoModelo[0][1] = particiones[posicion].getProceso().getPID();
					procesoModelo[0][2] = particiones[posicion].getTamano();
					procesoModelo[0][3] = particiones[posicion].getProceso().getTamano();
					procesoModelo[0][4] = conversorHex.toHex(particiones[posicion].getInicio()*1024);
					procesoModelo[0][5] = conversorHex.toHex((particiones[posicion].getInicio()*1024+particiones[posicion].getTamano()*1024)-1);
					modeloTabla.addRow(procesoModelo[0]);
					
					procesoAgregado = true;
				}else {
					procesoAgregado = false;
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
					//Si el tamaño de la particion es igual al tamaño proceso se hace la asignacion directa
					if(particiones[posicion].getTamano() == proceso.getTamano()) {
						particionesAr.get(posicion).setProceso(proceso);
						particionesAr.get(posicion).setDisponible(false);
					}else {
						//Agrega la particion en la posicion donde hay espacio y le asigna el tamaño del proceso ("divide la particion")
						particionesAr.add(posicion,
								new Particion(contadorPID+1, false, proceso.getTamano(), proceso, particionesAr.get(posicion).getInicio()));
						//la particion desplazada ("Sobrante que queda libre de la particion dividida") se le asigna nuevo tamaño he inicio.
						particionesAr.get(posicion+1).setTamano(particionesAr.get(posicion+1).getTamano()-proceso.getTamano());
						particionesAr.get(posicion+1).setInicio(particionesAr.get(posicion).getInicio()+proceso.getTamano());
					}
					contadorPID++;
					memTotalLibre = memTotalLibre - proceso.getTamano();
					arrayListToArray();

					//Insertando en la tabla de procesos
					procesoModelo[0][0] = particiones[posicion].getProceso().getNombre();
					procesoModelo[0][1] = particiones[posicion].getProceso().getPID();
					procesoModelo[0][2] = particiones[posicion].getTamano();
					procesoModelo[0][3] = particiones[posicion].getProceso().getTamano();
					procesoModelo[0][4] = conversorHex.toHex(particiones[posicion].getInicio()*1024);
					procesoModelo[0][5] = conversorHex.toHex((particiones[posicion].getInicio()*1024+particiones[posicion].getTamano()*1024)-1);
					modeloTabla.addRow(procesoModelo[0]);
					
					procesoAgregado = true;
				}else {
					procesoAgregado = false;
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
					//Si el tamaño de la particion es igual al tamaño proceso se hace la asignacion directa
					if(particiones[posicion].getTamano() == proceso.getTamano()) {
						particionesAr.get(posicion).setProceso(proceso);
						particionesAr.get(posicion).setDisponible(false);
					}else {
						//Agrega la particion en la posicion donde hay espacio y le asigna el tamaño del proceso ("divide la particion")
						particionesAr.add(posicion,
								new Particion(contadorPID+1, false, proceso.getTamano(), proceso, particionesAr.get(posicion).getInicio()));
						//la particion desplazada ("Sobrante que queda libre de la particion dividida") se le asigna nuevo tamaño he inicio.
						particionesAr.get(posicion+1).setTamano(particionesAr.get(posicion+1).getTamano()-proceso.getTamano());
						particionesAr.get(posicion+1).setInicio(particionesAr.get(posicion).getInicio()+proceso.getTamano());
					}
					contadorPID++;
					memTotalLibre = memTotalLibre - proceso.getTamano();
					arrayListToArray();

					//Insertando en la tabla de procesos
					procesoModelo[0][0] = particiones[posicion].getProceso().getNombre();
					procesoModelo[0][1] = particiones[posicion].getProceso().getPID();
					procesoModelo[0][2] = particiones[posicion].getTamano();
					procesoModelo[0][3] = particiones[posicion].getProceso().getTamano();
					procesoModelo[0][4] = conversorHex.toHex(particiones[posicion].getInicio()*1024);
					procesoModelo[0][5] = conversorHex.toHex((particiones[posicion].getInicio()*1024+particiones[posicion].getTamano()*1024)-1);
					modeloTabla.addRow(procesoModelo[0]);
					
					procesoAgregado = true;
				}else {
					procesoAgregado = false;
				}
			}
			
			//Si el proceso se agrego retorna true, de lo contrario hay que compactar
			if (procesoAgregado == true) {
				return procesoAgregado;
			}else {
				if(compactacion == true & memTotalLibre >= proceso.getTamano()) {
					procesoAgregado = compactar(proceso.getTamano());
					procesoAgregado = añadirProceso(proceso, asignacion, compactacion);
					memTotalLibre = memTotalLibre - proceso.getTamano();
					
					//Se recontruye el modelo de la tabla
					modeloTabla.setRowCount(0);
					//Eliminando de la tabla de particiones
					for (int i = 0; i < particiones.length; i++) {
						//Insertando en la tabla de procesos
						procesoModelo[0][0] = particiones[i].getProceso().getNombre();
						procesoModelo[0][1] = particiones[i].getProceso().getPID();
						procesoModelo[0][2] = particiones[i].getTamano();
						procesoModelo[0][3] = particiones[i].getProceso().getTamano();
						procesoModelo[0][4] = conversorHex.toHex(particiones[i].getInicio()*1024);
						procesoModelo[0][5] = conversorHex.toHex((particiones[i].getInicio()*1024+particiones[i].getTamano()*1024)-1);
						modeloTabla.addRow(procesoModelo[0]);
					}
					
				}
				
				return procesoAgregado;
				
			}
			
		//Memoria total es insuficiente
		}else
			return false;
	}
	
	
	/**
	 * Eliminar proceso
	 * @param PID
	 * @return verdadero si se realia el proceso
	 */
	public boolean eliminarProceso(int PID) {
		
		boolean anteriorLib = false;
		int tamProceso = 0;

		for(int i=1; i<particiones.length; i++) {
			if(particiones[i].getDisponible() == false) {
				
				if(particiones[i].getProceso().getPID() == PID) {
					tamProceso = particiones[i].getProceso().getTamano();
					//Se trabaja sobre el array, asi que el que se modifica primero es el arrayList luego si el array con arrayListToArray(). 
					//Verifica si la particion anterior a la que se debe eliminar esta libre, si lo es las une. Si no lo quita el proceso buscado y libera la particion
					if(particiones[i-1].getDisponible() == true) {
						particionesAr.get(i-1).setTamano(particionesAr.get(i-1).getTamano() + particionesAr.get(i).getTamano());
						particionesAr.remove(i);
						anteriorLib = true;
					}else {
						particionesAr.get(i).setDisponible(true);
						particionesAr.get(i).setProceso(null);
						anteriorLib = false;
					}
					//Verifica si la particion siguiente a la que se debe eliminar existe y si esta libre, si lo es las une.
					if(i+1 < particiones.length) {
						if(particiones[i+1].getDisponible() == true) {
							if (anteriorLib == true) {
								particionesAr.get(i-1).setTamano(particionesAr.get(i-1).getTamano() + particiones[i+1].getTamano());
								particionesAr.remove(i);
							}else {
								particionesAr.get(i).setTamano(particionesAr.get(i).getTamano() + particionesAr.get(i+1).getTamano());
								particionesAr.remove(i+1);
							}
							
						}
					}
					memTotalLibre = memTotalLibre + tamProceso;
					arrayListToArray();

					//Eliminando de la tabla de particiones
					int removidos = 0;	//lleva el control de los removidos para reposicionar "i".
					int filas = modeloTabla.getRowCount();
					//Eliminando de la tabla de particiones
					for (int j = 0; j < filas; j++) {
						if((int)modeloTabla.getValueAt(j-removidos, 1) == PID) {
							modeloTabla.removeRow(j-removidos);
							removidos++;
						}
					}
					
					return true;	
				}
			}
		}
		return false;	
	}
	
	//Pasar el arrayList a rreglo
	public void arrayListToArray() {
		particiones = particionesAr.toArray(new Particion[0]);
	}
	
	//Une las particiones libres
	public boolean compactar(int tamProceso) {
		int pos1 = 0;
		int pos2 = 0;
		int tamParNueva = 0;
		
		while (tamParNueva < tamProceso) {
			
			for(int i=1; i<particiones.length; i++) {
				if(particiones[i].getDisponible() == true) {
					pos1 = i;
					for(i=i+1; i<particiones.length; i++){
						if(particiones[i].getDisponible() == true) {
							pos2 = i;
							i = particiones.length;
						}
					}
				}
			}
			
			//Mueve la primera particion libre hasta una posicion antes de la segunda particion y borra la posicion antigua
			particionesAr.get(pos1).setInicio(particionesAr.get(pos2).getInicio()-particionesAr.get(pos1).getTamano());
			particionesAr.add(pos2, particionesAr.get(pos1));
			particionesAr.remove(pos1);


			for (int i = pos1; i<pos2-1; i++) {
				particionesAr.get(i).setInicio(particionesAr.get(i-1).getInicio()+particionesAr.get(i-1).getTamano());	

			}
			
			//Asigna tamaño y posicion de inicio a la particion nueva y borra la segunda particion antigua
			particionesAr.get(pos2-1).setTamano(particionesAr.get(pos2-1).getTamano()+particionesAr.get(pos2).getTamano());
			//particionesAr.get(pos2-1).setInicio(particionesAr.get(pos2-2).getInicio()+particionesAr.get(pos2-2).getTamano());
			particionesAr.remove(pos2);
			tamParNueva = particionesAr.get(pos2-1).getTamano();
			
			arrayListToArray();
			
		}
		
		return true;
		
	}

	
	public void imprimir() {
		System.out.println("///////////////");
		for(int i=0; i<particiones.length; i++) {
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
	
	public void imprimirAr() {
		System.out.println("///////////////- Lista");
		for(int i=0; i<particionesAr.size(); i++) {
			System.out.print("ID: " + particionesAr.get(i).getId());
			System.out.print(" | Disp: " + particionesAr.get(i).getDisponible());
			if(particionesAr.get(i).getDisponible()==false) {
				System.out.print(" | PID: " + particionesAr.get(i).getId());
				System.out.print(" | Proc: " + particionesAr.get(i).getProceso().getNombre());
			}
			System.out.print(" | Tam: " + particionesAr.get(i).getTamano());
			System.out.print(" | Ini: " + particionesAr.get(i).getInicio());
			System.out.println();
		}
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

	public Particion[] getParticiones() {
		return particiones;
	}

	public void setParticiones(Particion[] particiones) {
		this.particiones = particiones;
	}

	public int getMemTotalLibre() {
		return memTotalLibre;
	}

	public void setMemTotalLibre(int memTotalLibre) {
		this.memTotalLibre = memTotalLibre;
	}

	public DefaultTableModel getModeloTabla() {
		return modeloTabla;
	}

	public void setModeloTabla(DefaultTableModel modeloTabla) {
		this.modeloTabla = modeloTabla;
	}

}
