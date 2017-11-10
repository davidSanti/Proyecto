/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controller;

import com.sigueme.backend.entities.Categoria;
import com.sigueme.backend.entities.EstadoOportunidad;
import com.sigueme.backend.entities.FortalezaDeConocimiento;
import com.sigueme.backend.entities.Incidencia;
import com.sigueme.backend.entities.Mail;
import com.sigueme.backend.entities.OportunidadDeAprendizaje;
import com.sigueme.backend.entities.OportunidadUsuario;
import com.sigueme.backend.entities.Rol;
import com.sigueme.backend.entities.TipoDeOportunidad;
import com.sigueme.backend.entities.Usuario;
import com.sigueme.backend.model.CategoriaFacadeLocal;
import com.sigueme.backend.model.EstadoOportunidadFacadeLocal;
import com.sigueme.backend.model.IncidenciaFacadeLocal;
import com.sigueme.backend.model.OportunidadDeAprendizajeFacadeLocal;
import com.sigueme.backend.model.OportunidadUsuarioFacadeLocal;
import com.sigueme.backend.model.TipoDeOportunidadFacadeLocal;
import com.sigueme.backend.model.UsuarioFacadeLocal;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Santi
 */
@Named(value = "oportunidadController")
@ViewScoped
public class OportunidadController implements Serializable {

    @EJB
    private OportunidadDeAprendizajeFacadeLocal oportunidadFacadeLocal;
    @EJB
    private TipoDeOportunidadFacadeLocal tipoFacadeLocal;
    @EJB
    private OportunidadUsuarioFacadeLocal oportunidadUsuarioFacadeLocal;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    @EJB
    private EstadoOportunidadFacadeLocal estadoFacadeLocal;
    @EJB
    private CategoriaFacadeLocal categoriaFacadeLocal;
    @EJB
    private IncidenciaFacadeLocal incidenciaFacadeLocal;
    private List<Incidencia> incidenciasOportunidad;

    private OportunidadDeAprendizaje oportunidad;
    private OportunidadUsuario oportunidadUsuario;
    private Usuario usuario;
    private EstadoOportunidad estadoOportunidad;

    private List<TipoDeOportunidad> tiposDeOportunidad;
    private List<Usuario> usuarios;
    private List<EstadoOportunidad> estadosOportunidad;
    private List<Usuario> pruebaUsuarios;
    private boolean banderaRegistro = false;
    private boolean banderaSelect = false;
    private boolean banderaModalFortaleza = true;
    private int estadoInicialOportunidadUsuario = 0;
    private Date nuevaFechaFin;

    private UploadedFile file;
    private StreamedContent downloadfile;
    private FortalezaDeConocimiento fortalezaDeConocimiento;

    private String fechaInicio;
    private String horaInicio;
    private String fechaFin;
    private String horaFin;

    @Inject
    FortalezaController fortalezaController;

    public OportunidadController() {
    }

    @PostConstruct
    public void init() {
        oportunidad = new OportunidadDeAprendizaje();
        oportunidadUsuario = new OportunidadUsuario();
        usuario = new Usuario();
        fortalezaDeConocimiento = new FortalezaDeConocimiento();
    }

