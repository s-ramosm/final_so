package logica;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

public class Segmentacion {
	
	public Segmentacion() {
		dividirMemoria();
	}
	
	private IntAHex conversorHex = new IntAHex();
	
	// Contador que permite asignar PID a cada proceso 
	// En este modelo contadorPID+1 servira para identificar la particion
	int contadorPID = 1;
	
	// Tamaño del segmento de pila en KB (100KB para poder viualizarlo graficamente)
	int tamPila = 100;
	
	private int memoriaPpal = 16384;
	private int memTotalLibre = 0;
	private Proceso SO = new Proceso(0, "S.O.", 2048, new Color(215, 215, 84));
	private ArrayList<Particion> particionesAr = new ArrayList<Particion>();
	private Particion particiones[];
	
	//Variables para la creacion de la tabla de procesos
	private DefaultTableModel modeloTabla;
	String[] nombreColumna = {"Proceso", "PID", "Segmento", "Base", "Limite"};
	Object[][] procesoModelo = new Object[1][5];
	
	public void dividirMemoria() {
		
		//Definicion del tamaño de la particion del S.0.
		particionesAr.add(new Particion(0, false, SO.getTamano(), SO, 0));
		
		memTotalLibre = memoriaPpal - SO.getTamano();
		particionesAr.add(new Particion(1, true, memTotalLibre, null, particionesAr.get(0).getTamano()));
		
		particiones = particionesAr.toArray(new Particion[0]);
		
		//Agrega S.O. al modelo de la tabla
		procesoModelo[0][0] = SO.getNombre();
		procesoModelo[0][1] = SO.getPID();
		procesoModelo[0][2] = "Todos";
		procesoModelo[0][3] = conversorHex.toHex(particiones[0].getInicio()*1024);
		procesoModelo[0][4] = conversorHex.toHex((particiones[0].getInicio()+ SO.getTamano()-1)*1024);
				
		modeloTabla = new DefaultTableModel(procesoModelo,  nombreColumna);
		
		//imprimir();
		
	}
	
