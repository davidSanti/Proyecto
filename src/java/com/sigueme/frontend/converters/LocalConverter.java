/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.converters;

import com.sigueme.backend.entities.Usuario;
import com.sigueme.backend.model.UsuarioFacadeLocal;
import java.util.List;
import java.util.Locale;
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
@FacesConverter(forClass = Locale.class)
public class LocalConverter implements Converter {
      

    public LocalConverter() {
    }

    @Override
    public Object getAsObject(FacesContext contexto, UIComponent componente, String valor) {
        if(valor != null && !valor.equals("")){
            return new Locale(valor);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext contexto, UIComponent componente, Object objeto) {
        if (objeto != null) {
            return ((Locale) objeto).toString();
        } else {
            return "";
        }
    }
    
    
}
