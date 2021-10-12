package interfaz;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import logica.ProcesosDisponibles;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JToggleButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;
import logica.FCFS;
import logica.Proceso;
import logica.SJF;
import logica.gestor;



class Pintar extends Thread { 
    
 JPanel jp;
 int x;
 int y;
 gestor GS;

    public void setGS(gestor GS) {
        this.GS = GS;
    }

    public void setJp(JPanel jp) {
        this.jp = jp;
    }
 ArrayList<Proceso> procesos;
 public Pintar(String str,JPanel jp, int x,int y) { 
  
  super(str); 
  this.jp = jp;
  this.x = x;
  this.y = y;
 }
 public Pintar (Vector v){
     
     this.procesos = new ArrayList<Proceso>();
     int y = 5;
     int index = 0;
     for (Object item : v){
                     
                        String nombre = (String)((Vector)item).get(0);
                        int  llegada = (int)((Vector)item).get(1);
                        int ejecucion = (int)((Vector)item).get(2);
                        int interrupcion =(int)((Vector)item).get(3);
                        int dr_interrupcion = (int)((Vector)item).get(4);        
                        Proceso pr = new Proceso(nombre,llegada,ejecucion,interrupcion,dr_interrupcion);
                        pr.cordy = y;
                        
                        y = y+12;
                        
                        this.procesos.add(pr);
                     
          }
     
 }
 
 public Color definir_color (Proceso pr){
     if (pr.estado == "esperando"){
         return Color.GRAY;
     }  
     
     if (pr.estado == "ejecución"){
         return Color.GREEN;
     }
     
     if (pr.estado == "bloqueado"){
         return Color.RED;
     }
     
     return Color.WHITE;
 }
 
 public void run() { 
    int tiempo_ejecucion = 0;
    int x = 30;
    while(true){
        
        GS.validar_procesos(procesos);
        
        for (Proceso item : this.procesos){
        
            JPanel jp = new JPanel();
            jp.setBackground(this.definir_color(item));
            jp.setBounds(x, item.cordy, 10, 10);
        
            this.jp.add(jp);
            this.jp.revalidate();
            this.jp.repaint();
            
        
        }
        x = x+10;
        tiempo_ejecucion = tiempo_ejecucion +1;
        GS.setTiempo_ejecución(tiempo_ejecucion);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Pintar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
 } 
}

@SuppressWarnings("serial")
public class JFramePrincipal extends JFrame implements ActionListener{

        private gestor GS;
	private int modelo = 1;				//Modelo seleccionado
	private int asignacion = 1;			//Algoritmos de asignacion
	
	private JPanel panelPrincipal;
	
	private JPanel panelProcesos;
	private JToggleButton tglbtnON_OFF;
	private JLabel lblProcesosDisp;
	private JScrollPane scrollLista;
	private JList<String> listaProcesos;
	private JLabel lblTLegada;
	private JSpinner spnTLlegada;
        private JSpinner spnTiempo;
	//Interrupcion
	private JCheckBox chckbxInterrupcion;
	private JLabel lblIInterrup;
	private JSpinner spnIInterrup;
	private JLabel lblTInterrup;
	private JSpinner spnTInterrup;
	
	private JPanel panelModPlanificacion;
	private ButtonGroup btgModMemoria;
	private JRadioButton rdbtnFCFS;
	private JRadioButton rdbtnSPN;
	private JRadioButton rdbtnSRTF;
	private JRadioButton rdbtnRR;
	private JRadioButton rdbtnDerPreferente;
	private JCheckBox chckbxCompactacion;
	
	private JPanel panelTabla;
	private DefaultTableModel modeloTabla;
	
