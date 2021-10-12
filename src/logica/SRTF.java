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
public class SRTF implements gestor{

    int tiempo_ejecución;
    
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
                    }
                    
            }
            
            if (pro.estado == "ejecución"){
                
                if (pro.interrupcion == pro.tiempo_ejecutandose){
                    pro.estado = "bloqueado";
                    
                    pro.duración_interrupcion = pro.duración_interrupcion -1;
                }
                else if (pro.tiempo_ejecutandose == pro.getTamano()){
                    pro.estado = "terminado";
                }
                else{
                    pro.tiempo_ejecutandose = pro.tiempo_ejecutandose +1;
                }
            }
            
            
        }
        boolean cambio = true;
        for (Proceso pro : pr ){
            if (pro.estado == "ejecución"){
                cambio = false;
                break;
            }
        }
        
        if (cambio){
           this.escorger_ejecución(pr); 
        } //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int escorger_ejecución(ArrayList<Proceso> pr) {
        ArrayList<Proceso> temp_array = new ArrayList<Proceso>();
        for (Proceso pro : pr ){
            if (pro.estado != "bloqueado" && pro.estado != "terminado" && pro.estado == "esperando"){
                temp_array.add(pro);
            }
        }
        if (temp_array.isEmpty()){
            return 0;
        } 
        Proceso temp_pro = temp_array.get(0);
        for (Proceso pro : temp_array ){
            if (pro.estado != "bloqueado" && pro.estado != "terminado" && pro.estado == "esperando" 
                    && temp_pro.getTamano() - temp_pro.tiempo_ejecutandose > pro.getTamano() - pro.tiempo_ejecutandose){
                temp_pro = pro;
            }
        }
        temp_pro.estado = "ejecución";
        temp_pro.tiempo_ejecutandose = temp_pro.tiempo_ejecutandose + 1;
        return 0;//To change body of generated methods, choose Tools | Templates.
    }
    
}
