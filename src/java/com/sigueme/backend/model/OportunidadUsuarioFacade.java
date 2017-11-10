/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.OportunidadDeAprendizaje;
import com.sigueme.backend.entities.OportunidadUsuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;

/**
 *
 * @author Santi
 */
@Stateless
public class OportunidadUsuarioFacade extends AbstractFacade<OportunidadUsuario> implements OportunidadUsuarioFacadeLocal {

    @PersistenceContext(unitName = "SiguemeProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OportunidadUsuarioFacade() {
        super(OportunidadUsuario.class);
    }

    @Override
    public void editarOportunidadUsuario(OportunidadUsuario algo) {
        try {
            Query query = em.createQuery("UPDATE OportunidadUsuario u SET u.descripcion=:descripcion, u.archivo=:archivo, u.estadoID=:estado WHERE u.usuarioID.usuarioID=:usuario and u.oportunidadID.oportunidadID=:oportunidad");
            query.setParameter("descripcion", algo.getDescripcion());
            query.setParameter("archivo", algo.getArchivo());
            query.setParameter("estado", algo.getEstadoID());
            query.setParameter("usuario", algo.getUsuarioID().getUsuarioID());
            query.setParameter("oportunidad", algo.getOportunidadID().getOportunidadID());
            query.executeUpdate();

        } catch (Exception e) {
            System.out.println("algo paso " + e.getMessage());
        }
    }

    @Override
    public void inactivarOportunidadUsuario(OportunidadUsuario oportunidad) {
        try {
            Query query = getEntityManager().createQuery("UPDATE OportunidadUsuario ou SET ou.estadoID.estadoID = 4 WHERE ou.oportunidadID.oportunidadID =:oportunidad AND ou.usuarioID.usuarioID =:usuario");
            query.setParameter("oportunidad", oportunidad.getOportunidadID().getOportunidadID());
            query.setParameter("usuario", oportunidad.getUsuarioID().getUsuarioID());
            query.executeUpdate();
        } catch (Exception e) {
            
        }
    }

    @Override
    public void eliminarOportunidadUsuario() {
        try {
            StoredProcedureQuery stpq = em.createNamedStoredProcedureQuery("eliminarOportunidadUsuario");
            stpq.execute();
        } catch (Exception e) {
            System.out.println("Error en el procedimiento almacenado de eliminar Oportunidad Usuario" + e.getMessage());
        }
    }
    
    @Override
    public List<OportunidadUsuario> listarOportunidadesUsuario(){
        try{
            TypedQuery<OportunidadUsuario> query = getEntityManager().createNamedQuery("OportunidadUsuario.findAll", OportunidadUsuario.class);
            return query.getResultList();
        } catch(Exception e) {
            throw e;
        }
    }
    
    @Override
    public void inactivarOportunidadesUsuarioAlEliminarUsuario(OportunidadUsuario oportunidadUsuario) {
        try {
            Query query = em.createQuery("UPDATE OportunidadUsuario ou SET ou.estadoID = :estado WHERE ou.usuarioID = :usuario AND ou.estadoID.estadoID != 6");
            query.setParameter("estado", oportunidadUsuario.getEstadoID());
            query.setParameter("usuario", oportunidadUsuario.getUsuarioID());
            query.executeUpdate();
        } catch (Exception e) {
            
        }
    }
    
    @Override
    public List<OportunidadUsuario> listarOportunidadesUsuarioPorOportunidad(OportunidadDeAprendizaje oportunidad) {
        try {
            Query query = em.createQuery("SELECT ou FROM OportunidadUsuario ou WHERE ou.oportunidadID = :oportunidad AND ou.estadoID.estadoID != 6 ", OportunidadUsuario.class);
            query.setParameter("oportunidad", oportunidad);
            return query.getResultList();
        } catch (Exception e) {
            throw e;
        }
    }

}
