/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controller;

import com.sigueme.backend.entities.Categoria;
import com.sigueme.backend.entities.ErrorFrecuente;
import com.sigueme.backend.entities.Incidencia;
import com.sigueme.backend.entities.Rol;
import com.sigueme.backend.entities.Usuario;
import com.sigueme.backend.model.CategoriaFacadeLocal;
import com.sigueme.backend.model.ErrorFrecuenteFacadeLocal;
import com.sigueme.backend.model.IncidenciaFacadeLocal;
import com.sigueme.backend.model.UsuarioFacadeLocal;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Santi
 */
@Named(value = "incidenciaController")
@ViewScoped
public class IncidenciaController implements Serializable {

    @EJB
    private IncidenciaFacadeLocal incidenciaFacadeLocal;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    @EJB
    private CategoriaFacadeLocal categoriaFacadeLocal;
    @EJB
    private ErrorFrecuenteFacadeLocal errorFrecuenteFacadeLocal;

    private Usuario usuario;
    private Categoria categoria;
    private Incidencia incidencia;
    private ErrorFrecuente errorFrecuente;

    private List<Usuario> usuarios;
    private List<Categoria> categorias;
    private List<Incidencia> incidencias;
    private List<ErrorFrecuente> errores;

    private boolean banderaRegistro = false;

    public IncidenciaController() {

    }

    @PostConstruct
    public void init() {
        usuario = new Usuario();
        categoria = new Categoria();
        incidencia = new Incidencia();
        errorFrecuente = new ErrorFrecuente();

    }

    public void registrarIncidencia() {
        ResourceBundle r = ResourceBundle.getBundle("/mensajes");
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (verificarMismoCasoAgente(incidencia)) {
                if (verificarMismoCasoCategoria(incidencia)) {
                    this.incidenciaFacadeLocal.create(incidencia);
                    this.banderaRegistro = true;
                    context.addMessage(
                            "growlIncidents", new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", r.getString("Message.Incident.register")));
                } else {
                    context.addMessage(
                            "growlIncidents", new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Ya se registró el mismo caso con otra categoría"));
                }

            } else {
                context.addMessage(
                        "growlIncidents", new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Ya se registró el mismo caso para éste agente"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            context.addMessage(
                    "growlIncidents", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo registrar la incidencia"));
        }
    }

    public void eliminarIncidencia(Incidencia incidencia) {
        FacesContext context = FacesContext.getCurrentInstance();
        this.incidencia = incidencia;
        try {
            this.incidencia.setEstado(false);
            this.incidenciaFacadeLocal.edit(this.incidencia);
            this.incidencia = new Incidencia();
            listarIncidencia();
            getIncidencias();
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "La incidencia se ha eliminado correctamente"));

        } catch (Exception e) {
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo eliminar la incidencia"));
        }

    }

    public boolean verificarMismoCasoAgente(Incidencia incidencia) {
        List<Incidencia> incidenciasCaso = this.incidenciaFacadeLocal.listarMismoCasoAgente(incidencia);
        boolean bandera = (incidenciasCaso.isEmpty() ? true : false);
        return bandera;
    }

    public boolean verificarMismoCasoCategoria(Incidencia incidencia) {
        List<Incidencia> incidenciasCaso = this.incidenciaFacadeLocal.listarMismoCasoCategoria(incidencia);
        boolean bandera = false;
        if (!incidenciasCaso.isEmpty()) {
            if (incidenciasCaso.get(0).getCategoriaID().getCategoriaID() == incidencia.getCategoriaID().getCategoriaID()) {
                bandera = true;
            }
        } else {
            bandera = true;
        }
        return bandera;
    }

    public void editarIncidencia(Incidencia incidencia) {
        this.incidencia = incidencia;
    }

    public void editarIncidencia() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (verificarMismoCasoCategoria(incidencia)) {
                this.incidenciaFacadeLocal.edit(this.incidencia);
                this.incidencia = new Incidencia();
                this.banderaRegistro = true;
                context.addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "La incidencia se ha modificado correctamente"));
            } else {
                context.addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Ya se registró el mismo caso con otra categoría"));
            }

        } catch (Exception e) {
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "La incidencia no se pudo modificar"));
        }
        this.incidencia = new Incidencia();
    }

    public List<Incidencia> listarIncidencia() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Usuario u = (Usuario) session.getAttribute("usuario");
        if (verificarRol()) {
            return this.incidenciaFacadeLocal.listarIncidenciasPropias(u);
        }
        return this.incidenciaFacadeLocal.listar();
    }

    public void listJPQ() {
        try {
            List<Incidencia> listaaaa = this.incidenciaFacadeLocal.listarIncidenciasParaOportunidades();
        } catch (Exception e) {

        }
    }

    public void ocultarModalRegistrar() {
        this.registrarIncidencia();
        if (banderaRegistro) {
            this.ocultarModal(1);
            banderaRegistro = false;
        }
    }

    public void ocultarModalEditar() {
        this.editarIncidencia();
        if (banderaRegistro) {
            this.ocultarModal(2);
            banderaRegistro = false;
        }
    }

    public void ocultarModal(int option) {
        RequestContext req = RequestContext.getCurrentInstance();
        String form = null;
        switch (option) {
            case 1:
                req.execute("PF('registrarIncidencia').hide()");
                form = "registroIncidencia";
                break;
            case 2:
                req.execute("PF('editarIncidencia').hide()");
                form = "f";
                break;
            case 3:
                req.execute("PF('verIncidencia').hide()");
                form = "botonViewIncident";
                break;
            default:
                break;
        }
        req.reset(form);
        this.incidencia = new Incidencia();
        banderaRegistro = false;
    }

    public boolean verificarRol() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Rol r = (Rol) session.getAttribute("rol");
        boolean bandera = false;

        if (r.getRolID() == 1) {
            bandera = true;
        }
        return bandera;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Incidencia getIncidencia() {
        return incidencia;
    }

    public void setIncidencia(Incidencia incidencia) {
        this.incidencia = incidencia;
    }

    public ErrorFrecuente getErrorFrecuente() {
        return errorFrecuente;
    }

    public void setErrorFrecuente(ErrorFrecuente errorFrecuente) {
        this.errorFrecuente = errorFrecuente;
    }

    public List<Usuario> getUsuarios() {
        usuarios = usuarioFacadeLocal.listarAgentes();
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Categoria> getCategorias() {
        categorias = categoriaFacadeLocal.listar();
        return categorias;
    }

    public void setServicios(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public List<Incidencia> getIncidencias() {
        incidencias = incidenciaFacadeLocal.findAll();
        return incidencias;
    }

    public void setIncidencias(List<Incidencia> incidencias) {
        this.incidencias = incidencias;
    }

    public List<ErrorFrecuente> getErrores() {
        errores = errorFrecuenteFacadeLocal.findAll();
        return errores;
    }

    public void setErrores(List<ErrorFrecuente> errores) {
        this.errores = errores;
    }

    public boolean isBanderaRegistro() {
        return banderaRegistro;
    }

    public void setBanderaRegistro(boolean banderaRegistro) {
        this.banderaRegistro = banderaRegistro;
    }

}
