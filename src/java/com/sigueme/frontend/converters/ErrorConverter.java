/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.converters;

import com.sigueme.backend.entities.ErrorFrecuente;
import com.sigueme.backend.model.ErrorFrecuenteFacadeLocal;
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
@FacesConverter(value = "errorConverter")
@SessionScoped
public class ErrorConverter implements Converter{
     
    @EJB
    private ErrorFrecuenteFacadeLocal errorFacadeLocal;

    public ErrorConverter() {
    }

    @Override
    public Object getAsObject(FacesContext contexto, UIComponent componente, String valor) {
        List<ErrorFrecuente> errores = this.errorFacadeLocal.findAll();
        for (ErrorFrecuente objeto : errores) {
            if (objeto.getErrorID() == Integer.parseInt(valor)) {
                return objeto;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext contexto, UIComponent componente, Object objeto) {
        if (objeto != null) {
            return ((ErrorFrecuente) objeto).getErrorID().toString();
        } else {
            return "";
        }
    }
    
}
