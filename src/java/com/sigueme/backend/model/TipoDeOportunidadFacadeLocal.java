/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.TipoDeOportunidad;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Santi
 */
@Local
public interface TipoDeOportunidadFacadeLocal {

    void create(TipoDeOportunidad tipoDeOportunidad);

    void edit(TipoDeOportunidad tipoDeOportunidad);

    void remove(TipoDeOportunidad tipoDeOportunidad);

    TipoDeOportunidad find(Object id);

    List<TipoDeOportunidad> findAll();

    List<TipoDeOportunidad> findRange(int[] range);

    int count();
    
}