    public void registrarOportunidad() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (usuario.getRolesUsuario().get(0).getRolID() != 2) {
            this.usuarios = new ArrayList();
            usuarios.add(usuario);
        }
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date startDate = df.parse(this.fechaInicio + " " + this.horaInicio.substring(0, 5));
            Date endDate = df.parse(this.fechaFin + " " + this.horaFin.substring(0, 5));
            this.oportunidad.setFechaInicio(startDate);
            this.oportunidad.setFechaFin(endDate);
            if (this.oportunidad.getFechaInicio().before(this.oportunidad.getFechaFin())
                    && this.oportunidad.getFechaFin().after(Date.from(Instant.now()))
                    && this.oportunidad.getFechaInicio().after(Date.from(Instant.now()))) {
                Usuario u;
                oportunidad.setOportunidadesUsuarios(new ArrayList<OportunidadUsuario>());
                for (int i = 0; i < usuarios.size(); i++) {
                    OportunidadUsuario ou = new OportunidadUsuario();
                    u = usuarios.get(i);
                    ou.setOportunidadID(oportunidad);
                    ou.setUsuarioID(usuario);
                    ou.setUsuarioID(u);
                    ou.setNumeroDeIntentos(1);
                    ou.setEstadoID(new EstadoOportunidad(1));
                    oportunidad.getOportunidadesUsuarios().add(ou);
                }
                this.oportunidadFacadeLocal.create(oportunidad);
                this.banderaRegistro = true;
                context.addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "La Oportunidad se ha registrado correctamente"));
                this.enviarCorreo(usuarios, oportunidad,
                        "Nueva Oportunidad De Aprendizaje",
                        "Tienes una nueva oportunidad de aprendizaje, dale un vistazo.");
                this.oportunidad = new OportunidadDeAprendizaje();

            } else {
                context.addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Verifique que las fechas sea correctas"));
            }

        } catch (Exception e) {
            System.out.println("eeeeee + " + e.getMessage());
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo registrar la oportunidad"));

        }
    }

    public String cargarAdjunto() {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("archivos");
        path = path.substring(0, path.indexOf("\\build\\"));
        path += "\\Web\\archivos\\";
        String pathReal = null;

        try {
            String nombre = file.getFileName();
            path += nombre;
            pathReal = "/archivos/" + nombre;

            InputStream input = file.getInputstream();
            byte[] data = new byte[input.available()];
            input.read(data);
            FileOutputStream output = new FileOutputStream(path);
            output.write(data);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pathReal;
    }

    public void enviarCorreo(List<Usuario> usuariosCorreo, OportunidadDeAprendizaje oportunidadAprendizaje, String asunto, String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hora = new SimpleDateFormat("HH:mm");
        try {
            Mail.send(usuariosCorreo, asunto, mensaje
                    + "\n\nNombre: " + oportunidadAprendizaje.getNombre()
                    + "\nDescripción: " + oportunidadAprendizaje.getDescripcion()
                    + "\nFecha de Finalizacion: " + fecha.format(oportunidadAprendizaje.getFechaFin())
                    + " " + hora.format(oportunidadAprendizaje.getFechaFin()));
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Correo enviado exitosamente"));
        } catch (Exception e) {
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo enviar el correo"));
        }
    }

    public void enviarCorreoAlEditarOportunidad(List<Usuario> usuariosChecked, List<Usuario> usuariosUnChecked, OportunidadDeAprendizaje oportunity) {
        try {
            if (!usuariosChecked.isEmpty()) {
                this.enviarCorreo(usuariosChecked, oportunity,
                        "Nueva Oportunidad de Aprendizaje",
                        "Tienes una nueva oportunidad de aprendizaje, dale un vistazo.");
            } else if (!usuariosUnChecked.isEmpty()) {
                this.enviarCorreo(usuariosUnChecked, oportunity,
                        "Oportunidad de Aprendizaje",
                        "Has sido desmarcado de la oportunidad de la aprendizaje: ");
            }
        } catch (Exception e) {
            System.out.println("Error al enviar correo edit: " + e.getMessage());
        }
    }

    public void enviarCorreoAlEnviarOportunidad(Usuario agente, OportunidadDeAprendizaje oportunidadAgente) {
        List<Usuario> usuariosCorreo = this.usuarioFacadeLocal.verificarExistenciaLider();
        SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hora = new SimpleDateFormat("HH:mm");
        try {

            Mail.send(usuariosCorreo, "Envío de Actividad", agente.getNombre() + " " + agente.getApellido()
                    + " ha enviado la actividad correspondiente a la oportunidad: "
                    + "\n\nNombre: " + oportunidadAgente.getNombre()
                    + "\nDescripción: " + oportunidadAgente.getDescripcion()
                    + "\nFecha de Finalizacion: " + fecha.format(oportunidadAgente.getFechaFin())
                    + " " + hora.format(oportunidadAgente.getFechaFin()));

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(OportunidadController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<OportunidadDeAprendizaje> listarOportunidades() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        usuario = (Usuario) session.getAttribute("usuario");
        //Aquí está el procedimiento almacenado para inhabilitar la oportunidades de aprendizaje
        this.oportunidadFacadeLocal.inhabilitarOportunidades();
        this.oportunidadUsuarioFacadeLocal.eliminarOportunidadUsuario();

        if (usuario.getRolesUsuario().get(0).getRolID() == 1) {
            return this.oportunidadFacadeLocal.misOportunidades(usuario);
        } else {
            return this.oportunidadFacadeLocal.listarLider();
        }
    }

    public void calificarOportunidad(OportunidadUsuario p) {
        this.oportunidadUsuario = p;
        estadoInicialOportunidadUsuario = oportunidadUsuario.getEstadoID().getEstadoID();
        if (this.oportunidadUsuario.getArchivo() != null && !this.oportunidadUsuario.getArchivo().equals("")) {
            descargarAdjunto();
        }
    }

    public void calificarOportunidad() {
        try {
            if (verificarFechaFinRequerida()) {
                banderaRegistro = true;
                if (fechaFin != null && !fechaFin.equals("")) {
                    OportunidadDeAprendizaje oportunity = this.oportunidadUsuario.getOportunidadID();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date endDate = df.parse(this.fechaFin + " " + this.horaFin.substring(0, 5));
                    oportunity.setFechaFin(endDate);
                    this.oportunidadUsuario.setNumeroDeIntentos(oportunidadUsuario.getNumeroDeIntentos() + 1);
                    if (verificarFechaFin(oportunity)) {
                        oportunidadFacadeLocal.edit(oportunity);
                    } else {
                        FacesContext.getCurrentInstance().addMessage(
                                null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "La fecha fin no puede ser menor a la fecha actual"));
                        banderaRegistro = false;
                    }
                }
                if (banderaRegistro) {
                    this.oportunidadUsuarioFacadeLocal.edit(oportunidadUsuario);
                    ocultarModal(4);
                    FacesContext.getCurrentInstance().addMessage(
                            null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "La oportunidad se ha calificado correctamente"));
                }
            }

        } catch (Exception e) {
            System.out.println("mensaje : " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "La oportunidad no se ha podido calificar"));
        }

    }

    public boolean verificarFechaFinRequerida() {
        boolean banderilla = true;
        if (banderaSelect) {
            if (fechaFin.isEmpty() || fechaFin == null || fechaFin.equals("")) {
                FacesContext.getCurrentInstance().addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "La fecha es un campo obligatorio"));
                banderilla = false;
            }
            if (horaFin.isEmpty() || horaFin == null || horaFin.equals("")) {
                FacesContext.getCurrentInstance().addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "La hora es un campo obligatorio"));
                banderilla = false;
            }
        }

        return banderilla;
    }

    public void editarOportunidadUsuario(int opcion) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        usuario = (Usuario) session.getAttribute("usuario");

        try {
            if (this.file != null && !(file.getFileName().equals(""))) {
                this.oportunidadUsuario.setArchivo(this.cargarAdjunto());
            }
            this.oportunidadUsuario.setOportunidadID(this.getOportunidad());
            this.oportunidadUsuario.setUsuarioID(usuario);

            //Esto la inactiva para el agente
            this.oportunidadUsuario.setEstadoID(this.estadoFacadeLocal.find(4));
            this.oportunidadUsuarioFacadeLocal.editarOportunidadUsuario(oportunidadUsuario);
            banderaRegistro = true;
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Se ha enviado correctamente la oportunidad"));
            //Esto es para que le envie correo al lider 
            this.enviarCorreoAlEnviarOportunidad(usuario, this.oportunidadUsuario.getOportunidadID());

            if (opcion == 1) {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.getFlash().setKeepMessages(true);
                ec.redirect(ec.getRequestContextPath() + "/faces" + ec.getRequestPathInfo());
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo enviar la oportunidad"));
        }
    }

    public void editarOportunidad(OportunidadDeAprendizaje oportunidad) {
        this.oportunidad = oportunidad;
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.fechaInicio = sdf.format(this.oportunidad.getFechaInicio()).substring(0, 10);
        this.fechaFin = sdf.format(this.oportunidad.getFechaFin()).substring(0, 10);
        this.horaInicio = sdf.format(this.oportunidad.getFechaInicio()).substring(11, 16);
        this.horaFin = sdf.format(this.oportunidad.getFechaFin()).substring(11, 16);
        usuarios = new ArrayList<>();
        for (OportunidadUsuario oportunidadesUsuario : oportunidad.getOportunidadesUsuarios()) {
            usuarios.add(oportunidadesUsuario.getUsuarioID());
        }

    }

    public void asignarOportunidadParaVerDetalle(OportunidadDeAprendizaje oportunidad) {
        this.oportunidad = oportunidad;
        mostrarModalDetalle();
    }

    public void editarOportunidad() {
        FacesContext context = FacesContext.getCurrentInstance();
        String redirect = "editOportunity";
        List<Usuario> userChecked = new ArrayList();
        List<Usuario> userUnchecked = new ArrayList();
        String nombreAgentes = "";
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date startDate = df.parse(this.fechaInicio + " " + this.horaInicio.substring(0, 5));
            Date endDate = df.parse(this.fechaFin + " " + this.horaFin.substring(0, 5));
            this.oportunidad.setFechaInicio(startDate);
            this.oportunidad.setFechaFin(endDate);
            if (this.oportunidad.getFechaInicio().before(this.oportunidad.getFechaFin())
                    && this.oportunidad.getFechaFin().after(Date.from(Instant.now()))) {
                for (Usuario usu : usuarios) {
                    boolean bandera = false;
                    for (OportunidadUsuario ou : oportunidad.getOportunidadesUsuarios()) {
                        if (ou.getUsuarioID().getUsuarioID() == usu.getUsuarioID()) {
                            bandera = true;
                        }
                    }
                    if (!bandera) {
                        OportunidadUsuario opUsuario = new OportunidadUsuario();
                        opUsuario.setOportunidadID(oportunidad);
                        opUsuario.setUsuarioID(usu);
                        oportunidad.getOportunidadesUsuarios().add(opUsuario);
                        userChecked.add(usu);
                    }
                }

                List<OportunidadUsuario> agentesPorOportunidad = this.oportunidadFacadeLocal.verificarOportunidadesRealizadas(this.oportunidad);
                for (int i = 0; i < oportunidad.getOportunidadesUsuarios().size(); i++) {
                    boolean bandera = true;
                    boolean banderilla = false;
                    for (Usuario usuarioRemove : usuarios) {
                        if (usuarioRemove.getUsuarioID() == oportunidad.getOportunidadesUsuarios().get(i).getUsuarioID().getUsuarioID()) {
                            bandera = false;
                        }
                    }
                    if (bandera) {
                        for (OportunidadUsuario agentesOportuniad : agentesPorOportunidad) {
                            if (oportunidad.getOportunidadesUsuarios().get(i).getUsuarioID().getUsuarioID()
                                    == agentesOportuniad.getUsuarioID().getUsuarioID()) {
                                nombreAgentes += agentesOportuniad.getUsuarioID().getNombre() + " , ";
                                banderilla = true;
                            }
                        }
                        if (!banderilla) {
                            userUnchecked.add(oportunidad.getOportunidadesUsuarios().get(i).getUsuarioID());
                            oportunidad.getOportunidadesUsuarios().remove(i);
                            i--;
                        }
                    }
                }

                if (!nombreAgentes.equals("")) {
                    context.addMessage(
                            null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "los agentes " + nombreAgentes + " ya enviaron la actividad correspondiente a ésta Oportunidad"));
                }

                this.oportunidadFacadeLocal.edit(oportunidad);
                this.banderaRegistro = true;
                this.enviarCorreoAlEditarOportunidad(userChecked, userUnchecked, oportunidad);
                context.addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "La Oportunidad se ha modificado correctamente"));

            } else {
                context.addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Verifique que las fechas sea correctas"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo modificar la Oportunidad"));

        }

    }

    public void eliminarOportunidad(OportunidadDeAprendizaje oportunidad) {
        FacesContext context = FacesContext.getCurrentInstance();
        this.oportunidad = oportunidad;
        List<OportunidadUsuario> listaOportunidadesUsuario;
        Date now = new Date(System.currentTimeMillis());
        String nombreAgente = "";
        boolean banderilla = false;

        try {
            if (this.oportunidad.getFechaInicio().after(now)) {
                banderilla = true;
            } else {
                listaOportunidadesUsuario = this.oportunidadFacadeLocal.verificarOportunidadesRealizadas(this.oportunidad);
                if (listaOportunidadesUsuario.isEmpty()) {
                    banderilla = true;
                } else {
                    for (int i = 0; i < listaOportunidadesUsuario.size(); i++) {
                        nombreAgente += listaOportunidadesUsuario.get(i).getUsuarioID().getNombre() + " , ";
                    }
                    context.addMessage(
                            null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se puede eliminar", "los agentes " + nombreAgente + " ya enviaron la actividad correspondiente a ésta Oportunidad"));
                }
            }
            if (banderilla) {
                List<Usuario> usuariosCorreo = this.oportunidadFacadeLocal.listarAgentesPorOportunidad(this.oportunidad);
                this.oportunidad.setEstado(false);
                this.oportunidadFacadeLocal.edit(this.oportunidad);
                this.oportunidadFacadeLocal.editarEstado(this.oportunidad);
                context.addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "La Oportunidad se ha eliminado correctamente"));
                enviarCorreo(usuariosCorreo, this.oportunidad, "Oportunidad Eliminada", "La siguiente Oportunidad se ha cancelado: ");
            }
        } catch (Exception e) {
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo eliminar la Oportunidad"));
        }
    }

    public void mostrarModalDetalle() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Rol rol = (Rol) session.getAttribute("rol");
        RequestContext req = RequestContext.getCurrentInstance();

        if (rol.getRolID() == 1
                && oportunidad.getTipoOportunidadID().getTipoID() == 1) {
            req.execute("PF('verOportunidadFortaleza').show()");
        } else {
            req.execute("PF('verOportunidad').show()");
        }
    }

    public void ocultarModal(int option) {
        RequestContext req = RequestContext.getCurrentInstance();
        String form = null;
        switch (option) {
            case 1:
                req.execute("PF('registrarOportunidad').hide()");
                form = "registerForm";
                break;
            case 2:
                req.execute("PF('editarOportunidad').hide()");
                form = "f";
                break;
            case 3:
                req.execute("PF('verOportunidad').hide()");
                form = "verForm";
                break;
            case 4:
                req.execute("PF('verOportunidad').hide()");
                form = "ff";
                banderaSelect = false;
                break;
            case 5:
                req.execute("PF('verOportunidadFortaleza').hide()");
                form = "formViewOportunity:verFormStrength";
                banderaSelect = false;
                break;
            default:
                break;
        }
        req.reset(form);
        this.oportunidadUsuario = new OportunidadUsuario();
        this.oportunidad = new OportunidadDeAprendizaje();
        this.estadoOportunidad = new EstadoOportunidad();
        this.estadosOportunidad = null;
        this.usuarios = null;
        this.usuarios = new ArrayList();
        fortalezaDeConocimiento = new FortalezaDeConocimiento();
        fechaInicio = "";
        fechaFin = "";
        horaInicio = "";
        horaFin = "";
    }

    public void ocultarModalEditar() {
        this.editarOportunidad();
        if (banderaRegistro) {
            this.ocultarModal(2);
            this.banderaRegistro = false;
        }
    }

    public void ocultarModalRegistrar() {
        this.registrarOportunidad();
        if (banderaRegistro) {
            this.ocultarModal(1);
            this.banderaRegistro = false;
        }
        this.oportunidad = new OportunidadDeAprendizaje();
    }

    public void ocultarModalDetail(int opcion) {
        this.editarOportunidadUsuario(opcion);
        if (banderaRegistro) {
            this.ocultarModal(3);
            this.banderaRegistro = false;
        }
        this.oportunidad = new OportunidadDeAprendizaje();
        this.oportunidadUsuario = new OportunidadUsuario();
    }

    public void ocultarModalCalificar() {
        this.calificarOportunidad();
        if (banderaRegistro) {
            this.ocultarModal(4);
            banderaRegistro = false;
            estadoInicialOportunidadUsuario = 0;
        }
    }

    public List<OportunidadUsuario> listarOportunidadesUsuarios() {
        this.oportunidadFacadeLocal.inhabilitarOportunidades();
        this.oportunidadUsuarioFacadeLocal.eliminarOportunidadUsuario();
        return this.oportunidadUsuarioFacadeLocal.listarOportunidadesUsuario();
    }
    
    public boolean verificarEstadoOportunidad(OportunidadUsuario oportunidadUsuario) {
        if (oportunidadUsuario.getEstadoID().getEstadoID() == 2) {
            return true;
        } else {
            return false;
        }
    }

    public void abrirModalFortaleza() {
        RequestContext req = RequestContext.getCurrentInstance();
        try {
            banderaModalFortaleza = fortalezaController.mostrarFortalezaDeUsuarioPorOportunidad(oportunidadUsuario);
            if (!banderaModalFortaleza) {
                req.execute("PF('verOportunidad').show()");
                FacesContext.getCurrentInstance().addMessage(
                        "growlCalificarOportunities", new FacesMessage(FacesMessage.SEVERITY_WARN, "", "No se encuentra ningún registro"));
            }
        } catch (Exception e) {

        }
    }

    public void prb(AjaxBehaviorEvent event) {
        Object o = event.getComponent().getAttributes().get("value");
        if (o instanceof EstadoOportunidad) {
            this.oportunidadUsuario.setEstadoID((EstadoOportunidad) o);
            if (oportunidadUsuario.getEstadoID().getEstadoID() == 5
                    || oportunidadUsuario.getEstadoID().getEstadoID() == 3) {
                banderaSelect = true;
            } else {
                if (banderaSelect) {
                    banderaSelect = false;
                }
                nuevaFechaFin = null;
                fechaFin = "";
                horaFin = "";
            }
        }
    }

    public void verificarCalificacion(ValueChangeEvent event) {
        this.oportunidadUsuario.setEstadoID((EstadoOportunidad) event.getNewValue());
        if (oportunidadUsuario.getEstadoID().getEstadoID() == 5) {
            banderaSelect = true;
        } else {
            if (banderaSelect) {
                banderaSelect = false;
            }
        }
    }

    public void enviarOportunidadConFortaleza() {
        try {
            if (asignarFortaleza()) {
                this.oportunidadUsuario.setDescripcion("Haga click en el botón para ver la fortaleza de conocimiento");
                this.editarOportunidadUsuario(1);
                if (banderaRegistro) {
                    this.ocultarModal(3);
                    this.banderaRegistro = false;
                }
                this.oportunidad = new OportunidadDeAprendizaje();
                this.oportunidadUsuario = new OportunidadUsuario();
            } else {
                FacesContext.getCurrentInstance().addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Algo ocurrió al intentar registrar la fortaleza"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "Error al enviar la oportunidad"));
        }

    }

    public boolean asignarFortaleza() {
        this.fortalezaDeConocimiento.setOportunidadID(this.oportunidad);
        if (this.file != null && !file.getFileName().equals("")) {
            this.fortalezaDeConocimiento.setArchivo(this.cargarAdjunto());
            this.file = null;
        }
        if (!fortalezaDeConocimiento.getNombre().equals("")
                && !fortalezaDeConocimiento.getDescripcion().equals("")) {
            fortalezaController.asignarFortaleza(this.fortalezaDeConocimiento);
            return true;
        } else {
            return false;
        }
    }

    public void inicializarUsuarios(Incidencia incidencia) {
        this.usuario = incidencia.getUsuarioID();
        RequestContext r = RequestContext.getCurrentInstance();
        r.execute("PF('registrarOportunidad').show()");
    }

    public boolean verificarFecha(OportunidadDeAprendizaje oportunidad) {
        if (oportunidad.getFechaFin().before(Date.from(Instant.now()))) {
            return true;
        }
        return false;
    }

    public boolean verificarFechaFin(OportunidadDeAprendizaje oportunidad) {
        if (oportunidad.getFechaFin().after(Date.from(Instant.now()))) {
            return true;
        }
        return false;
    }

    public void descargarAdjunto() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            if (this.oportunidadUsuario.getArchivo() != null) {
                String url = this.oportunidadUsuario.getArchivo();
                String path = fc.getExternalContext().getRealPath("/") + url;
                File f = new File(path);
                InputStream stream = (InputStream) new FileInputStream(f);
                downloadfile = new DefaultStreamedContent(stream, URLConnection.guessContentTypeFromStream(stream), f.getName());
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OportunidadController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OportunidadController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cerrarModalFortaleza(int opcion) {
        RequestContext req = RequestContext.getCurrentInstance();
        if (opcion == 1) {
            banderaModalFortaleza = false;
            req.execute("PF('verOportunidadFortaleza').show()");

        } else {
            req.execute("PF('verFortaleza').hide()");
            req.execute("PF('verOportunidad').show()");
        }
    }

    public List<Incidencia> retornarIncidenciasOp() {
        return incidenciasOportunidad;
    }

    public void sugerirOportunidades() {
        List<Usuario> usuariosIncidencia = usuarioFacadeLocal.listarAgentes();
        List<Categoria> categoriasIncidencia = categoriaFacadeLocal.listar();
        List<Incidencia> incidenciasParaOportunidad = new ArrayList();
        List<Incidencia> incidenciasTotal;

        try {
            for (int i = 0; i < usuariosIncidencia.size(); i++) {
                for (int j = 0; j < categoriasIncidencia.size(); j++) {
                    incidenciasTotal = this.incidenciaFacadeLocal.listarIncidenciasUsuariosPorCategoria(usuariosIncidencia.get(i), categoriasIncidencia.get(j));
                    if (!incidenciasTotal.isEmpty()) {
                        if (incidenciasTotal.size() > 6) {
                            Incidencia incidenciaOportunidad = new Incidencia();
                            incidenciaOportunidad.setUsuarioID(usuariosIncidencia.get(i));
                            incidenciaOportunidad.setCategoriaID(categoriasIncidencia.get(j));
                            incidenciasParaOportunidad.add(incidenciaOportunidad);

                        }
                    }
                }
            }
        } catch (Exception e) {

        }
        incidenciasOportunidad = incidenciasParaOportunidad;
    }

    public OportunidadDeAprendizaje getOportunidad() {
        return oportunidad;
    }

    public void setOportunidad(OportunidadDeAprendizaje oportunidad) {
        this.oportunidad = oportunidad;
    }

    public OportunidadUsuario getOportunidadUsuario() {
        return oportunidadUsuario;
    }

    public void setOportunidadUsuario(OportunidadUsuario oportunidadUsuario) {
        this.oportunidadUsuario = oportunidadUsuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public EstadoOportunidad getEstadoOportunidad() {
        return estadoOportunidad;
    }

    public void setEstadoOportunidad(EstadoOportunidad estadoOportunidad) {
        this.estadoOportunidad = estadoOportunidad;
    }

    public List<TipoDeOportunidad> getTiposDeOportunidad() {
        this.tiposDeOportunidad = tipoFacadeLocal.findAll();
        return tiposDeOportunidad;
    }

    public void setTiposDeOportunidad(List<TipoDeOportunidad> tiposDeOportunidad) {
        this.tiposDeOportunidad = tiposDeOportunidad;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<EstadoOportunidad> getEstadosOportunidad() {
        this.estadosOportunidad = estadoFacadeLocal.listarEstados();
        return estadosOportunidad;
    }

    public void setEstadosOportunidad(List<EstadoOportunidad> estadosOportunidad) {
        this.estadosOportunidad = estadosOportunidad;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public boolean isBanderaRegistro() {
        return banderaRegistro;
    }

    public void setBanderaRegistro(boolean banderaRegistro) {
        this.banderaRegistro = banderaRegistro;
    }

    public boolean isBanderaSelect() {
        return banderaSelect;
    }

    public void setBanderaSelect(boolean banderaSelect) {
        this.banderaSelect = banderaSelect;
    }

    public StreamedContent getDownloadfile() {
        return downloadfile;
    }

    public Date getNuevaFechaFin() {
        return nuevaFechaFin;
    }

    public void setNuevaFechaFin(Date nuevaFechaFin) {
        this.nuevaFechaFin = nuevaFechaFin;
    }

    public FortalezaDeConocimiento getFortalezaDeConocimiento() {
        return fortalezaDeConocimiento;
    }

    public void setFortalezaDeConocimiento(FortalezaDeConocimiento fortalezaDeConocimiento) {
        this.fortalezaDeConocimiento = fortalezaDeConocimiento;
    }

    public int getEstadoInicialOportunidadUsuario() {
        return estadoInicialOportunidadUsuario;
    }

    public void setEstadoInicialOportunidadUsuario(int estadoInicialOportunidadUsuario) {
        this.estadoInicialOportunidadUsuario = estadoInicialOportunidadUsuario;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public List<Incidencia> getIncidenciasOportunidad() {
        return incidenciasOportunidad;
    }

    public void setIncidenciasOportunidad(List<Incidencia> incidenciasOportunidad) {
        this.incidenciasOportunidad = incidenciasOportunidad;
    }

    public FortalezaController getFortalezaController() {
        return fortalezaController;
    }

    public void setFortalezaController(FortalezaController fortalezaController) {
        this.fortalezaController = fortalezaController;
    }

    public boolean isBanderaModalFortaleza() {
        return banderaModalFortaleza;
    }

    public void setBanderaModalFortaleza(boolean banderaModalFortaleza) {
        this.banderaModalFortaleza = banderaModalFortaleza;
    }

}