	private JPanel panelMemoria;
	private JLabel lblMemoriaPrincipalpal;
	private PanelDibujoMem dibujoMemoria;
	private JLabel lblMemoriaLibre;
	private JLabel lblKB;
	private PanelDibujoProc dibujoProcesos;
	private JLabel lblInicioMem;
	private JLabel lblFinMemoria;
	private JTable tablaProcesos;
	private JScrollPane scrollTabla;
	private JLabel lblTablaProcesos;
	private String[] nombreColumna = {"Proceso", "Llegada", "T. Ejecucion", "Inicio B.", "Duracion B.", "T. Espera", "T. Bloqueo"};
	private Object[][] procesoModelo = new Object[0][7];
	

	private JButton btnAgregarProceso;


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JFramePrincipal() {
                GS = new SJF();
                
		setTitle("Simulador Gestor de Memoria");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50, 50, 910, 650);
		
		panelPrincipal = new JPanel();
		panelPrincipal.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panelPrincipal);
		panelPrincipal.setLayout(null);
		
		//Panel de seleccion de procesos
		panelProcesos = new JPanel();
		panelProcesos.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelProcesos.setBounds(10, 10, 876, 223);
		panelPrincipal.add(panelProcesos);
		panelProcesos.setLayout(null);
		
		tglbtnON_OFF = new JToggleButton();
		tglbtnON_OFF.setSelectedIcon(new ImageIcon(JFramePrincipal.class.getResource("/img/detener.png")));
		tglbtnON_OFF.setIcon(new ImageIcon(JFramePrincipal.class.getResource("/img/iniciar.png")));
		tglbtnON_OFF.setForeground(Color.WHITE);
		tglbtnON_OFF.setBounds(34, 175, 100, 30);
		tglbtnON_OFF.addActionListener(this);
		panelProcesos.add(tglbtnON_OFF);
		
		lblProcesosDisp = new JLabel("Proc. Disponibles");
		lblProcesosDisp.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblProcesosDisp.setBounds(10, 10, 142, 13);
		panelProcesos.add(lblProcesosDisp);
		
		listaProcesos = new JList(generarListaProcesos());
		listaProcesos.setSelectedIndex(0);
		listaProcesos.setFont(new Font("Tahoma", Font.PLAIN, 10));
		listaProcesos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		scrollLista = new JScrollPane();
		scrollLista.setViewportView(listaProcesos);
		scrollLista.setBounds(10, 33, 160, 123);
		panelProcesos.add(scrollLista);
		
		lblTLegada = new JLabel("Tiempo Llegada");
		lblTLegada.setBounds(247, 24, 96, 13);
		panelProcesos.add(lblTLegada);
		
		spnTLlegada = new JSpinner();
		spnTLlegada.setModel(new SpinnerNumberModel(Integer.valueOf(0), null, null, Integer.valueOf(1)));
		spnTLlegada.setToolTipText("Tiempo de llegada");
		spnTLlegada.setFont(new Font("Tahoma", Font.PLAIN, 10));
		spnTLlegada.setBounds(257, 43, 46, 19);
		panelProcesos.add(spnTLlegada);
		
                
                
                lblTLegada = new JLabel("Tiempo de ejcución");
		lblTLegada.setBounds(350, 24, 110, 13);
		panelProcesos.add(lblTLegada);
                
                spnTiempo = new JSpinner();
		spnTiempo.setModel(new SpinnerNumberModel(Integer.valueOf(0), null, null, Integer.valueOf(1)));
		spnTiempo.setToolTipText("Tiempo de ejecución");
		spnTiempo.setFont(new Font("Tahoma", Font.PLAIN, 10));
		spnTiempo.setBounds(370, 43, 46, 19);
		panelProcesos.add(spnTiempo);
		
		//Botones interrupcion
		chckbxInterrupcion = new JCheckBox("<html>Interrupci\u00F3n</html>");
		chckbxInterrupcion.setFont(new Font("Tahoma", Font.BOLD, 11));
		chckbxInterrupcion.setBounds(257, 80, 100, 19);
		panelProcesos.add(chckbxInterrupcion);
		
