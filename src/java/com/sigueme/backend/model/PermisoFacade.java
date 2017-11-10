/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Permiso;
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
public class PermisoFacade extends AbstractFacade<Permiso> implements PermisoFacadeLocal {

    @PersistenceContext(unitName = "SiguemeProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PermisoFacade() {
        super(Permiso.class);
    }

    @Override
    public List<Permiso> listarPermisos() {
        try {
            TypedQuery<Permiso> query = getEntityManager().createNamedQuery("Permiso.findAll", Permiso.class);
            return query.getResultList();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void eliminarPermisosRol(Permiso permiso) {
        try {
            Query query = em.createNativeQuery("DELETE FROM PermisosRol WHERE PermisoID = ?1");
            query.setParameter(1, permiso.getPermisoID());
            query.executeUpdate();
        } catch (Exception e) {
            
        }
    }

    @Override
    public List<Permiso> permisosPorRol(Rol r) {
        try {
            Query query = em.createQuery("SELECT p FROM Rol r JOIN r.permisosRol p WHERE r.rolID = :rol",Permiso.class);
            query.setParameter("rol", r.getRolID());
            List<Permiso> permisos = query.getResultList();
            return permisos;
        } catch (Exception e) {
            throw e;
        }
    }
}
