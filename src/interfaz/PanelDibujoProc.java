package interfaz;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.UIManager;

import logica.Paginacion;
import logica.ParticionesDinamicas;
import logica.ParticionesEstFijas;
import logica.ParticionesEstVariables;
import logica.Segmentacion;

@SuppressWarnings("serial")
public class PanelDibujoProc extends JPanel{
	
	public PanelDibujoProc(int modelo, int asignacion) {
		this.modelo = modelo;
		this.asignacion = asignacion;
		
		setToolTipText("");
		
		inicialModelo();
		iniciarDibujoMemLibre();
	}
	
	private PanelDibujoMem dibujoMemLibre; 
	
	private int modelo = 0;
	private int asignacion = 0;
	
	private Color verde = new Color(86, 186, 7);
	private Color amarillo = new Color(215, 215, 84);
	private Color negro = new Color(0, 0, 0);
	
	private ParticionesEstFijas particionesEstFijas;
	private ParticionesEstVariables particionesEstVariables;
	private ParticionesDinamicas particionesDinamicas;
	private Paginacion paginacion;
	private Segmentacion segmentacion;
	
	/**
	 * Pintar el recuadro de procesos en memoria
	 */
    public void paint(Graphics g) {

    	//Modelo Particiones Estaticas Fijas
    	if(modelo == 1) {
        	
        	int pos = 0; 	//Llava la posicion de donde se va a dibujar
        	g.setFont(new Font("Tahoma", Font.BOLD, 7));		//Tamaño del texto en el dibujo
        	
        	//Calculando tamaño del S.O. para ser dibujada
        	double tamanoSO = (particionesEstFijas.getSO().getTamano()*100.0)/particionesEstFijas.getMemoriaPpal();
        	int drawSO = (int) (getWidth()*(tamanoSO/100));

        	g.setColor(amarillo);
        	g.fillRect(pos, 0, drawSO, getHeight());
        	g.setColor(negro);
        	g.drawRect(pos, 0, drawSO, getHeight()-1);
        	
        	g.drawString(particionesEstFijas.getParticiones()[0].getProceso().getNombre(), 5, (getHeight()*2)/5);
        	g.drawString("PID=" + particionesEstFijas.getParticiones()[0].getProceso().getPID(), 5, ((getHeight()*4)/5));
        	//g.drawString("T: " + partcionesEstFijas.getParticiones()[0].getTamano() + "KB", 5, (getHeight()*3)/5);
        	
        	pos = pos + drawSO;
        	
        	//Recorrer el arreglo de particiones para ir dibujando cada una
        	for(int i=1; i<particionesEstFijas.getParticiones().length;i++) {
        		
        		int tamDibujo = cacularTamDibujo(i);
        		int tamProceso = 0;
        		
        		g.setColor(verde);
        		
        		//Verificar si hay proceso en la particion, si lo hay calcular el tamaño
        		if(particionesEstFijas.getParticiones()[i].getDisponible() == true)
        			tamProceso = 0;	
        		else
        			tamProceso = cacularTamProceso(i);

            	g.fillRect(pos, 0, tamDibujo, getHeight());	//Pintar particion
            	
            	if (tamProceso != 0) {		//Si la particion tiene proceso pintar proceso
            		g.setColor(particionesEstFijas.getParticiones()[i].getProceso().getColor());
            		g.fillRect(pos, 0, tamProceso, getHeight());
            	}
            	
            	g.setColor(negro);			//Pintar borde
            	g.drawRect(pos, 0, tamDibujo, getHeight()-1);
            	
            	if(particionesEstFijas.getParticiones()[i].getDisponible() == false) {
                	g.drawString(particionesEstFijas.getParticiones()[i].getProceso().getNombre(), pos+5, (getHeight()*2)/5);
                	g.drawString("PID=" + particionesEstFijas.getParticiones()[i].getProceso().getPID(), pos+5, ((getHeight()*4)/5));
                	//g.drawString("T: " + partcionesEstFijas.getParticiones()[0].getTamano() + "KB", 5, (getHeight()*3)/5);
            	}
            	pos = pos + tamDibujo;		//Se suma el area dibujada a la posiscion
        	}
    	}
    	
    	//Modelo Particiones Estaticas Fijas
    	if(modelo == 2) {
        	
        	int pos = 0; 	//Llava la posicion de donde se va a dibujar
        	g.setFont(new Font("Tahoma", Font.BOLD, 7));		//Tamaño del texto en el dibujo
        	
        	//Calculando tamaño del S.O. para ser dibujada
        	double tamanoSO = (particionesEstVariables.getSO().getTamano()*100.0)/particionesEstVariables.getMemoriaPpal();
        	int drawSO = (int) (getWidth()*(tamanoSO/100));

        	g.setColor(amarillo);
        	g.fillRect(pos, 0, drawSO, getHeight());
        	g.setColor(negro);
        	g.drawRect(pos, 0, drawSO, getHeight()-1);
        	
        	g.drawString(particionesEstVariables.getParticiones()[0].getProceso().getNombre(), 5, (getHeight()*2)/5);
        	g.drawString("PID=" + particionesEstVariables.getParticiones()[0].getProceso().getPID(), 5, ((getHeight()*4)/5));
        	//g.drawString("T: " + partcionesEstFijas.getParticiones()[0].getTamano() + "KB", 5, (getHeight()*3)/5);
        	
        	pos = pos + drawSO;
        	
        	//Recorrer el arreglo de particiones para ir dibujando cada una
        	for(int i=1; i<particionesEstVariables.getParticiones().length;i++) {
        		
        		int tamDibujo = cacularTamDibujo(i);
        		int tamProceso = 0;
        		
        		g.setColor(verde);
        		
        		//Verificar si hay proceso en la particion, si lo hay calcular el tamaño
        		if(particionesEstVariables.getParticiones()[i].getDisponible() == true)
        			tamProceso = 0;	
        		else
        			tamProceso = cacularTamProceso(i);

            	g.fillRect(pos, 0, tamDibujo, getHeight());	//Pintar particion
            	
            	if (tamProceso != 0) {		//Si la particion tiene proceso pintar proceso
            		g.setColor(particionesEstVariables.getParticiones()[i].getProceso().getColor());
            		g.fillRect(pos, 0, tamProceso, getHeight());
            	}
            	
            	g.setColor(negro);			//Pintar borde
            	g.drawRect(pos, 0, tamDibujo, getHeight()-1);
            	
            	if(particionesEstVariables.getParticiones()[i].getDisponible() == false) {
                	g.drawString(particionesEstVariables.getParticiones()[i].getProceso().getNombre(), pos+5, (getHeight()*2)/5);
                	g.drawString("PID=" + particionesEstVariables.getParticiones()[i].getProceso().getPID(), pos+5, ((getHeight()*4)/5));
                	//g.drawString("T: " + partcionesEstFijas.getParticiones()[0].getTamano() + "KB", 5, (getHeight()*3)/5);
            	}
            	pos = pos + tamDibujo;		//Se suma el area dibujada a la posiscion
        	}
    	}
    	
    	//Modelo Particiones Dinamicas
    	if(modelo == 3) {
        	
        	int pos = 0; 	//Llava la posicion de donde se va a dibujar
        	g.setFont(new Font("Tahoma", Font.BOLD, 7));		//Tamaño del texto en el dibujo
        	
        	//Calculando tamaño del S.O. para ser dibujada
        	double tamanoSO = (particionesDinamicas.getSO().getTamano()*100.0)/particionesDinamicas.getMemoriaPpal();
        	int drawSO = (int) (getWidth()*(tamanoSO/100));

        	g.setColor(amarillo);
        	g.fillRect(pos, 0, drawSO, getHeight());
        	g.setColor(negro);
        	g.drawRect(pos, 0, drawSO, getHeight()-1);
        	
        	g.drawString(particionesDinamicas.getParticiones()[0].getProceso().getNombre(), 5, (getHeight()*2)/5);
        	g.drawString("PID=" + particionesDinamicas.getParticiones()[0].getProceso().getPID(), 5, ((getHeight()*4)/5));
        	//g.drawString("T: " + partcionesEstFijas.getParticiones()[0].getTamano() + "KB", 5, (getHeight()*3)/5);
        	
        	pos = pos + drawSO;
        	
        	//Recorrer el arreglo de particiones para ir dibujando cada una
        	for(int i=1; i<particionesDinamicas.getParticiones().length;i++) {
        		
        		int tamDibujo = cacularTamDibujo(i);
        		int tamProceso = 0;
        		
        		g.setColor(verde);
        		
        		//Verificar si hay proceso en la particion, si lo hay calcular el tamaño
        		if(particionesDinamicas.getParticiones()[i].getDisponible() == true)
        			tamProceso = 0;	
        		else
        			tamProceso = cacularTamProceso(i);

            	g.fillRect(pos, 0, tamDibujo, getHeight());	//Pintar particion
            	
            	if (tamProceso != 0) {		//Si la particion tiene proceso pintar proceso
            		g.setColor(particionesDinamicas.getParticiones()[i].getProceso().getColor());
            		g.fillRect(pos, 0, tamProceso, getHeight());
            	}
            	
            	g.setColor(negro);			//Pintar borde
            	g.drawRect(pos, 0, tamDibujo, getHeight()-1);
            	
            	if(particionesDinamicas.getParticiones()[i].getDisponible() == false) {
                	g.drawString(particionesDinamicas.getParticiones()[i].getProceso().getNombre(), pos+5, (getHeight()*2)/5);
                	g.drawString("PID=" + particionesDinamicas.getParticiones()[i].getProceso().getPID(), pos+5, ((getHeight()*4)/5));
                	//g.drawString("T: " + partcionesEstFijas.getParticiones()[0].getTamano() + "KB", 5, (getHeight()*3)/5);
            	}
            	pos = pos + tamDibujo;		//Se suma el area dibujada a la posiscion
        	}
    	}
    	
    	//Modelo Paginacion
    	if(modelo == 4) {
        	
        	int pos = 0; 	//Llava la posicion de donde se va a dibujar
        	g.setFont(new Font("Tahoma", Font.BOLD, 7));		//Tamaño del texto en el dibujo
        	
        	//Calculando tamaño del S.O. para ser dibujada
        	double tamanoSO = (paginacion.getSO().getTamano()*100.0)/paginacion.getMemoriaPpal();
        	int drawSO = (int) (getWidth()*(tamanoSO/100));

        	g.setColor(amarillo);
        	g.fillRect(pos, 0, drawSO, getHeight());
        	g.setColor(negro);
        	g.drawRect(pos, 0, drawSO, getHeight()-1);
        	
        	g.drawString(paginacion.getParticiones()[0].getProceso().getNombre(), 5, (getHeight()*2)/5);
        	g.drawString("PID=" + paginacion.getParticiones()[0].getProceso().getPID(), 5, ((getHeight()*4)/5));
        	//g.drawString("T: " + partcionesEstFijas.getParticiones()[0].getTamano() + "KB", 5, (getHeight()*3)/5);
        	
        	pos = pos + drawSO;
        	
        	//Recorrer el arreglo de particiones para ir dibujando cada una
        	for(int i=1; i<paginacion.getParticiones().length;i++) {
        		
        		int tamDibujo = cacularTamDibujo(i);
        		int tamProceso = 0;
  
        		g.setColor(verde);
        		
        		//Verificar si hay proceso en la particion, si lo hay calcular el tamaño
        		if(paginacion.getParticiones()[i].getDisponible() == true)
        			tamProceso = 0;	
        		else
        			tamProceso = cacularTamProceso(i);

            	g.fillRect(pos, 0, tamDibujo, getHeight());	//Pintar particion
            	
            	if (tamProceso != 0) {		//Si la particion tiene proceso pintar proceso
            		g.setColor(paginacion.getParticiones()[i].getProceso().getColor());
            		g.fillRect(pos, 0, tamProceso, getHeight());
            	}
            	
            	g.setColor(negro);			//Pintar borde
            	g.drawRect(pos, 0, tamDibujo, getHeight()-1);
            	
            	if(paginacion.getParticiones()[i].getDisponible() == false) {
                	g.drawString(paginacion.getParticiones()[i].getProceso().getNombre(), pos+5, (getHeight()*2)/5);
                	g.drawString("PID=" + paginacion.getParticiones()[i].getProceso().getPID(), pos+5, ((getHeight()*4)/5));
                	//g.drawString("T: " + partcionesEstFijas.getParticiones()[0].getTamano() + "KB", 5, (getHeight()*3)/5);
            	}
            	pos = pos + tamDibujo;		//Se suma el area dibujada a la posiscion
        	}
    	}
    	
    	//Modelo Segmentacion
    	if(modelo == 5) {
        	
        	int pos = 0; 	//Llava la posicion de donde se va a dibujar
        	g.setFont(new Font("Tahoma", Font.BOLD, 7));		//Tamaño del texto en el dibujo
        	
        	//Calculando tamaño del S.O. para ser dibujada
        	double tamanoSO = (segmentacion.getSO().getTamano()*100.0)/segmentacion.getMemoriaPpal();
        	int drawSO = (int) (getWidth()*(tamanoSO/100));

        	g.setColor(amarillo);
        	g.fillRect(pos, 0, drawSO, getHeight());
        	g.setColor(negro);
        	g.drawRect(pos, 0, drawSO, getHeight()-1);
        	
        	g.drawString(segmentacion.getParticiones()[0].getProceso().getNombre(), 5, (getHeight()*2)/5);
        	g.drawString("PID=" + segmentacion.getParticiones()[0].getProceso().getPID(), 5, ((getHeight()*4)/5));
        	
        	pos = pos + drawSO;
        	
        	//Recorrer el arreglo de particiones para ir dibujando cada una
        	for(int i=1; i<segmentacion.getParticiones().length;i++) {
        		
        		int tamDibujo = cacularTamDibujo(i);
        		int tamProceso = 0;
  
        		g.setColor(verde);
        		
        		//Verificar si hay proceso en la particion, si lo hay calcular el tamaño
        		if(segmentacion.getParticiones()[i].getDisponible() == true)
        			tamProceso = 0;	
        		else
        			tamProceso = cacularTamProceso(i);

            	g.fillRect(pos, 0, tamDibujo, getHeight());	//Pintar particion
            	
            	if (tamProceso != 0) {		//Si la particion tiene proceso pintar proceso segun el segmento
            		if(segmentacion.getParticiones()[i].isCodigo())
            			g.setColor( (Color)segmentacion.getParticiones()[i].getProceso().getSegCodigo()[2]);
            		
            		if(segmentacion.getParticiones()[i].isDatos())
            			g.setColor( (Color)segmentacion.getParticiones()[i].getProceso().getSegDatos()[2]);
            		
            		if(segmentacion.getParticiones()[i].isPila())
            			g.setColor( (Color)segmentacion.getParticiones()[i].getProceso().getSegPila()[2]);
            		
            		g.fillRect(pos, 0, tamProceso, getHeight());
            	}
            	
            	g.setColor(negro);			//Pintar borde
            	g.drawRect(pos, 0, tamDibujo, getHeight()-1);
            	
            	if(segmentacion.getParticiones()[i].getDisponible() == false) {
                	g.drawString(segmentacion.getParticiones()[i].getProceso().getNombre(), pos+5, (getHeight()*2)/5);
                	g.drawString("PID=" + segmentacion.getParticiones()[i].getProceso().getPID(), pos+5, ((getHeight()*4)/5));
                	//g.drawString("T: " + partcionesEstFijas.getParticiones()[0].getTamano() + "KB", 5, (getHeight()*3)/5);
            	}
            	pos = pos + tamDibujo;		//Se suma el area dibujada a la posiscion
        	}
    	}
    	
    }

