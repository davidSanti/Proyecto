/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controller;

import com.sigueme.backend.entities.ErrorFrecuente;
import com.sigueme.backend.entities.Incidencia;
import com.sigueme.backend.model.ErrorFrecuenteFacadeLocal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Santi
 */
@Named(value = "errorController")
@ViewScoped
public class ErrorController implements Serializable{

    @EJB
    private ErrorFrecuenteFacadeLocal errorFacadeLocal;
    private ErrorFrecuente errorFrecuente;
    private List<ErrorFrecuente> errores;

    public ErrorController() {
    }

    @PostConstruct
    public void init() {
        errorFrecuente = new ErrorFrecuente();
        errores = this.errorFacadeLocal.listar();
    }

    public void registrarErrorFrecuente() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            int contador = 0;
            for (ErrorFrecuente ef : this.errorFacadeLocal.findAll()) {
                if (this.errorFrecuente.getDescripcion().equals(ef.getDescripcion())) {
                    contador++;
                }
            }
            if (contador == 0) {
                this.errorFrecuente.setEstado(true);
                this.errorFacadeLocal.create(errorFrecuente);
                this.listarErrores();
                this.getErrores();
                ocultarModal(1);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Registo Exitoso"));
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Ese Error frecuente ya existe"));
            }
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo registrar la Categoría"));
        }
    }

    public void eliminarError(ErrorFrecuente error) {
        this.errorFrecuente = error;
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            this.errorFrecuente.setEstado(false);
            this.errorFacadeLocal.edit(this.errorFrecuente);
            this.listarErrores();
            this.getErrores();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Se ha eliminado correctamente el error"));

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "No se ha podido eliminar el error"));
        }
    }

    public void editarError(ErrorFrecuente error) {
        this.errorFrecuente = error;
    }

    public void editarError() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            int contador = 0;            
            for (ErrorFrecuente ef : this.errorFacadeLocal.listar()) {
                if (ef.getDescripcion().equals(this.errorFrecuente.getDescripcion())
                        && this.errorFrecuente.getErrorID() != ef.getErrorID()) {
                    contador += 1;
                }
            }
            if (contador == 0) {
                this.errorFacadeLocal.edit(errorFrecuente);
                ocultarModal(2);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Se ha modificado correctamente el error"));
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Ese Error frecuente ya existe"));
            }
            this.listarErrores();
            this.getErrores();
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "No ha podido modificar el error"));
        }
    }

    public List<ErrorFrecuente> listarErrores() {
        return errores;
    }

    public void ocultarModal(int option) {
        RequestContext req = RequestContext.getCurrentInstance();
        String form = null;
        switch (option) {
            case 1:
                req.execute("PF('errorFrecuenteCrear').hide()");
                form = "crearErrorFrecuente";
                break;
            case 2:
                req.execute("PF('errorFrecuenteEditar').hide()");
                form = "editarErrorFrecuente";
                break;
            default:
                break;
        }
        req.reset(form);
        this.errorFrecuente = new ErrorFrecuente();
    }

    public ErrorFrecuente getErrorFrecuente() {
        return errorFrecuente;
    }

    public void setErrorFrecuente(ErrorFrecuente errorFrecuente) {
        this.errorFrecuente = errorFrecuente;
    }

    public List<ErrorFrecuente> getErrores() {
        errores = this.errorFacadeLocal.listar();
        return errores;
    }

    public void setErrores(List<ErrorFrecuente> errores) {
        this.errores = errores;
    }

}
