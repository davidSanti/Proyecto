/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.TipoDeOportunidad;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Santi
 */
@Stateless
public class TipoDeOportunidadFacade extends AbstractFacade<TipoDeOportunidad> implements TipoDeOportunidadFacadeLocal {

    @PersistenceContext(unitName = "SiguemeProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoDeOportunidadFacade() {
        super(TipoDeOportunidad.class);
    }
    
}