    //Inicia la valriable del modelo
    public void inicialModelo() {
    	if(modelo == 1)
    		particionesEstFijas = new ParticionesEstFijas(asignacion);
    	else if(modelo == 2)
    		particionesEstVariables = new ParticionesEstVariables(asignacion);
    	else if(modelo == 3)
    		particionesDinamicas = new ParticionesDinamicas(asignacion);
    	else if(modelo == 4)
    		paginacion = new Paginacion();
    	else if(modelo == 5)
    		segmentacion = new Segmentacion();
    }
    
    public void iniciarDibujoMemLibre() {
    	
    	if(modelo == 1)
    		dibujoMemLibre = new PanelDibujoMem(particionesEstFijas.getParticiones(), particionesEstFijas.getMemoriaPpal());
    	else if(modelo == 2)
    		dibujoMemLibre = new PanelDibujoMem(particionesEstVariables.getParticiones(), particionesEstVariables.getMemoriaPpal());
    	else if(modelo == 3)
    		dibujoMemLibre = new PanelDibujoMem(particionesDinamicas.getParticiones(), particionesDinamicas.getMemoriaPpal());
    	else if(modelo == 4)
    		dibujoMemLibre = new PanelDibujoMem(paginacion.getParticiones(),paginacion.getMemoriaPpal());
    	else if(modelo == 5)
    		dibujoMemLibre = new PanelDibujoMem(segmentacion.getParticiones(),segmentacion.getMemoriaPpal());
    		
    }
    
