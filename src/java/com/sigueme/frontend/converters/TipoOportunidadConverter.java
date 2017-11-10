/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.converters;

import com.sigueme.backend.entities.TipoDeOportunidad;
import com.sigueme.backend.model.TipoDeOportunidadFacadeLocal;
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
@FacesConverter(value = "tipoOportunidadConverter")
@ViewScoped
public class TipoOportunidadConverter implements Converter{
         
    @EJB
    private TipoDeOportunidadFacadeLocal tipoFacadeLocal;

    public TipoOportunidadConverter() {
    }

    @Override
    public Object getAsObject(FacesContext contexto, UIComponent componente, String valor) {
        List<TipoDeOportunidad> tipos = this.tipoFacadeLocal.findAll();
        for (TipoDeOportunidad objeto : tipos) {
            if (objeto.getTipoID()== Integer.parseInt(valor)) {
                return objeto;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext contexto, UIComponent componente, Object objeto) {
        if (objeto != null) {
            return ((TipoDeOportunidad) objeto).getTipoID().toString();
        } else {
            return "";
        }
    }

}
