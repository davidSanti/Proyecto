/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controller;

import com.sigueme.backend.entities.Crypto;
import com.sigueme.backend.entities.Mail;
import com.sigueme.backend.entities.PasswordGenerator;
import com.sigueme.backend.entities.Usuario;
import com.sigueme.backend.model.UsuarioFacadeLocal;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Santi
 */
@ManagedBean(name ="mensajeController")
@RequestScoped
public class MensajeController implements Serializable {

    private Usuario usuario;
    private String mensajeContacto;

    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    
    public MensajeController() {
    }

    @PostConstruct
    public void init() {
        this.usuario = new Usuario();
    }
    
    public void addMessage(String sumary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, sumary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void enviarMensajeContacto() {
        FacesContext context = FacesContext.getCurrentInstance();
        Usuario usuarioCorreo = new Usuario();
        usuarioCorreo.setEmail("sistema.sigueme@gmail.com");
        List<Usuario> usuariosCorreo = new ArrayList();
        try {
            usuariosCorreo.add(usuarioCorreo);
            Mail.send(usuariosCorreo, "Nuevo Mensaje",
                    usuario.getNombre() + " envió el siguiente mensaje:\n" + mensajeContacto + "\nCorreo de Contacto del Usuario: " + usuario.getEmail());
            this.usuario = null;
            this.usuario = new Usuario();
            this.mensajeContacto = null;
            this.cerrarDialogo();
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Se ha enviado correctamente tu mensaje"));

        } catch (UnsupportedEncodingException ex) {
        }

    }

    public void cerrarDialogo() {
        RequestContext request = RequestContext.getCurrentInstance();
        request.reset("contactoForm");
        request.execute("PF('contacto').hide()");

    }
    
    public void recuperarContrasena() {
        RequestContext req = RequestContext.getCurrentInstance();
        String newPassword = PasswordGenerator.getPassword();
        List<Usuario> userPassword = new ArrayList<Usuario>();
        Usuario user;
        try {
            List<Usuario> usuarios = this.usuarioFacadeLocal.verificarDatosParaRecuperarContrasena(this.usuario);
            if (!usuarios.isEmpty()) {
                user = usuarios.get(0);
                user.setClave(Crypto.Encriptar(newPassword));
                this.usuarioFacadeLocal.edit(user);
                userPassword.add(user);
                req.execute("PF('password').hide()");
                Mail.send(userPassword, "Recuperación de Contraseña", "Hola, recibimos tu solicitud de cambio de contraseña, así pues ingresa al siguiente link: " +
                        "http://23.20.193.102:4848/sigueme" + "\nCon la siguiente contraseña: " + newPassword);
                 FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Hemos enviado tu nueva cotraseña al correo"));
       
            } else {req.execute("PF('password').hide()");
                 FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Datos incorrecto, por favor verifique nuevamente"));
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
             FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Se ha presentado un error al procesar la información"));
       
        }
        this.usuario = new Usuario();

    }


    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getMensajeContacto() {
        return mensajeContacto;
    }

    public void setMensajeContacto(String mensajeContacto) {
        this.mensajeContacto = mensajeContacto;
    }   
    
}
    