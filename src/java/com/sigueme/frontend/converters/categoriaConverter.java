/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.converters;

import com.sigueme.backend.entities.Categoria;
import com.sigueme.backend.model.CategoriaFacadeLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Santi
 */
@FacesConverter(value = "categoriaConverter")
@SessionScoped
public class categoriaConverter implements Converter{
     
    @EJB
    private CategoriaFacadeLocal categoriaFacadeLocal;

    public categoriaConverter() {
    }

    @Override
    public Object getAsObject(FacesContext contexto, UIComponent componente, String valor) {
        List<Categoria> categorias = this.categoriaFacadeLocal.findAll();
        for (Categoria objeto : categorias) {
            if (objeto.getCategoriaID() == Integer.parseInt(valor)) {
                return objeto;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext contexto, UIComponent componente, Object objeto) {
        if (objeto != null) {
            return ((Categoria) objeto).getCategoriaID().toString();
        } else {
            return "";
        }
    }
}