    /**
     * Calcula el tamaño de la particion a dibujar
     * @param posicion
     * @return tamaño del recuadro a dibujar
     */
    public int cacularTamDibujo(int posicion) {
    	if(modelo == 1) {
    		double tamano = (particionesEstFijas.getParticiones()[posicion].getTamano()*100.0)/particionesEstFijas.getMemoriaPpal();
        	int draw = (int) (getWidth()*(tamano/100));
    		return draw;
    	}else if(modelo == 2) {
    		double tamano = (particionesEstVariables.getParticiones()[posicion].getTamano()*100.0)/particionesEstVariables.getMemoriaPpal();
        	int draw = (int) (getWidth()*(tamano/100));
    		return draw;
    	}else if(modelo == 3) {
    		double tamano = (particionesDinamicas.getParticiones()[posicion].getTamano()*100.0)/particionesDinamicas.getMemoriaPpal();
        	int draw = (int) (getWidth()*(tamano/100));
    		return draw;
    	}else if(modelo == 4) {
    		double tamano = (paginacion.getParticiones()[posicion].getTamano()*100.0)/paginacion.getMemoriaPpal();
        	int draw = (int) (getWidth()*(tamano/100));
    		return draw;
    	}else if(modelo == 5) {
    		double tamano = (segmentacion.getParticiones()[posicion].getTamano()*100.0)/segmentacion.getMemoriaPpal();
        	int draw = (int) (getWidth()*(tamano/100));
    		return draw;
    	}else {
    		return 0;
    	}

    }

