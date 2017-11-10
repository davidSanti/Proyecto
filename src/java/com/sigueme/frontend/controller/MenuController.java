/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controller;

import com.sigueme.backend.entities.Permiso;
import com.sigueme.backend.entities.Rol;
import com.sigueme.backend.entities.Usuario;
import com.sigueme.backend.model.PermisoFacadeLocal;
import com.sigueme.backend.model.UsuarioFacadeLocal;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author Santi
 */
@Named(value = "menuController")
@SessionScoped
public class MenuController implements Serializable {

    @EJB
    private PermisoFacadeLocal permisoFacadeLocal;
    private List<Permiso> permisos;
    private MenuModel model;
    private Usuario usuario;
    private UsuarioFacadeLocal usuarioFacadeLocal;
    private Locale locale;
    private List<Locale> supportedLocales;

    private String localizacion;
    private static Map<String, Object> paises;
    
    static {
        paises = new LinkedHashMap<>();
        Locale espanol = new Locale("es");
        paises.put("Español", espanol);
        paises.put("English", Locale.ENGLISH);
    }

    public MenuController() {
    }

    @PostConstruct
    public void init() {
        model = new DefaultMenuModel();
        this.usuario = new Usuario();
        locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        supportedLocales = new ArrayList<>();
        permisos = new ArrayList<>();
        this.listarPermisos();
        this.establecerPermisos();
        Iterator<Locale> it = FacesContext.getCurrentInstance().getApplication().getSupportedLocales();
        while (it.hasNext()) {
            supportedLocales.add(it.next());
        }
    }

    public void verificarPermiso() throws IOException {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        FacesContext context = FacesContext.getCurrentInstance();
        String path = ((HttpServletRequest) context.getExternalContext().getRequest()).getContextPath();
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        Rol rol = (Rol) context.getExternalContext().getSessionMap().get("rol");
        List<Permiso> paginas = permisoFacadeLocal.permisosPorRol(rol);
        int contador = 0;
        String[] partes = uri.split("/", url.length());
        if (!url.contains("sigueme.xhtml")) {
            for (Permiso pag : paginas) {
                if (pag.getUrl() != null) {
                    if (pag.getUrl().contains(partes[partes.length - 1])) {
                        contador++;
                    }
                }
            }
            if (contador <= 0) {
                context.getExternalContext().redirect(path + "/faces/pages/sigueme/sigueme.xhtml");
            }
        }
    }

    public void localeChangeListener(ValueChangeEvent changeEvent) {
        locale = ((Locale) changeEvent.getNewValue());
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }

    public void cambiarIdioma(ValueChangeEvent event) {
        String nuevaLocalizacion = event.getNewValue().toString();
        setLocale(new Locale(nuevaLocalizacion));
    }

    public void cambiarIdiomaF(Locale locale) {
        setLocale(locale);
    }

    public void listarPermisos() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession sesion = (HttpSession) context.getExternalContext().getSession(true);
        String path = ((HttpServletRequest) context.getExternalContext().getRequest()).getContextPath();
        
        if (sesion.getAttribute("rol") != null) {
            Rol r = (Rol) sesion.getAttribute("rol");
            permisos = r.getPermisosRol();
        }
        if(permisos.isEmpty() || permisos == null){
            try {
                context.getExternalContext().redirect(path + "/faces/index.xhtml");
                context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "El usuario no tiene permisos"));

            } catch (IOException ex) {
                Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

     public void establecerPermisos() {
        ResourceBundle r = ResourceBundle.getBundle("/mensajes", locale);
        if (permisos != null) {
            for (Permiso permiso : permisos) {
                if (permiso.getTipo()) {
                    if (permiso.getDependencia() == null) {
                        DefaultMenuItem item = new DefaultMenuItem(r.containsKey(permiso.getDescripcion()) ? r.getString(permiso.getDescripcion()) : permiso.getDescripcion());
                        item.setUrl("/faces/" + permiso.getUrl());
                        model.addElement(item);
                    }
                } else {
                    DefaultSubMenu firtsSubMenu = new DefaultSubMenu(r.containsKey(permiso.getDescripcion()) ? r.getString(permiso.getDescripcion()) : permiso.getDescripcion());
                    for (Permiso permiso1 : permisos) {
                        Permiso subPermiso = permiso1.getDependencia();
                        if (subPermiso != null) {
                            if (subPermiso.getPermisoID() == permiso.getPermisoID()) {
                                DefaultMenuItem item = new DefaultMenuItem(r.containsKey(permiso1.getDescripcion()) ? r.getString(permiso1.getDescripcion()) : permiso1.getDescripcion());
                                item.setUrl("/faces/" + permiso1.getUrl());
                                firtsSubMenu.addElement(item);
                            }
                        }

                    }
                    model.addElement(firtsSubMenu);
                }
            }
        }

    }

     public void verificarSesion() {
        FacesContext context = FacesContext.getCurrentInstance();
        String path = ((HttpServletRequest) context.getExternalContext().getRequest()).getContextPath();
        try {
            Usuario usuario = (Usuario) context.getExternalContext().getSessionMap().get("usuario");
            String url = context.getExternalContext().getRequestPathInfo();
            if (usuario == null) {
                context.getExternalContext().redirect(path + "/faces/index.xhtml");
            }
            //else if (!tienePermiso(url)) {
            //context.getExternalContext().redirect(path + "/faces/pages/sigueme/sigueme.xhtml");
            //}
        } catch (Exception e) {
        }
    }

    public boolean tienePermiso(String url) {
        if (url.equals("/index.xhtml") || url.equals("") || url.equals("/pages/sigueme/sigueme.xhtml")) {
            return true;
        }

        for (Permiso p : permisos) {
            if (p.getUrl() != null && p.getUrl().endsWith(url.replaceFirst("/", ""))) {
                return true;
            }
        }
        return false;
    }
    
    public String cerrarSesion() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().invalidateSession();
        return "/index?faces-redirect=true";
    }

    public String mostrarNombreUsuario() {
        HttpSession sesion = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Usuario u = (Usuario) sesion.getAttribute("usuario");
        return u.getNombre();
    }

    public Usuario leerDatos() {
        HttpSession sesion = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Usuario usua = (Usuario) sesion.getAttribute("usuario");
        this.usuario = usua;
        return usuario;
    }

    public void editarUsuario() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            this.usuarioFacadeLocal.edit(this.usuario);
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Los datos se han modificado correctamente"));

        } catch (Exception e) {
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se han modificado los datos"));
            System.out.println("eeee" + e.getMessage());

        }

    }

    public List<Permiso> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<Permiso> permisos) {
        this.permisos = permisos;
    }

    public MenuModel getModel() {
        if (model.getElements().size() == 0) {
            listarPermisos();
            establecerPermisos();
        }
        return model;
    }

    public void setModel(MenuModel model) {
        this.model = model;
    }

    public Usuario getUsuario() {

        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public static Map<String, Object> getPaises() {
        return paises;
    }

    public static void setPaises(Map<String, Object> paises) {
        MenuController.paises = paises;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            model = new DefaultMenuModel();
            ec.redirect(ec.getRequestContextPath() + "/faces" + ec.getRequestPathInfo());
        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Locale> getSupportedLocales() {
        return supportedLocales;
    }

    public void setSupportedLocales(List<Locale> supportedLocales) {
        this.supportedLocales = supportedLocales;
    }

}