		lblIInterrup = new JLabel("Inicio Interrupci\u00F3n");
		lblIInterrup.setBounds(267, 105, 108, 19);
		panelProcesos.add(lblIInterrup);
		
		spnIInterrup = new JSpinner();
		spnIInterrup.setModel(new SpinnerNumberModel(Integer.valueOf(0), null, null, Integer.valueOf(1)));
		spnIInterrup.setToolTipText("Tiempo de Inicio");
		spnIInterrup.setFont(new Font("Tahoma", Font.PLAIN, 10));
		spnIInterrup.setBounds(289, 123, 46, 19);
		panelProcesos.add(spnIInterrup);
		
		lblTInterrup = new JLabel("Tiem. Interrupci\u00F3n");
		lblTInterrup.setBounds(384, 105, 125, 19);
		panelProcesos.add(lblTInterrup);
		
		spnTInterrup = new JSpinner();
		spnTInterrup.setModel(new SpinnerNumberModel(Integer.valueOf(0), null, null, Integer.valueOf(1)));
		spnTInterrup.setToolTipText("Tiempo de Fin");
		spnTInterrup.setFont(new Font("Tahoma", Font.PLAIN, 10));
		spnTInterrup.setBounds(404, 123, 46, 19);
		panelProcesos.add(spnTInterrup);
		
		
		//Panel Modelos de planificacion
		panelModPlanificacion = new JPanel();
		panelModPlanificacion.setBounds(638, 10, 213, 203);
                
		panelProcesos.add(panelModPlanificacion);
		
                
                panelModPlanificacion.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Algoritmo de Planificaci\u00F3n", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelModPlanificacion.setLayout(null);
		
		rdbtnFCFS = new JRadioButton("<html>FCFS \u2013 First come, first<br />served</html>");
		rdbtnFCFS.setSelected(true);
		rdbtnFCFS.setBounds(10, 25, 197, 29);
		panelModPlanificacion.add(rdbtnFCFS);
		
		rdbtnSPN = new JRadioButton("<html>SPN \u2013 Shorted process<br />next</html>");
		rdbtnSPN.setBounds(10, 56, 197, 29);
		panelModPlanificacion.add(rdbtnSPN);
		
		rdbtnSRTF = new JRadioButton("<html>SRTF \u2013 Shortest remaining <br />time first</html>");
		rdbtnSRTF.setBounds(10, 87, 197, 29);
		panelModPlanificacion.add(rdbtnSRTF);
			
		rdbtnRR = new JRadioButton("<html>RR \u2013 Round Robin</html>");
		rdbtnRR.setBounds(10, 118, 197, 21);
		panelModPlanificacion.add(rdbtnRR);
		
		rdbtnDerPreferente = new JRadioButton("<html>Derecho preferente</html>");
		rdbtnDerPreferente.setBounds(10, 141, 197, 21);
		panelModPlanificacion.add(rdbtnDerPreferente);
		
		chckbxCompactacion = new JCheckBox("<html>Retroalimentacion</html>");
		chckbxCompactacion.setFont(new Font("Tahoma", Font.PLAIN, 11));
		chckbxCompactacion.setBounds(35, 164, 172, 21);
		panelModPlanificacion.add(chckbxCompactacion);
		
		//Agrega botones seleccion de proceso a un grupo
		btgModMemoria = new ButtonGroup();
		btgModMemoria.add(rdbtnFCFS);
		btgModMemoria.add(rdbtnSPN);
		btgModMemoria.add(rdbtnSRTF);
		btgModMemoria.add(rdbtnRR);
		btgModMemoria.add(rdbtnDerPreferente);
		
		btnAgregarProceso = new JButton("A\u00F1adir Proceso");
		btnAgregarProceso.setBounds(304, 171, 134, 21);
		btnAgregarProceso.addActionListener(this);
		panelProcesos.add(btnAgregarProceso);
		
