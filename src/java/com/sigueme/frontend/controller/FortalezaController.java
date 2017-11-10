/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controller;

import com.sigueme.backend.entities.Categoria;
import com.sigueme.backend.entities.FortalezaDeConocimiento;
import com.sigueme.backend.entities.Incidencia;
import com.sigueme.backend.entities.OportunidadDeAprendizaje;
import com.sigueme.backend.entities.OportunidadUsuario;
import com.sigueme.backend.entities.Usuario;
import com.sigueme.backend.model.CategoriaFacadeLocal;
import com.sigueme.backend.model.FortalezaDeConocimientoFacadeLocal;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.net.URLConnection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Santi
 */
@Named(value = "fortalezaController")
@ViewScoped
public class FortalezaController implements Serializable {

    @EJB
    private FortalezaDeConocimientoFacadeLocal fortalezaFacadeLocal;
    @EJB
    private CategoriaFacadeLocal categoriaFacadeLocal;

    private FortalezaDeConocimiento fortaleza;
    private Categoria categoria;
    private List<Categoria> categorias;

    private UploadedFile file;
    private StreamedContent downloadfile;

    private boolean banderaRegistro = false;

    private boolean banderaModal;

    public FortalezaController() {
    }

    @PostConstruct
    public void init() {
        fortaleza = new FortalezaDeConocimiento();
        categoria = new Categoria();
    }

    public void registrarFortaleza() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Usuario p = (Usuario) session.getAttribute("usuario");
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (this.file != null && !file.getFileName().equals("")) {
                this.fortaleza.setArchivo(this.cargarAdjunto());
            }
            this.fortaleza.setUsuarioID(p);
            this.fortalezaFacadeLocal.create(fortaleza);
            banderaRegistro = true;
            if (this.fortaleza.getOportunidadID() == null) {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.getFlash().setKeepMessages(true);
            }
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "La Fortaleza de conocimiento se ha registrado correctamente"));

        } catch (Exception e) {
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo registrar la Fortaleza de conocimiento"));
        }
    }

    public void asignarOportunidadDeArendizaje(OportunidadDeAprendizaje oportunidad) {
        this.fortaleza.setOportunidadID(oportunidad);
    }

    public List<FortalezaDeConocimiento> listarFortalezas() {
        return this.fortalezaFacadeLocal.listar();
    }

    public void eliminarFortaleza(FortalezaDeConocimiento fortaleza) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            this.fortaleza = fortaleza;
            this.fortalezaFacadeLocal.cambiarEstado(this.fortaleza);
            listarFortalezas();
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "La Fortaleza de conocimiento se ha eliminado correctamente"));

        } catch (Exception e) {
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo eliminar la fortaleza de conocimiento"));
        }
    }

    public void editarFortaleza(FortalezaDeConocimiento fortaleza) {
        this.fortaleza = fortaleza;
    }

    public void mostrarFortaleza(FortalezaDeConocimiento fortaleza) {
        this.fortaleza = fortaleza;
        if (this.fortaleza.getArchivo() != null && !this.fortaleza.getArchivo().equals("")) {
            descargarAdjunto();
        }
    }

    public void editarFortaleza() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Usuario p = (Usuario) session.getAttribute("usuario");
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            this.fortaleza.setUsuarioID(p);
            this.fortalezaFacadeLocal.edit(fortaleza);
            banderaRegistro = true;
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "La Fortaleza de conocimiento se ha modificado correctamente"));

        } catch (Exception e) {
            context.addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "No se pudo modificar la fortaleza de conocimiento"));
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

    public boolean verificarUsuario(FortalezaDeConocimiento fortaleza) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Usuario u = (Usuario) session.getAttribute("usuario");
        boolean bandera = false;
        if (u.getUsuarioID() == fortaleza.getUsuarioID().getUsuarioID()) {
            bandera = true;
        }
        return bandera;
    }

    public boolean mostrarFortalezaDeUsuarioPorOportunidad(OportunidadUsuario oportunidadUsuario) {
        boolean bandera = false;
        List<FortalezaDeConocimiento> fortalezasPorAgenteOp = this.fortalezaFacadeLocal.listarFortalezasDeAgentePorOportunidad(
                oportunidadUsuario.getUsuarioID(), oportunidadUsuario.getOportunidadID());
        if (!fortalezasPorAgenteOp.isEmpty()) {
            mostrarFortaleza(fortalezasPorAgenteOp.get(0));
            RequestContext req = RequestContext.getCurrentInstance();
            req.execute("PF('verFortaleza').show()");
            bandera = true;
        } 
        return bandera;
    }

    public boolean asignarFortaleza(FortalezaDeConocimiento fortaleza) {
        boolean bandera = false;
        this.fortaleza = fortaleza;
        registrarFortaleza();
        if (banderaRegistro) {
            this.ocultarModal(4);
            banderaRegistro = false;
            bandera = true;
        }
        return bandera;
    }

    public void ocultarModalRegistrar() {
        boolean otherFlag = false;
        RequestContext req = RequestContext.getCurrentInstance();
        otherFlag = true;
        this.registrarFortaleza();
        if (banderaRegistro) {
            this.ocultarModal(1);
            banderaRegistro = false;
            file = null;
            otherFlag = true;
        }
        if (!otherFlag) {
            req.execute("PF('registrarFortaleza').show()");
        }
    }

    public void registrarFortalezaOportunidadDeAprnedizaje() {
        this.registrarFortaleza();
        if (banderaRegistro) {
            this.ocultarModal(4);
            banderaRegistro = false;
        }
    }

    public void ocultarModalEditar() {
        this.editarFortaleza();
        if (banderaRegistro) {
            this.ocultarModal(2);
            banderaRegistro = false;
        }
    }

    public void ocultarModal(int option) {
        RequestContext req = RequestContext.getCurrentInstance();
        String form = null;
        switch (option) {
            case 1:
                req.execute("PF('registrarFortaleza').hide()");
                form = "registerFormS";
                break;
            case 2:
                req.execute("PF('editarFortaleza').hide()");
                form = "fo";
                break;
            case 3:
                req.execute("PF('verFortaleza').hide()");
                form = "datailFormStrength";
                break;
            case 4:
                req.execute("PF('registrarFortalezaOp').hide()");
                form = "strengthRegisterForm";
                break;
            default:
                break;
        }
        req.reset(form);
        this.fortaleza = new FortalezaDeConocimiento();
        this.file = null;
    }

    public void descargarAdjunto() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            String url = this.fortaleza.getArchivo();
            String path = fc.getExternalContext().getRealPath("/") + url;
            File f = new File(path);
            InputStream stream = (InputStream) new FileInputStream(f);
            downloadfile = new DefaultStreamedContent(stream, URLConnection.guessContentTypeFromStream(stream), f.getName());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OportunidadController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OportunidadController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public FortalezaDeConocimiento getFortaleza() {
        return fortaleza;
    }

    public void setFortaleza(FortalezaDeConocimiento fortaleza) {
        this.fortaleza = fortaleza;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<Categoria> getCategorias() {
        this.categorias = categoriaFacadeLocal.listar();
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public StreamedContent getDownloadfile() {
        return downloadfile;
    }

    public void setDownloadfile(StreamedContent downloadfile) {
        this.downloadfile = downloadfile;
    }

    public boolean isBanderaModal() {
        return banderaModal;
    }

    public void setBanderaModal(boolean banderaModal) {
        this.banderaModal = banderaModal;
    }

}