    /**
     * Calcula el tamaño del proceso
     * @param posicion
     * @return tamProceso
     */
    public int cacularTamProceso(int posicion) {
    	if(modelo == 1) {
    		double tamProceso = (particionesEstFijas.getParticiones()[posicion].getProceso().getTamano()*100.0)/particionesEstFijas.getMemoriaPpal();
        	int draw = (int) (getWidth()*(tamProceso/100));
    		return draw;
    	}else if(modelo == 2) {
    		double tamProceso = (particionesEstVariables.getParticiones()[posicion].getProceso().getTamano()*100.0)/particionesEstVariables.getMemoriaPpal();
        	int draw = (int) (getWidth()*(tamProceso/100));
    		return draw;
    	}else if(modelo == 3) {
    		double tamProceso = (particionesDinamicas.getParticiones()[posicion].getProceso().getTamano()*100.0)/particionesDinamicas.getMemoriaPpal();
        	int draw = (int) (getWidth()*(tamProceso/100));
    		return draw;
    	}else if(modelo == 4) {
    		//Para este modelo se toma el tamaño de la particion no del proceso
    		double tamProceso = (paginacion.getParticiones()[posicion].getTamano()*100.0)/paginacion.getMemoriaPpal();
        	int draw = (int) (getWidth()*(tamProceso/100));
    		return draw;
    	}else if(modelo == 5) {
    		//Para este modelo se toma el tamaño de la particion no del proceso
    		double tamProceso = (segmentacion.getParticiones()[posicion].getTamano()*100.0)/segmentacion.getMemoriaPpal();
        	int draw = (int) (getWidth()*(tamProceso/100));
    		return draw;
    	}else {
    		return 0;
    	}
    	
    }
    
