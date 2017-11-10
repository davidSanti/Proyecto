/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Categoria;
import com.sigueme.backend.entities.Incidencia;
import com.sigueme.backend.entities.IncidenciaAgenteReporte;
import com.sigueme.backend.entities.IncidenciaPorCategoria;
import com.sigueme.backend.entities.Usuario;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;

/**
 *
 * @author Santi
 */
@Stateless
public class IncidenciaFacade extends AbstractFacade<Incidencia> implements IncidenciaFacadeLocal {

    @PersistenceContext(unitName = "SiguemeProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IncidenciaFacade() {
        super(Incidencia.class);
    }

    @Override
    public void editarEstado(Incidencia inci) {
        String consulta;

        try {
            consulta = "UPDATE Incidencia i SET i.estado = false WHERE i.incidenciaID = ?1";
            Query query = em.createQuery(consulta);
            query.setParameter(1, inci.getIncidenciaID());
            query.executeUpdate();
        } catch (Exception e) {
            
        }
    }

    @Override
    public List<Incidencia> listar() {
        TypedQuery<Incidencia> query = getEntityManager().createNamedQuery("Incidencia.findAll", Incidencia.class);
        return query.getResultList();
    }

    @Override
    public List<Incidencia> listarIncidenciasPropias(Usuario usuario) {
        try {
            TypedQuery<Incidencia> query = getEntityManager().createNamedQuery("Incidencia.listarPropias", Incidencia.class);
            query.setParameter("usuarioID", usuario.getUsuarioID());
            return query.getResultList();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<Object[]> generarReporteIncidenciasTotalAgente() {
        List<Incidencia> listaIncidenciasGrafica;
        try {
            Query query = em.createNativeQuery("SELECT u.UsuarioID, COUNT(i.IncidenciaID) "
                    + "FROM Incidencias i "
                    + "INNER JOIN Usuarios u "
                    + "ON i.UsuarioID = u.UsuarioID "
                    + "WHERE  MONTH(i.FechaRegistro) = MONTH(GETDATE()) AND i.Estado = 1 AND u.Estado = 1 "
                    + "GROUP BY u.UsuarioID");
            List<Object[]> result = query.getResultList();
            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<Object[]> generarReporteIncidenciasPorCategoria() {
        Query query = em.createNativeQuery("SELECT c.Descripcion, COUNT(i.IncidenciaID) "
                + "FROM Incidencias i "
                + "INNER JOIN Categorias c "
                + "ON i.CategoriaID = c.CategoriaID "
                + "WHERE i.Estado = 1 "
                + "GROUP BY c.Descripcion");
        List<Object[]> result = query.getResultList();
        return result;
    }

    @Override
    public List<Incidencia> listarMismoCasoAgente(Incidencia incidencia) {
        try {
            Query query = em.createQuery("SELECT i FROM Incidencia i WHERE i.numeroDeCaso = :numeroDeCaso AND i.usuarioID = :usuario AND i.estado = true");
            query.setParameter("numeroDeCaso", incidencia.getNumeroDeCaso());
            query.setParameter("usuario", incidencia.getUsuarioID());
            return query.getResultList();
        } catch (Exception e) {
            throw e;
        }

    }

    @Override
    public List<Incidencia> listarMismoCasoCategoria(Incidencia incidencia) {
        try {
            TypedQuery<Incidencia> query = em.createNamedQuery("Incidencia.findByNumeroDeCaso", Incidencia.class);
            query.setParameter("numeroDeCaso", incidencia.getNumeroDeCaso());
            return query.getResultList();
        } catch (Exception e) {
            throw e;
        }

    }

    @Override
    public void inactivarIncidenciasDeUsuarioEliminado(Usuario usuario) {
        try {
            Query query = em.createQuery("UPDATE Incidencia i SET i.estado = false WHERE i.usuarioID = :usuario");
            query.setParameter("usuario", usuario);
            query.executeUpdate();
        } catch (Exception e) {
            
        }
    }

    @Override
    public void listarIncidenciasParaOportunidad() {
        List<IncidenciaPorCategoria> inciadencias = null;
        List<Object> objetos = null;
        try {
            Query query1 = em.createNativeQuery("SELECT u.UsuarioID, c.Descripcion\n"
                    + "FROM Incidencias i\n"
                    + "INNER JOIN Categorias c\n"
                    + "ON i.CategoriaID = c.CategoriaID\n"
                    + "INNER JOIN Usuarios u\n"
                    + "ON i.UsuarioID = u.UsuarioID\n"
                    + "WHERE i.Estado = 1 AND u.Estado = 1 AND MONTH(i.FechaRegistro) = MONTH(GETDATE())\n"
                    + "GROUP BY u.UsuarioID, c.Descripcion\n"
                    + "HAVING COUNT(c.CategoriaID) > 0");
            List<Object[]> lista = query1.getResultList();

            for (Object[] object : lista) {
                IncidenciaPorCategoria inciadenciaP = new IncidenciaPorCategoria();
                inciadenciaP.setUsuarioId((int) object[0]);
                inciadenciaP.setNombreCategoria((String) object[1]);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<Incidencia> listarIncidenciasParaOportunidades() {
        try {        
            Query query1 = em.createQuery("SELECT i FROM Incidencia i WHERE i.usuarioID.estado = true AND i.estado = true AND FUNC('MONTH', i.fechaRegistro) = FUNC('MONTH', CURRENT_TIMESTAMP) GROUP BY i.incidenciaID, i.descripcion, i.estado, i.fechaRegistro, i.numeroDeCaso, i.usuarioID, i.categoriaID ");
            return query1.getResultList();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<Incidencia> listarIncidenciasUsuariosPorCategoria(Usuario usuario, Categoria categoria) {
        try {
            TypedQuery<Incidencia> query = em.createQuery("SELECT i FROM Incidencia i WHERE i.estado = true AND i.usuarioID = :usuario AND i.categoriaID = :categoria AND FUNC('MONTH', i.fechaRegistro) = FUNC('MONTH',CURRENT_TIMESTAMP)", Incidencia.class);
            query.setParameter("usuario", usuario);
            query.setParameter("categoria", categoria);
            return query.getResultList();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<IncidenciaAgenteReporte> incidenciaAgenteReporte() {
        List<IncidenciaAgenteReporte> incidenciaAgenteReporte = new ArrayList();
        String consulta = "SELECT Usuarios.Nombre, DATENAME(MONTH,Incidencias.FechaRegistro), COUNT(IncidenciaID) FROM Usuarios \n"
                + "INNER JOIN UsuariosRol on Usuarios.UsuarioID = UsuariosRol.UsuarioID\n"
                + "INNER JOIN Incidencias on Usuarios.UsuarioID = Incidencias.UsuarioID\n"
                + "WHERE Usuarios.Estado = 1\n"
                + "AND DATEPART(YEAR,Incidencias.FechaRegistro) = 2017\n"
                + "GROUP BY Usuarios.Nombre,DATENAME(MONTH,Incidencias.FechaRegistro)\n"
                + "ORDER BY DATENAME(MONTH,Incidencias.FechaRegistro),Usuarios.Nombre";
        try {
            Query query = em.createNativeQuery(consulta);
            List<Object[]> resultList = query.getResultList();
            for (Object[] object : resultList) {
                IncidenciaAgenteReporte iar = new IncidenciaAgenteReporte();
                iar.setNombre(object[0].toString());
                iar.setMes(object[1].toString());
                iar.setCantidad(Integer.parseInt(object[2].toString()));
                incidenciaAgenteReporte.add(iar);
            }
        } catch (Exception e) {
            throw e;
        }
        return incidenciaAgenteReporte;
    }
}