	/**
	 * Añade los procesos a matriz memoria:
	 * @param proceso
	 * @param asignacion:
	 *  1-Primer ajuste | 2-Mejor ajuste | 3-Peor Ajuste
	 */
	public boolean añadirProceso(Proceso proceso, int segCodigo, int segDatos) {
		
		int posicion = 0;
		proceso.setPID(contadorPID);
		
		Particion[] copiaParticiones = particiones;
		ArrayList<Particion> copiaParticionesAr = particionesAr;
		
		
		//Verificar que la memoria total disponible tenga el tamaño suficiente
		if( memTotalLibre >= (proceso.getTamano() + tamPila) ) {
			
			//Se añades los tamaños de los segmentos
			proceso.addSegmentoCodigo(segCodigo);
			proceso.addSegmentoDatos(segDatos);
			proceso.addSegmentoPila(tamPila);
			
			boolean segCodigoAdd = false;
			boolean segDatosAdd = false;
			boolean segPilaAdd = false;
			
			for (int seg=1; seg<=3; seg++) {
				posicion = 0;
				// Segmento Codigo
				if(seg == 1) {
					for(int i=1; i<particiones.length; i++) {
						if(particiones[i].getDisponible() == true & particiones[i].getTamano() >= (int)proceso.getSegCodigo()[1] ) {
							posicion = i;
							break;
						}
					}
					if(posicion != 0) {
						//Si el tamaño de la particion es igual al tamaño proceso se hace la asignacion directa
						if(particiones[posicion].getTamano() == (int)proceso.getSegCodigo()[1]) {
							particionesAr.get(posicion).setProceso(proceso);
							particionesAr.get(posicion).setDisponible(false);
							particionesAr.get(posicion).setEsCodigo(true);
						}else {
							//Agrega la particion en la posicion donde hay espacio y le asigna el tamaño del proceso ("divide la particion")
							particionesAr.add(posicion,
									new Particion(contadorPID+1, false, (int)proceso.getSegCodigo()[1], proceso, particionesAr.get(posicion).getInicio()));
							particionesAr.get(posicion).setEsCodigo(true);
							//la particion desplazada ("Sobrante que queda libre de la particion dividida") se le asigna nuevo tamaño he inicio.
							particionesAr.get(posicion+1).setTamano( particionesAr.get(posicion+1).getTamano() - (int)proceso.getSegCodigo()[1] );
							particionesAr.get(posicion+1).setInicio(particionesAr.get(posicion).getInicio() + (int)proceso.getSegCodigo()[1]);
						}
						
						memTotalLibre = memTotalLibre - (int)proceso.getSegCodigo()[1];
						arrayListToArray();	
						
						//Agrega segmento al modelo de la tabla
						procesoModelo[0][0] = proceso.getNombre();
						procesoModelo[0][1] = proceso.getPID();
						procesoModelo[0][2] = "Codigo";
						procesoModelo[0][3] = conversorHex.toHex(particionesAr.get(posicion).getInicio()*1024);
						procesoModelo[0][4] = conversorHex.toHex((particionesAr.get(posicion).getInicio()+ particionesAr.get(posicion).getTamano()-1)*1024);
						modeloTabla.addRow(procesoModelo[0]);
						
						segCodigoAdd = true;
						
					}else					
						segCodigoAdd = false; 

				}
				// Segmento Datos
				if(seg == 2) {
					for(int i=1; i<particiones.length; i++) {
						if(particiones[i].getDisponible() == true & particiones[i].getTamano() >= (int)proceso.getSegDatos()[1] ) {
							posicion = i;
							break;
						}
					}
					if(posicion != 0) {
						//Si el tamaño de la particion es igual al tamaño proceso se hace la asignacion directa
						if(particiones[posicion].getTamano() == (int)proceso.getSegDatos()[1]) {
							particionesAr.get(posicion).setProceso(proceso);
							particionesAr.get(posicion).setDisponible(false);
							particionesAr.get(posicion).setEsDatos(true);
						}else {
							//Agrega la particion en la posicion donde hay espacio y le asigna el tamaño del proceso ("divide la particion")
							particionesAr.add(posicion,
									new Particion(contadorPID+1, false, (int)proceso.getSegDatos()[1], proceso, particionesAr.get(posicion).getInicio()));
							particionesAr.get(posicion).setEsDatos(true);
							//la particion desplazada ("Sobrante que queda libre de la particion dividida") se le asigna nuevo tamaño he inicio.
							particionesAr.get(posicion+1).setTamano( particionesAr.get(posicion+1).getTamano() - (int)proceso.getSegDatos()[1] );
							particionesAr.get(posicion+1).setInicio(particionesAr.get(posicion).getInicio() + (int)proceso.getSegDatos()[1]);
						}
						
						memTotalLibre = memTotalLibre - (int)proceso.getSegDatos()[1];
						arrayListToArray();	
						
						//Agrega segmento al modelo de la tabla
						procesoModelo[0][0] = proceso.getNombre();
						procesoModelo[0][1] = proceso.getPID();
						procesoModelo[0][2] = "Datos";
						procesoModelo[0][3] = conversorHex.toHex(particionesAr.get(posicion).getInicio()*1024);
						procesoModelo[0][4] = conversorHex.toHex((particionesAr.get(posicion).getInicio()+ particionesAr.get(posicion).getTamano()-1)*1024);
						modeloTabla.addRow(procesoModelo[0]);
						
						segDatosAdd = true;
						
					}else					
						segDatosAdd = false; 
				}
				// Segmento Pila
				if(seg == 3) {
					for(int i=1; i<particiones.length; i++) {
						if(particiones[i].getDisponible() == true & particiones[i].getTamano() >= (int)proceso.getSegPila()[1] ) {
							posicion = i;
							break;
						}
					}
					if(posicion != 0) {
						//Si el tamaño de la particion es igual al tamaño proceso se hace la asignacion directa
						if(particiones[posicion].getTamano() == (int)proceso.getSegPila()[1]) {
							particionesAr.get(posicion).setProceso(proceso);
							particionesAr.get(posicion).setDisponible(false);
							particionesAr.get(posicion).setEsPila(true);
						}else {
							//Agrega la particion en la posicion donde hay espacio y le asigna el tamaño del proceso ("divide la particion")
							particionesAr.add(posicion,
									new Particion(contadorPID+1, false, (int)proceso.getSegPila()[1], proceso, particionesAr.get(posicion).getInicio()));
							particionesAr.get(posicion).setEsPila(true);
							//la particion desplazada ("Sobrante que queda libre de la particion dividida") se le asigna nuevo tamaño he inicio.
							particionesAr.get(posicion+1).setTamano( particionesAr.get(posicion+1).getTamano() - (int)proceso.getSegPila()[1] );
							particionesAr.get(posicion+1).setInicio(particionesAr.get(posicion).getInicio() + (int)proceso.getSegPila()[1]);
						}
						
						memTotalLibre = memTotalLibre - (int)proceso.getSegPila()[1];
						arrayListToArray();	
						
						//Agrega segmento al modelo de la tabla
						procesoModelo[0][0] = proceso.getNombre();
						procesoModelo[0][1] = proceso.getPID();
						procesoModelo[0][2] = "Pila";
						procesoModelo[0][3] = conversorHex.toHex(particionesAr.get(posicion).getInicio()*1024);
						procesoModelo[0][4] = conversorHex.toHex((particionesAr.get(posicion).getInicio()+ particionesAr.get(posicion).getTamano()-1)*1024);
						modeloTabla.addRow(procesoModelo[0]);
						
						segPilaAdd = true;
						
					}else					
						segPilaAdd = false; 
				}
			}
			
			// Si se añadieron los tres segmentos se returna true, si no, se restablecen los arreglos
			// y se retorna false
			if(segCodigoAdd == true & segDatosAdd == true & segPilaAdd == true) {
				contadorPID++;
				return true;
			}else {
				particiones = copiaParticiones;
				particionesAr = copiaParticionesAr;
				return false;
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
		
		boolean eliminado = false;
		boolean anteriorLib = false;
		int tamProceso = 0;
		int noEliminados = 0;

		for(int i=1; i<particiones.length; i++) {
			
			if(particiones[i].getDisponible() == false) {

				if(particiones[i].getProceso().getPID() == PID) {
					tamProceso = particiones[i].getTamano();
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
					
					eliminado = true;
					noEliminados++;
					//reinicia y para verificar nuevamente (no se estaba eliminando la pila)
					if(noEliminados < 3)
						i = 1;
					
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
	
	//Pasar el arrayList a arreglo
	public void arrayListToArray() {
		particiones = particionesAr.toArray(new Particion[0]);
	}
	
	public void imprimir() {
		System.out.println("///////////////");
		for(int i=0; i<particiones.length; i++) {
			System.out.print("ID: " + particiones[i].getId());
			System.out.print(" | Disp: " + particiones[i].getDisponible());
			if(particiones[i].getDisponible()==false) {
				if(particiones[i].isCodigo())
					System.out.print(" | Es : " + particiones[i].getProceso().getSegCodigo()[0]);
				else if(particiones[i].isDatos())
        			System.out.print(" | Es : " + particiones[i].getProceso().getSegDatos()[0]);
				else if(particiones[i].isPila())
        			System.out.print(" | Es : " + particiones[i].getProceso().getSegPila()[0]);
        		
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

	public Proceso getSO() {
		return SO;
	}

	public void setSO(Proceso sO) {
		SO = sO;
	}

	public ArrayList<Particion> getParticionesAr() {
		return particionesAr;
	}

	public void setParticionesAr(ArrayList<Particion> particionesAr) {
		this.particionesAr = particionesAr;
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