    //Muestra el tooltip con la informacion de la particion
	public String getToolTipText(MouseEvent e) {
		String texto= "";
		//Si el area sobre la que esta el mouse no es el area de dibujo returna null
		if(getMousePosition() != null) {	
			
			int inicioPar = 0;
			int finPar = 0;
			int cont = -1;
			int posXMouse = this.getMousePosition().x;
			
			if(modelo == 1) {
				
				do{
					cont++;
					//el area de dibujo puede tener algunos pixeles mas que el area dibujada asi que si el 
					//contador se pasa lo regresa para evitar error de posicion que no existe
					if(cont >= particionesEstFijas.getParticiones().length)
						cont--;

					//Si la posicion es la 0 el inicio es 0
					if(cont <= 0) {
						inicioPar = 0;
						finPar = (inicioPar + cacularTamDibujo(cont));
					}else {
						inicioPar = (finPar);
						finPar = (inicioPar + cacularTamDibujo(cont));
					}
				}while( posXMouse < inicioPar ||  posXMouse > finPar);
				
				texto = "<html>";
				
				if(particionesEstFijas.getParticiones()[cont].getDisponible()==false) {
					texto = texto + "<b>Proceso:</b> " + particionesEstFijas.getParticiones()[cont].getProceso().getNombre();
					texto = texto + "<br/><b>PID:</b> " + particionesEstFijas.getParticiones()[cont].getProceso().getPID();
					texto = texto + "<br/><b>Tamaño Particion:</b> " + particionesEstFijas.getParticiones()[cont].getTamano() + "KB";
					texto = texto + "<br/><b>Ocupado:</b> " + particionesEstFijas.getParticiones()[cont].getProceso().getTamano()  + " KB";
					UIManager.put("ToolTip.background", particionesEstFijas.getParticiones()[cont].getProceso().getColor());
				}else {
					texto =  texto + "<b>Libre</b>";
					texto = texto + "<br/><b>Tamaño Particion:</b> " + particionesEstFijas.getParticiones()[cont].getTamano() + "KB";
					UIManager.put("ToolTip.background", verde);
				}
				texto = texto + "<br /><b>Inicio:</b> " + particionesEstFijas.getParticiones()[cont].getInicio() + " KB<br/><b>Fin: </b>"
						+ (particionesEstFijas.getParticiones()[cont].getInicio() + particionesEstFijas.getParticiones()[cont].getTamano() - 1) + " KB</html>";
				
	    	}else if(modelo == 2) {
	    		
	    		do{
					cont++;
					//el area de dibujo puede tener algunos pixeles mas que el area dibujada asi que si el 
					//contador se pasa lo regresa para evitar error de posicion que no existe
					if(cont >= particionesEstVariables.getParticiones().length)
						cont--;

					//Si la posicion es la 0 el inicio es 0
					if(cont <= 0) {
						inicioPar = 0;
						finPar = (inicioPar + cacularTamDibujo(cont));
					}else {
						inicioPar = (finPar);
						finPar = (inicioPar + cacularTamDibujo(cont));
					}
				}while( posXMouse < inicioPar ||  posXMouse > finPar);
				
				texto = "<html>";
				
				if(particionesEstVariables.getParticiones()[cont].getDisponible()==false) {
					texto = texto + "<b>Proceso:</b> " + particionesEstVariables.getParticiones()[cont].getProceso().getNombre();
					texto = texto + "<br/><b>PID:</b> " + particionesEstVariables.getParticiones()[cont].getProceso().getPID();
					texto = texto + "<br/><b>Tamaño Particion:</b> " + particionesEstVariables.getParticiones()[cont].getTamano() + "KB";
					texto = texto + "<br/><b>Ocupado:</b> " + particionesEstVariables.getParticiones()[cont].getProceso().getTamano()  + "KB";
					UIManager.put("ToolTip.background", particionesEstVariables.getParticiones()[cont].getProceso().getColor());
				}else {
					texto =  texto + "<b>Libre</b>";
					texto = texto + "<br/><b>Tamaño Particion:</b> " + particionesEstVariables.getParticiones()[cont].getTamano() + "KB";
					UIManager.put("ToolTip.background", verde);
				}
				texto = texto + "<br /><b>Inicio:</b> " + particionesEstVariables.getParticiones()[cont].getInicio() + "KB<br/><b>Fin:</b> "
						+ (particionesEstVariables.getParticiones()[cont].getInicio() + particionesEstVariables.getParticiones()[cont].getTamano() - 1) + "KB</html>";
	    		
	    	}else if(modelo == 3) {
	    		
	    		do{
					cont++;
					//el area de dibujo puede tener algunos pixeles mas que el area dibujada asi que si el 
					//contador se pasa lo regresa para evitar error de posicion que no existe
					if(cont >= particionesDinamicas.getParticiones().length)
						cont--;

					//Si la posicion es la 0 el inicio es 0
					if(cont <= 0) {
						inicioPar = 0;
						finPar = (inicioPar + cacularTamDibujo(cont));
					}else {
						inicioPar = (finPar);
						finPar = (inicioPar + cacularTamDibujo(cont));
					}
				}while( posXMouse < inicioPar ||  posXMouse > finPar);
	    		
				texto = "<html>";
				
				if(particionesDinamicas.getParticiones()[cont].getDisponible()==false) {
					texto = texto + "<b>Proceso:</b> " + particionesDinamicas.getParticiones()[cont].getProceso().getNombre();
					texto = texto + "<br/><b>PID:</b> " + particionesDinamicas.getParticiones()[cont].getProceso().getPID();
					texto = texto + "<br/><b>Tamaño Particion:</b> " + particionesDinamicas.getParticiones()[cont].getTamano() + "KB";
					texto = texto + "<br/><b>Ocupado:</b> " + particionesDinamicas.getParticiones()[cont].getProceso().getTamano() + " KB";
					UIManager.put("ToolTip.background", particionesDinamicas.getParticiones()[cont].getProceso().getColor());
				}else {
					texto =  texto + "<b>Libre</b>";
					UIManager.put("ToolTip.background", verde);
					texto = texto + "<br/><b>Tamaño Particion:</b> " + particionesDinamicas.getParticiones()[cont].getTamano() + "KB";
				}
				texto = texto + "<br/><b>Inicio:</b> " + particionesDinamicas.getParticiones()[cont].getInicio() + " KB<br/><b>Fin:</b> "
						+ (particionesDinamicas.getParticiones()[cont].getInicio() + particionesDinamicas.getParticiones()[cont].getTamano() - 1) + " KB</html>";
				
	    	}else if(modelo == 4) {
	    		
				do{
					cont++;
					//el area de dibujo puede tener algunos pixeles mas que el area dibujada asi que si el 
					//contador se pasa lo regresa para evitar error de posicion que no existe
					if(cont >= paginacion.getParticiones().length)
						cont--;

					//Si la posicion es la 0 el inicio es 0
					if(cont <= 0) {
						inicioPar = 0;
						finPar = (inicioPar + cacularTamDibujo(cont));
					}else {
						inicioPar = (finPar);
						finPar = (inicioPar + cacularTamDibujo(cont));
					}
				}while( posXMouse < inicioPar ||  posXMouse > finPar);
				
				texto = "<html>";
				
				if(paginacion.getParticiones()[cont].getDisponible()==false) {
					texto = texto + "<b>Proceso:</b> " + paginacion.getParticiones()[cont].getProceso().getNombre();
					texto = texto + "<br/><b>PID:</b> " + paginacion.getParticiones()[cont].getProceso().getPID();
					texto = texto + "<br/><b>Tamaño Particion:</b> " + paginacion.getParticiones()[cont].getTamano() + "KB";
					texto = texto + "<br/><b>Ocupado:</b> " + paginacion.getParticiones()[cont].getProceso().getTamano()  + " KB";
					UIManager.put("ToolTip.background", paginacion.getParticiones()[cont].getProceso().getColor());
				}else {
					texto =  texto + "<b>Libre</b>";
					texto = texto + "<br/><b>Tamaño Particion:</b> " + paginacion.getParticiones()[cont].getTamano() + "KB";
					UIManager.put("ToolTip.background", verde);
				}
				texto = texto + "<br /><b>Inicio:</b> " + paginacion.getParticiones()[cont].getInicio() + " KB<br/><b>Fin: </b>"
						+ (paginacion.getParticiones()[cont].getInicio() + paginacion.getParticiones()[cont].getTamano() - 1) + " KB</html>";
	    	
	    	}else if(modelo == 5) {
	    		
				do{
					cont++;
					//el area de dibujo puede tener algunos pixeles mas que el area dibujada asi que si el 
					//contador se pasa lo regresa para evitar error de posicion que no existe
					if(cont >= segmentacion.getParticiones().length)
						cont--;

					//Si la posicion es la 0 el inicio es 0
					if(cont <= 0) {
						inicioPar = 0;
						finPar = (inicioPar + cacularTamDibujo(cont));
					}else {
						inicioPar = (finPar);
						finPar = (inicioPar + cacularTamDibujo(cont));
					}
				}while( posXMouse < inicioPar ||  posXMouse > finPar);
				
				texto = "<html>";
				
				if(segmentacion.getParticiones()[cont].getDisponible()==false) {
					texto = texto + "<b>Proceso:</b> " + segmentacion.getParticiones()[cont].getProceso().getNombre();
					texto = texto + "<br/><b>PID:</b> " + segmentacion.getParticiones()[cont].getProceso().getPID();
					texto = texto + "<br/><b>Tamaño Particion:</b> " + segmentacion.getParticiones()[cont].getTamano() + "KB";
					String segmento = "";
					if(segmentacion.getParticiones()[cont].isCodigo())
						segmento = "Codigo";
					else if(segmentacion.getParticiones()[cont].isDatos())
						segmento = "Datos";
					else if(segmentacion.getParticiones()[cont].isPila())
						segmento = "Pila";
					
					texto = texto + "<br/><b>Segmento:</b> " + segmento;
					
					UIManager.put("ToolTip.background", segmentacion.getParticiones()[cont].getProceso().getColor());
				}else {
					texto =  texto + "<b>Libre</b>";
					texto = texto + "<br/><b>Tamaño Particion:</b> " + segmentacion.getParticiones()[cont].getTamano() + "KB";
					UIManager.put("ToolTip.background", verde);
				}
				texto = texto + "<br /><b>Inicio:</b> " + segmentacion.getParticiones()[cont].getInicio() + " KB<br/><b>Fin: </b>"
						+ (segmentacion.getParticiones()[cont].getInicio() + segmentacion.getParticiones()[cont].getTamano() - 1) + " KB</html>";
	    	}else {
	    		return "";
	    	}
			 
		}
		return texto;
    }
	
