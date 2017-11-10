/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.EstadoOportunidad;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Santi
 */
@Local
public interface EstadoOportunidadFacadeLocal {

    void create(EstadoOportunidad estadoOportunidad);

    void edit(EstadoOportunidad estadoOportunidad);

    void remove(EstadoOportunidad estadoOportunidad);

    EstadoOportunidad find(Object id);

    List<EstadoOportunidad> findAll();

    List<EstadoOportunidad> findRange(int[] range);

    int count();
    
    List<EstadoOportunidad> listarEstados();
}
