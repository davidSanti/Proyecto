/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.FortalezaDeConocimiento;
import com.sigueme.backend.entities.OportunidadDeAprendizaje;
import com.sigueme.backend.entities.Usuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Santi
 */
@Local
public interface FortalezaDeConocimientoFacadeLocal {

    void create(FortalezaDeConocimiento fortalezaDeConocimiento);

    void edit(FortalezaDeConocimiento fortalezaDeConocimiento);

    void remove(FortalezaDeConocimiento fortalezaDeConocimiento);

    FortalezaDeConocimiento find(Object id);

    List<FortalezaDeConocimiento> findAll();

    List<FortalezaDeConocimiento> findRange(int[] range);

    int count();
    
    List<FortalezaDeConocimiento> listar();
    
    void cambiarEstado(FortalezaDeConocimiento fortaleza);
    
    List<FortalezaDeConocimiento> listarFortalezasDeAgentePorOportunidad(Usuario usuario, OportunidadDeAprendizaje oportunidad);
}
