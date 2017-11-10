/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controller;

import com.sigueme.backend.entities.Crypto;
import com.sigueme.backend.entities.Mail;
import com.sigueme.backend.entities.OportunidadDeAprendizaje;
import com.sigueme.backend.entities.OportunidadUsuario;
import com.sigueme.backend.entities.Rol;
import com.sigueme.backend.entities.Usuario;
import com.sigueme.backend.model.EstadoOportunidadFacadeLocal;
import com.sigueme.backend.model.IncidenciaFacadeLocal;
import com.sigueme.backend.model.OportunidadDeAprendizajeFacadeLocal;
import com.sigueme.backend.model.OportunidadUsuarioFacadeLocal;
import com.sigueme.backend.model.RolFacadeLocal;
import com.sigueme.backend.model.UsuarioFacadeLocal;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Santi
 */
@Named(value = "usuarioController")
@SessionScoped
public class UsuarioController implements Serializable {

    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    @EJB
    private RolFacadeLocal rolFacadeLocal;
    @EJB
    private IncidenciaFacadeLocal incidenciaFacadeLocal;
    @EJB
    private OportunidadDeAprendizajeFacadeLocal oportunidadFacadeLocal;
    @EJB
    private OportunidadUsuarioFacadeLocal oportunidadUsuarioFacadeLocal;
    @EJB
    private EstadoOportunidadFacadeLocal estadoOportunidadFacadeLocal;

    private OportunidadUsuario oportunidadUsuario;
    private OportunidadDeAprendizaje oportunidad;

    private Usuario usuario;
    private Rol rol;
    private List<Rol> roles;
    private boolean banderaRegistro = false;

    private String clave;
    private String nuevaClave;
    private boolean banderaModifyAcount = false;
    private boolean banderaEliminarUsuario = false;
    private int opcion = 0;

    private String nombre;
    private String contacto;
    private String mensaje;

    public UsuarioController() {
    }

    @PostConstruct
    public void init() {
        oportunidadUsuario = new OportunidadUsuario();
        usuario = new Usuario();
        rol = new Rol();
    }

