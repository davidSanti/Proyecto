/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.ErrorFrecuente;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Santi
 */
@Stateless
public class ErrorFrecuenteFacade extends AbstractFacade<ErrorFrecuente> implements ErrorFrecuenteFacadeLocal {

    @PersistenceContext(unitName = "SiguemeProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ErrorFrecuenteFacade() {
        super(ErrorFrecuente.class);
    }
    
    @Override
    public List<ErrorFrecuente> listar() {
        try {
            TypedQuery<ErrorFrecuente> query = em.createNamedQuery("ErrorFrecuente.findAll", ErrorFrecuente.class);
            return  query.getResultList();
        } catch (Exception e) {
            throw  e;
        }
    }
}
