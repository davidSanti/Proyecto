/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.converters;

import com.sigueme.backend.entities.EstadoOportunidad;
import com.sigueme.backend.entities.Rol;
import com.sigueme.backend.model.EstadoOportunidadFacadeLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Santi
 */
@FacesConverter (value = "estadosOportunidadConverter")
@ViewScoped
public class EstadosOportunidadConverter implements Converter{
    
    @EJB
    private EstadoOportunidadFacadeLocal estadoFacadeLocal;

    public EstadosOportunidadConverter() {
    }

    @Override
    public Object getAsObject(FacesContext contexto, UIComponent componente, String valor) {
        List<EstadoOportunidad> estados = this.estadoFacadeLocal.findAll();
        for (EstadoOportunidad objeto : estados) {
            if (objeto.getEstadoID()== Integer.parseInt(valor)) {
                return objeto;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext contexto, UIComponent componente, Object objeto) {
        if (objeto != null) {
            return ((EstadoOportunidad) objeto).getEstadoID().toString();
        } else {
            return "";
        }
    }

    
}
