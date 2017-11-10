/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.converters;

import com.sigueme.backend.entities.Usuario;
import com.sigueme.backend.model.UsuarioFacadeLocal;
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
@FacesConverter(value = "usuarioConverter")
@SessionScoped
public class UsuarioConverter implements Converter {
      
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;

    public UsuarioConverter() {
    }

    @Override
    public Object getAsObject(FacesContext contexto, UIComponent componente, String valor) {
        List<Usuario> usuarios = this.usuarioFacadeLocal.findAll();
        for (Usuario objeto : usuarios) {
            if (objeto.getUsuarioID()== Integer.parseInt(valor)) {
                return objeto;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext contexto, UIComponent componente, Object objeto) {
        if (objeto != null) {
            return ((Usuario) objeto).getUsuarioID().toString();
        } else {
            return "";
        }
    }
    
    
}
