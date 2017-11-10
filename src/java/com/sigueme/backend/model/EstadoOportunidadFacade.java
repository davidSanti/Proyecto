/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.EstadoOportunidad;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author Santi
 */
@Stateless
public class EstadoOportunidadFacade extends AbstractFacade<EstadoOportunidad> implements EstadoOportunidadFacadeLocal {

    @PersistenceContext(unitName = "SiguemeProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstadoOportunidadFacade() {
        super(EstadoOportunidad.class);
    }

    @Override
    public List<EstadoOportunidad> listarEstados() {
        try {
            TypedQuery<EstadoOportunidad> query = getEntityManager().createNamedQuery("EstadoOportunidad.findAll",EstadoOportunidad.class);
            return query.getResultList();
        } catch (Exception e) {
            throw e;
        }
    }

}
