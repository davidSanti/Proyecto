/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Usuario;
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
public class UsuarioFacade extends AbstractFacade<Usuario> implements UsuarioFacadeLocal {

    @PersistenceContext(unitName = "SiguemeProyectoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }

    @Override
    public Usuario iniciarSesion(Usuario us) {
        Usuario usuario = null;
        String consulta;
        try {
            consulta = "SELECT u FROM Usuario u WHERE u.cedula = ?1 AND u.clave = ?2 AND u.estado = 1 ";
            Query query = em.createQuery(consulta);
            query.setParameter(1, us.getCedula());
            query.setParameter(2, us.getClave());

            List<Usuario> usuariosLista = query.getResultList();
            if (!usuariosLista.isEmpty()) {
                usuario = usuariosLista.get(0);
            }
        } catch (Exception e) {
            throw e;
        }
        return usuario;
    }

    @Override
    public void editarEstado(Usuario us) {
        String consulta;

        try {
            consulta = "UPDATE Usuario u SET u.estado = false WHERE u.usuarioID = ?1";
            Query query = em.createQuery(consulta);
            query.setParameter(1, us.getUsuarioID());
            query.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<Usuario> listar() {
        Query query = em.createQuery("SELECT u FROM Usuario u JOIN u.rolesUsuario r WHERE r.rolID != 4 AND u.estado = true");
        return query.getResultList();
    }

    @Override
    public List<Usuario> listarAgentes() {
        Query query = em.createQuery("SELECT u FROM Usuario u JOIN u.rolesUsuario r WHERE r.rolID = 1 AND u.estado = true");
        List<Usuario> usuarios = query.getResultList();
        return usuarios;
    }

    @Override
    public List<Usuario> verificarDocumento(String cedula) {
        TypedQuery<Usuario> query = getEntityManager().createNamedQuery("Usuario.findByCedula", Usuario.class);
        query.setParameter("cedula", cedula);
        return query.getResultList();
    }

    @Override
    public List<Usuario> verificarExistenciaLider() {
        Query query = em.createQuery("SELECT u FROM Rol r JOIN r.usuariosRol u WHERE r.rolID =2 AND u.estado = true");
        List<Usuario> usuarios = query.getResultList();
        return usuarios;
    }

    @Override
    public List<Usuario> verificarDatosParaRecuperarContrasena(Usuario usuario) {
        Query query = em.createQuery("SELECT u FROM Usuario u WHERE u.cedula = :cedula AND u.email = :email AND u.estado = true");
        query.setParameter("cedula", usuario.getCedula());
        query.setParameter("email", usuario.getEmail());
        return query.getResultList();
    }

}
