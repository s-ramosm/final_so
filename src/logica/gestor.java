/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package logica;

import java.awt.List;
import java.util.ArrayList;

/**
 *
 * @author wramos
 */
public interface gestor {
    
    public void setTiempo_ejecución(int tiempo_ejecución);
    public void cambiar_estado(String estado,Proceso pr);
    
    /**
     *
     * @param pr
     */
    public void validar_procesos(ArrayList<Proceso> pr);
    
    public int escorger_ejecución(ArrayList<Proceso> pr);
}
