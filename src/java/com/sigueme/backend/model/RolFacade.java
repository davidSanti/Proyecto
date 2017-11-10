/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Rol;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author Santi
 */
@Stateless
public class RolFacade extends AbstractFacade<Rol> implements RolFacadeLocal {

    @PersistenceContext(unitName = "SiguemeProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RolFacade() {
        super(Rol.class);
    }

    @Override
    public List<Rol> listarRoles() {
        try{
            TypedQuery<Rol> query = getEntityManager().createNamedQuery("Rol.findAll", Rol.class);
            return query.getResultList();
        } catch(Exception e) {
            throw e;
        }
        
    }
    
    @Override
    public void eliminarPermisos(Rol rol){
        try {
            Query query = em.createNativeQuery("DELETE FROM PermisosRol WHERE RolID = ?1");
            query.setParameter(1, rol.getRolID());
            query.executeUpdate();
        } catch(Exception e) {
            throw e;
        }
    }
    
}
