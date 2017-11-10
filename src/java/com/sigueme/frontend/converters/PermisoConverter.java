/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.converters;

import com.sigueme.backend.entities.EstadoOportunidad;
import com.sigueme.backend.entities.Permiso;
import com.sigueme.backend.model.PermisoFacadeLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.faces.convert.Converter;

/**
 *
 * @author Santi
 */
@FacesConverter (value = "permisoConverter")
@ViewScoped
public class PermisoConverter implements Converter{
    
    @EJB
    private PermisoFacadeLocal permisoFacadeLocal;

    public PermisoConverter() {
    }

    @Override
    public Object getAsObject(FacesContext contexto, UIComponent componente, String valor) {
        List<Permiso> permisos = this.permisoFacadeLocal.listarPermisos();
        for (Permiso objeto : permisos) {
            if (objeto.getPermisoID() == Integer.parseInt(valor)) {
                return objeto;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext contexto, UIComponent componente, Object objeto) {
        if (objeto != null) {
            return ((Permiso) objeto).getPermisoID().toString();
        } else {
            return "";
        }
    }

}
