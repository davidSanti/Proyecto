/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controller;

import com.sigueme.backend.entities.Categoria;
import com.sigueme.backend.entities.ErrorFrecuente;
import com.sigueme.backend.entities.Incidencia;
import com.sigueme.backend.model.CategoriaFacadeLocal;
import com.sigueme.backend.model.ErrorFrecuenteFacade;
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
@Named(value = "categoriaController")
@ViewScoped
public class CategoriaController implements Serializable {

    @EJB
    private CategoriaFacadeLocal categoriaFacadeLocal;
    private Categoria categoria;
    private List<Categoria> categorias;

    public CategoriaController() {
    }

    @PostConstruct
    public void init() {
        this.categoria = new Categoria();
        this.categorias = this.categoriaFacadeLocal.listar();
    }

    public void registarCategoria() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            int contador = 0;
            for (Categoria ct : categoriaFacadeLocal.listar()) {
                if (this.categoria.getDescripcion().equals(ct.getDescripcion())
                        && ct.getEstado()) {
                    contador++;
                }
            }
            if (contador == 0) {
                this.categoria.setEstado(true);
                this.categoriaFacadeLocal.create(this.categoria);
                this.listarCategorias();
                this.getCategorias();
                ocultarModal(1);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Registo Exitoso"));

            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Esa categoria ya existe"));
            }
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo registrar la Categoría"));
        }
    }

    public void eliminarCategoria(Categoria categoria) {
        this.categoria = categoria;
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            this.categoria.setEstado(false);
            this.categoriaFacadeLocal.edit(this.categoria);
            this.listarCategorias();
            this.getCategorias();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Se ha eliminado correctamente la categoría"));

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha podido eliminar la Categoría"));
        }
    }

    public void editarCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void editarCategoria() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            int contador = 0;
            for (Categoria ct : categoriaFacadeLocal.listar()) {
                if (ct.getDescripcion().equals(this.categoria.getDescripcion())
                        && ct.getCategoriaID() != this.categoria.getCategoriaID()) {
                    contador += 1;
                }
            }
            if (contador == 0) {
                this.categoriaFacadeLocal.edit(this.categoria);
                ocultarModal(2);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Se ha modificado correctamente la categoría"));

            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Esa categoria ya existe"));
            }
            this.listarCategorias();
            this.getCategorias();
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No ha podido modificar la Categoría"));
        }
    }

    public void ocultarModal(int option) {
        RequestContext req = RequestContext.getCurrentInstance();
        String form = null;
        switch (option) {
            case 1:
                req.execute("PF('categoriaCrear').hide()");
                form = "crearCategoriaForm";
                break;
            case 2:
                req.execute("PF('categoriaEditar').hide()");
                form = "editarCategoriaForm";
                break;
            default:
                break;
        }
        req.reset(form);
        this.categoria = new Categoria();
    }

    public List<Categoria> listarCategorias() {
        return this.categorias;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<Categoria> getCategorias() {
        categorias = this.categoriaFacadeLocal.listar();
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

}
