/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.OportunidadDeAprendizaje;
import com.sigueme.backend.entities.OportunidadUsuario;
import com.sigueme.backend.entities.Usuario;
import java.util.Date;
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
public class OportunidadDeAprendizajeFacade extends AbstractFacade<OportunidadDeAprendizaje> implements OportunidadDeAprendizajeFacadeLocal {

    @PersistenceContext(unitName = "SiguemeProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OportunidadDeAprendizajeFacade() {
        super(OportunidadDeAprendizaje.class);
    }

    @Override
    public void editarEstado(OportunidadDeAprendizaje oportunidad) {
        try {
            Query query = em.createQuery("UPDATE OportunidadUsuario ou SET ou.estadoID.estadoID = 6 WHERE ou.oportunidadID.oportunidadID =:oportunidad");
            query.setParameter("oportunidad", oportunidad.getOportunidadID());
            query.executeUpdate();
        } catch (Exception e) {
            
        }
    }

    @Override
    public List<OportunidadDeAprendizaje> misOportunidades(Usuario p) {
        Date now = new Date(System.currentTimeMillis());
        try {
            List<OportunidadDeAprendizaje> oportunidades;
            Query query = em.createQuery("SELECT p FROM OportunidadUsuario t JOIN t.oportunidadID p WHERE t.usuarioID.usuarioID =?1 AND p.estado=true AND p.fechaInicio <= ?2 AND p.fechaFin >= ?3 AND t.estadoID.estadoID IN(1,3,5)");
            query.setParameter(1, p.getUsuarioID());
            query.setParameter(2, now);
            query.setParameter(3, now);
            oportunidades = query.getResultList();
            return oportunidades;
        } catch (Exception n) {
            throw n;
        }

    }

    @Override
    public List<OportunidadUsuario> verificarOportunidadesRealizadas(OportunidadDeAprendizaje p) {
        try {
            Query query = em.createQuery("SELECT t FROM OportunidadUsuario t JOIN t.oportunidadID p WHERE p.oportunidadID= ?1 AND t.descripcion != null");
            query.setParameter(1, p.getOportunidadID());
            return query.getResultList();
        } catch (Exception n) {
            throw n;
        }

    }

    @Override
    public List<Usuario> listarAgentesPorOportunidad(OportunidadDeAprendizaje p) {
        try {
            Query query = em.createQuery("SELECT t.usuarioID FROM OportunidadUsuario t JOIN t.oportunidadID p WHERE p.oportunidadID= ?1 AND t.estadoID.estadoID != 6");
            query.setParameter(1, p.getOportunidadID());
            return query.getResultList();
        } catch (Exception n) {
            throw n;
        }

    }
    
    @Override
    public List<OportunidadDeAprendizaje> listarLider() {
        Date now = new Date(System.currentTimeMillis());
        try {
            TypedQuery<OportunidadDeAprendizaje> query = getEntityManager().createNamedQuery("OportunidadDeAprendizaje.findAll", OportunidadDeAprendizaje.class);
            return query.getResultList();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void inhabilitarOportunidades() {
        try {
            StoredProcedureQuery stpq = em.createNamedStoredProcedureQuery("inhabilitarOportunidadDeAprendizaje");
            stpq.execute();
        } catch (Exception e) {
            System.out.println("Error en el procedimiento almacenado de inhabilitarOportunidad" + e.getMessage());
        }
    }
    
    @Override
    public List<OportunidadDeAprendizaje> listarOportunidadesPorUsuarioEliminado(Usuario usuario) {
        try {
            Query query = em.createQuery("SELECT op FROM OportunidadUsuario ou JOIN ou.oportunidadID op WHERE ou.usuarioID = :usuario AND ou.estadoID.estadoID != 6 AND op.estado = true", OportunidadDeAprendizaje.class);
            query.setParameter("usuario", usuario);
            return query.getResultList();
        } catch (Exception e) {
            throw e;
        }
    }
    
    public OportunidadDeAprendizaje retornarOportunidadNuevoIntento(OportunidadDeAprendizaje oportunidad){
        OportunidadDeAprendizaje oportunity;
        try {
            TypedQuery<OportunidadDeAprendizaje> query = em.createQuery("SELECT p FROM OportunidadUsuario t JOIN t.oportunidadID p WHERE t.oportunidadID = :oportunidad", OportunidadDeAprendizaje.class );
            query.setParameter("oportunidad", oportunidad);
            oportunity = query.getResultList().get(0);
            return oportunity;
        } catch (Exception e) {
            throw e;
        }
    }

}
