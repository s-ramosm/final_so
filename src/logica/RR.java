/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import java.util.ArrayList;

/**
 *
 * @author wramos
 */
public class RR implements gestor{
    boolean despacho = false;
    int tiempo_ejecución;
    int index = 0;
    Proceso dispacher;
    
    int global_quantum = 4;
    
    int quantum = global_quantum;
    
    
    @Override
    public void setTiempo_ejecución(int tiempo_ejecución) {
        this.tiempo_ejecución = tiempo_ejecución;//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cambiar_estado(String estado, Proceso pr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void validar_procesos(ArrayList<Proceso> pr) {
        for (Proceso pro : pr ){
            
            if (pro.estado == "esperando"){
                pro.tiempo_esperando = pro.tiempo_esperando +1;
            }
            
            if (pro.estado == "por entrar" && this.tiempo_ejecución == pro.llegada ){
                pro.estado = "esperando";
                pro.tiempo_esperando = pro.tiempo_esperando +1;
            }
            
            if (pro.estado == "bloqueado"){
                    
                    if (pro.duración_interrupcion>0){
                        pro.duración_interrupcion = pro.duración_interrupcion - 1;
                    }
                    else{
                        pro.estado = "esperando";
                        pro.tiempo_esperando = pro.tiempo_esperando +1;
                    }
                    
            }
            
            if (pro.estado == "ejecución"){
                
                if (pro.interrupcion == pro.tiempo_ejecutandose){
                    pro.estado = "bloqueado";
                    
                    pro.duración_interrupcion = pro.duración_interrupcion -1;
                }
 
                else if (pro.tiempo_ejecutandose == pro.getTamano()){
                    pro.estado = "terminado";
                } else if(quantum ==0) {
                    pro.estado = "esperando";
                }
                else{
                    pro.tiempo_ejecutandose = pro.tiempo_ejecutandose +1;
                }
            }
            
            
        }
        quantum = quantum - 1;
        boolean cambio = true;
        for (Proceso pro : pr ){
            if (pro.estado == "ejecución"){
                cambio = false;
                break;
            }
        }
        
        if (cambio){
           this.escorger_ejecución(pr); 
        }
        
        
        //To change body of generated methods, choose Tools | Templates.
    }

    public void asignacion_ejecucion(int index,ArrayList<Proceso> pr ){
        System.out.println(index%pr.size() );
        Proceso pro = pr.get( index%pr.size() );
        if (pro.estado != "bloqueado" && pro.estado != "terminado" && pro.estado == "esperando"){
            pro.estado = "ejecución";
            pro.tiempo_ejecutandose = pro.tiempo_ejecutandose + 1;
            this.index = this.index +1;
        }else{
           this.index = this.index +1;
           this.asignacion_ejecucion(this.index, pr);
        }
    }
    
    @Override
    public int escorger_ejecución(ArrayList<Proceso> pr) {
        
        if (this.dispacher == null ){
            dispacher = new Proceso("Dispacher", 0);
            dispacher.cordy= pr.get(pr.size()-1).cordy + 25;
            dispacher.estado  = "despachador";
        }
        
        if (!despacho){
            pr.add(dispacher);
            quantum= global_quantum;
            despacho = true;
            return 0;
        }
        else{
            despacho = false;
            quantum = quantum - 1;
            this.asignacion_ejecucion(index, pr);
            
        }
        return 0;
    }
    
}
