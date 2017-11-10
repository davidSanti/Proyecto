/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controller;

import com.sigueme.backend.entities.Permiso;
import com.sigueme.backend.entities.Rol;
import com.sigueme.backend.model.PermisoFacadeLocal;
import com.sigueme.backend.model.RolFacadeLocal;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Kathe
 */
@Named(value = "permisoController")
@ViewScoped
public class PermisoController implements Serializable {

    @EJB
    private PermisoFacadeLocal permisoFacadeLocal;
    @EJB
    private RolFacadeLocal rolFacadeLocal;

    private Permiso permiso;
    private Rol rol;

    private List<Permiso> permisos;
    private List<Rol> roles;
    private boolean tipo;

    public PermisoController() {
    }

    @PostConstruct
    public void init() {
        this.permiso = new Permiso();
        this.rol = new Rol();
        this.permisos = this.permisoFacadeLocal.listarPermisos();
    }

    public void registrarPermiso() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            this.permiso.setTipo(tipo);
            this.permisoFacadeLocal.create(this.permiso);
            getPermisos();
            listarPermisos();
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Se ha registrado correctamente el permiso"));

        } catch (Exception e) {
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo registrar el permiso"));
        }
    }

    public void editarPermiso(Permiso permiso) {
        this.permiso = permiso;
    }

    public void editarPermiso() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            this.permisoFacadeLocal.edit(this.permiso);
            ocultarModales(1);
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Se ha modificado correctamente el permiso"));
        } catch (Exception e) {
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo modificar el permiso"));

        }
    }

    public void eliminarPermiso(Permiso permiso) {
        FacesContext context = FacesContext.getCurrentInstance();
        this.permiso = permiso;
        try {
            this.permisoFacadeLocal.eliminarPermisosRol(this.permiso);
            this.permisoFacadeLocal.remove(this.permiso);
            this.getPermisos();
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Se ha eliminado correctamente el permiso"));
        } catch (Exception e) {
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar el permiso"));

        }
    }

    public List<Permiso> listarPermisos() {
        return this.permisos;
    }

    public String verificarDependencia(Permiso permiso) {
        if (permiso.getTipo()) {
            return "Permission.SubMenu";
        }
        return "Permission.Menu";
    }

    public String mostrarDescripcion(String descripcion) {
        ResourceBundle r = ResourceBundle.getBundle("/mensajes");
        String nombre = descripcion;
        nombre = (r.containsKey(r.getString(nombre)) ? r.getString(nombre) : descripcion);
        return nombre;
    }

    public void registrarRol() {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle r = ResourceBundle.getBundle("/mensajes");

        try {
            this.rol.setEstado(true);
            this.rolFacadeLocal.create(this.rol);
            getRoles();
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Registro exitoso"));
        } catch (Exception e) {
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo registar el rol"));

        }
    }

    public void editarRol(Rol rol) {
        this.rol = rol;
    }

    public void editarRol() {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle r = ResourceBundle.getBundle("/mensajes");

        try {
            this.rolFacadeLocal.edit(this.rol);
            ocultarModales(2);
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "se ha modificado correctamente el rol"));
        } catch (Exception e) {
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo modificar el permiso"));

        }
    }

    public void eliminarRol(Rol rol) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle r = ResourceBundle.getBundle("/mensajes");
        this.rol = rol;
        try {
            this.rolFacadeLocal.eliminarPermisos(this.rol);
            this.rolFacadeLocal.remove(this.rol);
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Rol eliminado exitosamente"));
        } catch (Exception e) {
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar el rol"));

        }
    }

    public List<Rol> listarRoles() {
        return this.rolFacadeLocal.findAll();
    }

    public boolean volverVerdadero() {
        this.tipo = true;
        return tipo;
    }

    public boolean volverFalso() {
        this.tipo = false;
        return tipo;
    }

    public void ocultarModales(int option){
        RequestContext r = RequestContext.getCurrentInstance();
        String form = null;
        switch (option) {
            case 1:
                r.execute("PF('editPermission').hide()");
                form = "formEditPermission";
                break;
            case 2:
                r.execute("PF('editRol').hide()");
                form = "formEditRol";
                break;
            default:
               break;
        }
        r.reset(form);
    }
 
    public Permiso getPermiso() {
        return permiso;
    }

    public void setPermiso(Permiso permiso) {
        this.permiso = permiso;
    }

    public List<Permiso> getPermisos() {
        permisos = permisoFacadeLocal.listarPermisos();
        return permisos;
    }

    public void setPermisos(List<Permiso> permisos) {
        this.permisos = permisos;
    }

    public boolean isTipo() {
        return tipo;
    }

    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }

    public List<Rol> getRoles() {
        this.roles = rolFacadeLocal.findAll();
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
    
    

}