    public void contactenos() {
        try {
            Usuario usr = new Usuario();
            usr.setEmail("sistema.sigueme@gmail.com");
            List<Usuario> ls = new ArrayList();
            ls.add(usr);
            Mail.send(ls, nombre + " - " + contacto, mensaje);
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Enviado: ", "estaremos en contacto, Muchas gracias."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error: ", "no pudimos enviar los datos"));
        }
    }

    public String autenticarUsuario() {
        String redireccion = null;
        Usuario usuarioValidado = null;
        try {
            String pass = Crypto.Encriptar(this.usuario.getClave());
            this.usuario.setClave(pass);
            usuarioValidado = usuarioFacadeLocal.iniciarSesion(this.usuario);
            if (usuarioValidado != null) {
                HttpSession sesion = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                sesion.setAttribute("usuario", usuarioValidado);
                sesion.setAttribute("rol", usuarioValidado.getRolesUsuario().get(0));
                redireccion = "pages/sigueme/sigueme?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "datos incorrectos"));
                RequestContext req = RequestContext.getCurrentInstance();
                req.reset("login");
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error: ", "Error al iniciar sesion"));
            System.out.println("Error:: "+e.getMessage());
        }
        return redireccion;
    }

    public void registrarUsuario() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {

            if (this.usuario.getRolesUsuario().size() == 1) {

                if (this.usuario.getRolesUsuario().get(0).getRolID() != 2
                        || (this.verificarRol() && (this.usuarioFacadeLocal.verificarExistenciaLider().isEmpty()))) {

                    if (this.usuarioFacadeLocal.verificarDocumento(this.usuario.getCedula()).isEmpty()) {

                        this.usuario.setEstado(true);
                        this.usuarioFacadeLocal.create(usuario);
                        this.banderaRegistro = true;
                        context.addMessage(
                                null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El usuario se ha registrado correctamente"));

                    } else {
                        context.addMessage(
                                null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Ese número de cedula ya se encuentra registrado"));
                    }
                } else {
                    context.addMessage(
                            null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "No puede agregar otro lider, debe inhabilitar al existente primero"));
                }
            } else {
                context.addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Solo debe seleccionar un rol"));
            }

        } catch (Exception e) {
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo registrar el usuario"));
        }
    }

    public List<Usuario> listarUsuarios() {
        if (verificarRol()) {
            return this.usuarioFacadeLocal.findAll();
        } else {
            return this.usuarioFacadeLocal.listar();
        }
    }

    public List<Usuario> listarAgentesOportunidad() {
        return this.usuarioFacadeLocal.listarAgentes();
    }

    public void eliminarUsuario(Usuario usuario) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Usuario p = (Usuario) session.getAttribute("usuario");
        FacesContext context = FacesContext.getCurrentInstance();
        this.usuario = usuario;
        try {
            if (this.usuario.getUsuarioID() != p.getUsuarioID()) {
                this.usuarioFacadeLocal.editarEstado(this.usuario);
                banderaEliminarUsuario = true;

                FacesContext.getCurrentInstance().addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "El Usuario se ha eliminado correctamente"));
            } else {
                context.addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "No puede eliminarse a sí mismo"));
            }

        } catch (Exception e) {
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se han eliminar el usuario"));
        }

    }

    public void editarUsuario(Usuario usuario) {
        this.usuario = usuario;
        this.rol = usuario.getRolesUsuario().get(0);
    }

    public void editarUsuario() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (this.usuario.getRolesUsuario().size() == 1) {

                if (this.usuario.getRolesUsuario().get(0).getRolID() != 2 || this.verificarRol()
                        && (this.usuarioFacadeLocal.verificarExistenciaLider().isEmpty())) {
                    this.usuarioFacadeLocal.edit(this.usuario);
                    context.addMessage(
                            null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Los datos se han modificado correctamente"));
                    this.banderaRegistro = true;
                    banderaEliminarUsuario = true;
                } else {
                    context.addMessage(
                            null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Ya existe un lider, no puedes crear otro"));
                }
            } else {
                context.addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Solo debe seleccionar un rol"));
            }
        } catch (Exception e) {
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se han modificado los datos"));
        }
    }

    public void modificarCuenta(Usuario usuario) {
        this.usuario = usuario;
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (Crypto.Desencriptar(this.usuario.getClave()).equals(this.clave)) {
                if (this.nuevaClave != null) {
                    this.usuario.setClave(Crypto.Encriptar(nuevaClave));
                    cerrarSesion();
                }
                this.usuarioFacadeLocal.edit(this.usuario);
                banderaRegistro = true;
                context.addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Los datos se han modificado correctamente"));

            } else {
                context.addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "La contraseña no coincide"));
            }
        } catch (Exception e) {
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se han modificado los datos"));
        }
    }

    public String cerrarSesion() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().invalidateSession();
        return "/index?faces-redirect=true";
    }

    public String saludar() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Usuario p = (Usuario) session.getAttribute("usuario");

        return p.getNombre();
    }

    public boolean verificarRol() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Rol r = (Rol) session.getAttribute("rol");
        boolean bandera = false;
        if (r.getRolID() == 4) {
            bandera = true;
        }
        return bandera;
    }

    public void abrirDialogoEliminarUsuario(Usuario usuario) {
        this.usuario = usuario;
        RequestContext r = RequestContext.getCurrentInstance();
        r.execute("PF('deleteUser').show()");
    }

    public void decidirSiAbrirDialo(int option, Usuario usuario) {
        this.opcion = option;
        boolean bandera = false;
        if (option == 1) {
            if (usuario.getRolesUsuario().get(0).getRolID() == 1) {
                bandera = true;
            } else {
                this.usuario = usuario;
                inactivarIncidenciasYOportunidadesAUsuarioElimiando();
            }
        } else if (option == 2) {
            if (this.usuario.getRolesUsuario().get(0).getRolID() == rol.getRolID()) {
                ocultarModalEditar();
            } else {
                if (this.usuario.getRolesUsuario().get(0).getRolID() == 3) {
                    bandera = true;
                } else {
                    ocultarModalEditar();
                }
            }
        }

        if (bandera) {
            if (usuario != null) {
                abrirDialogoEliminarUsuario(usuario);
            } else {
                abrirDialogoEliminarUsuario(this.usuario);
            }
        }

    }

    public void inactivarIncidenciasYOportunidadesAUsuarioElimiando() {
        boolean banderillaDelete = false;
        List<OportunidadUsuario> oportunidadesPorUsuario;
        List<OportunidadUsuario> oportunidadesPorUsuarioFinal = new ArrayList();
        List<OportunidadDeAprendizaje> oportunidadesDeAprendizaje;
        try {
            if (determinarAccionAlGuardarModalInactivarTodo(opcion)) {
                this.incidenciaFacadeLocal.inactivarIncidenciasDeUsuarioEliminado(usuario);
                oportunidadesDeAprendizaje = this.oportunidadFacadeLocal.listarOportunidadesPorUsuarioEliminado(usuario);
                for (OportunidadDeAprendizaje oportunidadAprendizaje : oportunidadesDeAprendizaje) {
                    oportunidadesPorUsuario = this.oportunidadUsuarioFacadeLocal.listarOportunidadesUsuarioPorOportunidad(oportunidadAprendizaje);
                    oportunidadesPorUsuarioFinal.addAll(oportunidadesPorUsuario);
                }

                for (int i = 0; i < oportunidadesPorUsuarioFinal.size(); i++) {
                    int contador = 0;
                    for (int j = 0; j < oportunidadesPorUsuarioFinal.size(); j++) {

                        if (oportunidadesPorUsuarioFinal.get(i).getOportunidadID().getOportunidadID()
                                == oportunidadesPorUsuarioFinal.get(j).getOportunidadID().getOportunidadID()) {
                            contador += 1;
                            if (contador > 1) {
                                break;
                            }
                        }
                    }

                    if (contador == 1) {
                        oportunidadesPorUsuarioFinal.get(i).getOportunidadID().setEstado(false);
                        this.oportunidadFacadeLocal.edit(oportunidadesPorUsuarioFinal.get(i).getOportunidadID());
                    }
                }

                this.oportunidadUsuario.setUsuarioID(usuario);
                this.oportunidadUsuario.setEstadoID(this.estadoOportunidadFacadeLocal.find(6));
                this.oportunidadUsuarioFacadeLocal.inactivarOportunidadesUsuarioAlEliminarUsuario(oportunidadUsuario);
                banderillaDelete = true;
                banderaEliminarUsuario = false;
            }

            if (banderillaDelete) {
                cerrarModalEditar();
                ocultarModal(4);
            }
            banderillaDelete = false;
            this.opcion = 0;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Se ha presentado un error al eliminar los registros de incidencias u oportunidades"));
        }

    }

    public boolean determinarAccionAlGuardarModalInactivarTodo(int option) {
        if (option == 1) {
            eliminarUsuario(this.usuario);
        } else if (option == 2) {
            editarUsuario();
        }

        if (banderaEliminarUsuario) {
            return true;
        } else {
            return false;
        }
    }

    public void ocultarModalRegistrar() {
        this.registrarUsuario();
        if (this.banderaRegistro) {
            this.ocultarModal(1);
            this.banderaRegistro = false;
        }
    }

    public void ocultarModalEditar() {
        this.editarUsuario();
        if (this.banderaRegistro) {
            this.ocultarModal(2);
            this.banderaRegistro = false;
        }

    }

    public void cerrarModalEditar() {
        this.ocultarModal(2);
        usuario = new Usuario();
    }

    public void ocultarModificarCuenta(Usuario usuario) {
        this.modificarCuenta(usuario);
        if (this.banderaRegistro) {
            this.ocultarModal(3);
            this.banderaRegistro = false;
        }
    }

    public void ocultarModal(int option) {
        RequestContext req = RequestContext.getCurrentInstance();
        String form = null;
        switch (option) {
            case 1:
                req.execute("PF('registrarUsuario').hide()");
                form = "registerUser";
                break;
            case 2:
                req.execute("PF('editarUsuario').hide()");
                form = "f";
                break;
            case 3:
                req.execute("PF('modifyAccount').hide()");
                form = "modify";
                cerrarBandera();
                break;
            case 4:
                req.execute("PF('deleteUser').hide()");
            default:
                break;
        }

        if (form != null) {
            req.reset(form);
        }
        this.usuario = new Usuario();
        usuario = null;
    }

    public void verificarBandera() {
        if (banderaModifyAcount) {
            banderaModifyAcount = false;
        } else {
            banderaModifyAcount = true;
        }
    }

    public void cerrarBandera() {
        banderaModifyAcount = false;
    }

    public String mostrarAlgo() {
        String nombre;
        if (banderaModifyAcount) {
            nombre = "User.Modal.modifyAcount.hideFields";
        } else {
            nombre = "User.Modal.modifyAcount.changePassword";
        }
        return nombre;
    }

    public String verificarEstado(boolean estado) {
        if (estado) {
            return "Activo";
        } else {
            return "Inactivo";
        }
    }

    public boolean verificarRolEnsesionParsEditar(Usuario u) {
        HttpSession sesion = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuario");
        if (Objects.equals(user.getUsuarioID(), u.getUsuarioID())) 
            return true;
        else 
            return false;
    }

    public void abrirModalRegistrar() {
        RequestContext req = RequestContext.getCurrentInstance();
        this.usuario = null;
        this.usuario = new Usuario();
        req.reset("registerUser");
        req.execute("PF('registrarUsuario').show()");
    }

    public void recuperarContrasena() {
        //aunque el método debería estar aquí dirijase a MensaeController. recuperarContrasena()
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<Rol> getRoles() {
        this.roles = this.rolFacadeLocal.listarRoles();
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    public boolean isBanderaRegistro() {
        return banderaRegistro;
    }

    public void setBanderaRegistro(boolean banderaRegistro) {
        this.banderaRegistro = banderaRegistro;
    }

    public String getNuevaClave() {
        return nuevaClave;
    }

    public void setNuevaClave(String nuevaClave) {
        this.nuevaClave = nuevaClave;
    }

    public boolean isBanderaModifyAcount() {
        return banderaModifyAcount;
    }

    public void setBanderaModifyAcount(boolean banderaModifyAcount) {
        this.banderaModifyAcount = banderaModifyAcount;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public int getOpcion() {
        return opcion;
    }

    public void setOpcion(int opcion) {
        this.opcion = opcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}