		panelTabla = new JPanel();
		panelTabla.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelTabla.setBounds(10, 243, 876, 156);
		panelPrincipal.add(panelTabla);
			
		//Variables para la creacion de la tabla de procesos
		modeloTabla = new DefaultTableModel(procesoModelo,  nombreColumna);
		panelTabla.setLayout(null);
		tablaProcesos = new JTable(modeloTabla);
		
		scrollTabla = new JScrollPane();
		scrollTabla.setViewportView(tablaProcesos);
		scrollTabla.setBounds(0, 0, 876, 156);
		panelTabla.add(scrollTabla);
		
		//Panel Grafico Memoria
		panelMemoria = new JPanel();
		panelMemoria.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelMemoria.setBounds(10, 413, 876, 190);
                
              
                
		panelPrincipal.add(panelMemoria);
		panelMemoria.setLayout(null);
		
                
                

               
                //panelMemoria.setBackground(Color.red);
		//dibujoProcesos();
	}
	
	//Dibujar particiones
	public void dibujoProcesos() {
		
		//Proccesos en memoria dibujo
		lblMemoriaPrincipalpal = new JLabel("Memoria Principal");
		lblMemoriaPrincipalpal.setBounds(25, 207, 126, 13);
		panelMemoria.add(lblMemoriaPrincipalpal);
		
		dibujoProcesos = new PanelDibujoProc(modelo, asignacion);
		panelMemoria.add(dibujoProcesos);
		dibujoProcesos.setBounds(25, 230, 531, 80);
		
		//Dibujo mamoria libre y ocupada
		lblMemoriaLibre = new JLabel("Memoria Ocupada");
		lblMemoriaLibre.setBounds(135, 45, 115, 13);
		lblMemoriaLibre.setVisible(true);
		panelMemoria.add(lblMemoriaLibre);
		
		lblKB = new JLabel("KB");
		lblKB.setBounds(135, 68, 65, 13);
		panelMemoria.add(lblKB);
		
		dibujoMemoria = dibujoProcesos.getDibujoMemLibre();
		dibujoMemoria.setBounds(45, 45, 80, 150);
		panelMemoria.add(dibujoMemoria);
		
		lblInicioMem = new JLabel("0 KB");
		lblInicioMem.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblInicioMem.setHorizontalAlignment(SwingConstants.CENTER);
		lblInicioMem.setBounds(10, 315, 45, 13);
		panelMemoria.add(lblInicioMem);
		
		lblFinMemoria = new JLabel("16384 KB");
		lblFinMemoria.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblFinMemoria.setHorizontalAlignment(SwingConstants.CENTER);
		lblFinMemoria.setBounds(525, 315, 45, 13);
		panelMemoria.add(lblFinMemoria);
		
		//Crea la tabla de procesos para PartEstaFijas
		if(modelo == 1 ) {
			
			tablaProcesos = new JTable(dibujoProcesos.getParticionesEstFijas().getModeloTabla());
			
			scrollTabla = new JScrollPane();
			scrollTabla.setViewportView(tablaProcesos);
			scrollTabla.setBounds(256, 67, 299, 138);
			panelMemoria.add(scrollTabla);
			
			lblTablaProcesos = new JLabel("Tabla De Particiones");
			lblTablaProcesos.setHorizontalAlignment(SwingConstants.CENTER);
			lblTablaProcesos.setBounds(260, 45, 296, 13);
			panelMemoria.add(lblTablaProcesos);
		}
		
		if(modelo == 2) {
			
			tablaProcesos = new JTable(dibujoProcesos.getParticionesEstVariables().getModeloTabla());
			
			scrollTabla = new JScrollPane();
			scrollTabla.setViewportView(tablaProcesos);
			scrollTabla.setBounds(256, 67, 299, 138);
			panelMemoria.add(scrollTabla);
			
			lblTablaProcesos = new JLabel("Tabla De Particiones");
			lblTablaProcesos.setHorizontalAlignment(SwingConstants.CENTER);
			lblTablaProcesos.setBounds(260, 45, 296, 13);
			panelMemoria.add(lblTablaProcesos);
		}
		
		if(modelo == 3) {
			
			tablaProcesos = new JTable(dibujoProcesos.getParticionesDinamicas().getModeloTabla());
			
			scrollTabla = new JScrollPane();
			scrollTabla.setViewportView(tablaProcesos);
			scrollTabla.setBounds(256, 67, 299, 138);
			panelMemoria.add(scrollTabla);
			
			lblTablaProcesos = new JLabel("Tabla De Particiones");
			lblTablaProcesos.setHorizontalAlignment(SwingConstants.CENTER);
			lblTablaProcesos.setBounds(260, 45, 296, 13);
			panelMemoria.add(lblTablaProcesos);
		}
		
		//Crea la tabla de procesos para Paginacion
		if(modelo == 4) {
			
			tablaProcesos = new JTable(dibujoProcesos.getPaginacion().getModeloTabla());
			
			scrollTabla = new JScrollPane();
			scrollTabla.setViewportView(tablaProcesos);
			scrollTabla.setBounds(256, 67, 299, 138);
			panelMemoria.add(scrollTabla);
			
			lblTablaProcesos = new JLabel("Tabla De Paginas");
			lblTablaProcesos.setHorizontalAlignment(SwingConstants.CENTER);
			lblTablaProcesos.setBounds(260, 45, 296, 13);
			panelMemoria.add(lblTablaProcesos);
		}
		
		if (modelo == 5) {
			
			tablaProcesos = new JTable(dibujoProcesos.getSegmentacion().getModeloTabla());
			
			scrollTabla = new JScrollPane();
			scrollTabla.setViewportView(tablaProcesos);
			scrollTabla.setBounds(256, 67, 299, 138);
			panelMemoria.add(scrollTabla);
			
			lblTablaProcesos = new JLabel("Tabla De Segmentos");
			lblTablaProcesos.setHorizontalAlignment(SwingConstants.CENTER);
			lblTablaProcesos.setBounds(260, 45, 296, 13);
			panelMemoria.add(lblTablaProcesos);
		
			
			spnTLlegada = new JSpinner();
			spnTLlegada.setModel(new SpinnerNumberModel(Integer.valueOf(0), null, null, Integer.valueOf(1)));
			spnTLlegada.setToolTipText("Tama\u00F1o del Segmento de Codigo (KB)");
			spnTLlegada.setFont(new Font("Tahoma", Font.PLAIN, 10));
			spnTLlegada.setBounds(45, 196, 47, 17);
			//spnCodigo.add
			panelProcesos.add(spnTLlegada);
			
		}
		
	}
	
	//Manejo eventos
	public void actionPerformed(ActionEvent event) {
		
		//Boton iniciar/detener
		if(event.getSource() == tglbtnON_OFF){
					
			JToggleButton tglbON_OFF = (JToggleButton)event.getSource();
			
            if(tglbON_OFF.isSelected()){
                int x,y;
                x = 0;
                y = 0;
                
                
                Pintar hilo = new Pintar(modeloTabla.getDataVector());
                hilo.setJp(panelMemoria);
                hilo.setGS(GS);
                hilo.start();
            	//while (true){
                //    for (Object item :  modeloTabla.getDataVector()){
                //        System.out.println(((Vector)item).get(0));
                //        JPanel jp = new JPanel();
                //        jp.setBackground(Color.BLUE);
                //        jp.setBounds(x, y, 10, 10);
                //        x = x+10;
                //        panelMemoria.add(jp);
                //        System.out.println("Label agregado");
                //        panelMemoria.revalidate();
                //        panelMemoria.repaint();
                //    }
                    
                //    y = y+10;
                //    try {
                //        Thread.sleep(1000);
                //    } catch (InterruptedException ex) {
                //        Logger.getLogger(JFramePrincipal.class.getName()).log(Level.SEVERE, null, ex);
                //    }
                    
                //}
                
            }else {
            	//habilitarDetenido();
            	this.dispose();
            	JFramePrincipal frame = new JFramePrincipal();
            	frame.setVisible(true);
            }
	
		}
		//Boton Agregar proceso
		if(event.getSource() == btnAgregarProceso) { 	
        	agregarPoceso();
		}
		
	}
	
	/**
	 * Actualiza la lista de procesos activos
	 */
	/*public void actualizarActivos() {
		//Generar lista de procesos activos a partir de las particiones
		if(modelo == 1)
			activos = generarListaActivos(dibujoProcesos.getParticionesEstFijas().getParticiones());
		else if (modelo == 2)
			activos = generarListaActivos(dibujoProcesos.getParticionesEstVariables().getParticiones());
		else if (modelo == 3)
			activos = generarListaActivos(dibujoProcesos.getParticionesDinamicas().getParticiones());
		else if (modelo == 4)
			activos = generarListaActivos(dibujoProcesos.getPaginacion().getParticiones());
		else if (modelo == 5)
			activos = generarListaActivos(dibujoProcesos.getSegmentacion().getParticiones());
	}*/
	
	/**
	 * Genera la lista de procesos disponibles
	 * @return Arreglo de String con procesos disponibles
	 */
	public String[] generarListaProcesos() {
		ProcesosDisponibles procesosDisponibles = new ProcesosDisponibles();
		
		String listaProcesos[] = new String[procesosDisponibles.getDisponibles().length];
		
		for (int i = 0; i < listaProcesos.length; i++ ) {
			listaProcesos[i] = procesosDisponibles.getDisponibles()[i].getPID() + " - " + 
							   procesosDisponibles.getDisponibles()[i].getNombre() 
							   + " - Uso CPU:"+ procesosDisponibles.getDisponibles()[i].getTamano() ;
		}
		
		return listaProcesos;
	}
	
	//Generar lista de procesos activos
	public void agregarPoceso() {
		
                ProcesosDisponibles procesosDisponibles = new ProcesosDisponibles();
		
		int seleccionado = listaProcesos.getSelectedIndex();
		procesoModelo = new Object[1][7];
		
		//Insertando en la tabla de procesos
		procesoModelo[0][0] = procesosDisponibles.getDisponibles()[seleccionado].getNombre();
		procesoModelo[0][1] = (int)spnTLlegada.getValue();
		procesoModelo[0][2] = (int)spnTiempo.getValue();//procesosDisponibles.getDisponibles()[seleccionado].getTamano();
		procesoModelo[0][3] = (int)spnIInterrup.getValue();
		procesoModelo[0][4] = (int)spnTInterrup.getValue();
		procesoModelo[0][5] = 0;
		procesoModelo[0][6] = 0;
		modeloTabla.addRow(procesoModelo[0]);
		
	}
	
	
	/**
	 * Desabilitar paneles que no deben estar activos
	 */
	public void desabilitarIniciado() {
		rdbtnFCFS.setEnabled(false);
		rdbtnSPN.setEnabled(false);
		rdbtnSRTF.setEnabled(false);
		chckbxCompactacion.setEnabled(false);
		rdbtnRR.setEnabled(false);
		rdbtnDerPreferente.setEnabled(false);
	}
	
	
	/**
	 * Habilitar paneles que deben estar activos
	 */
	public void habilitarDetenido() {
		rdbtnFCFS.setEnabled(true);
		rdbtnSPN.setEnabled(true);
		rdbtnSRTF.setEnabled(true);
		chckbxCompactacion.setEnabled(true);
		rdbtnRR.setEnabled(true);
		rdbtnDerPreferente.setEnabled(true);
	}
}