	//Ubicacion del TooltopText
	public Point getToolTipLocation(MouseEvent e){
		Point p = e.getPoint();
		p.y += 15;
		return p;
		//return super.getToolTipLocation(e);
	 }
    
	public ParticionesEstFijas getParticionesEstFijas() {
		return particionesEstFijas;
	}
	public void setParticionesEstFijas(ParticionesEstFijas particionesEstFijas) {
		this.particionesEstFijas = particionesEstFijas;
	}

	public ParticionesEstVariables getParticionesEstVariables() {
		return particionesEstVariables;
	}

	public void setParticionesEstVariables(ParticionesEstVariables particionesEstVariables) {
		this.particionesEstVariables = particionesEstVariables;
	}
	
	public ParticionesDinamicas getParticionesDinamicas() {
		return particionesDinamicas;
	}

	public void setParticionesDinamicas(ParticionesDinamicas particionesDinamicas) {
		this.particionesDinamicas = particionesDinamicas;
	}

	public Paginacion getPaginacion() {
		return paginacion;
	}

	public void setPaginacion(Paginacion paginacion) {
		this.paginacion = paginacion;
	}

	public Segmentacion getSegmentacion() {
		return segmentacion;
	}

	public void setSegmentacion(Segmentacion segmentacion) {
		this.segmentacion = segmentacion;
	}

	public PanelDibujoMem getDibujoMemLibre() {
		return dibujoMemLibre;
	}

	public void setDibujoMemLibre(PanelDibujoMem dibujoMemLibre) {
		this.dibujoMemLibre = dibujoMemLibre;
	}

}
