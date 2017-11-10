/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controller;

import com.lowagie.text.Chunk;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.sigueme.backend.entities.Categoria;
import com.sigueme.backend.entities.ErrorFrecuente;
import com.sigueme.backend.entities.Incidencia;
import com.sigueme.backend.entities.Rol;
import com.sigueme.backend.entities.Usuario;
import com.sigueme.backend.model.ErrorFrecuenteFacadeLocal;
import com.sigueme.backend.model.IncidenciaFacadeLocal;
import com.sigueme.backend.model.UsuarioFacadeLocal;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import jxl.Sheet;
import jxl.Workbook;
import org.primefaces.component.export.ExcelExporter;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Santi
 */
@Named(value = "exportController")
@RequestScoped
public class ExportController implements Serializable {

    private ExcelExporter excelOportion;
    private UploadedFile file;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    @EJB
    private IncidenciaFacadeLocal incidenciaFacadeLocal;
    @EJB
    private ErrorFrecuenteFacadeLocal errorFacadeLocal;
    private Usuario usuario;
    private Rol rol;
    private Incidencia incidencia;
    private Categoria categoria;
    private ErrorFrecuente errorFrecuente;

    public String upload() {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("archivos");
        path = path.substring(0, path.indexOf("\\build\\"));
        path += "\\Web\\archivos\\";
        String pathReal = null;
        if (file != null) {
            try {
                String nombre = file.getFileName();
                path += nombre;
                pathReal = "/archivos/" + nombre;
                InputStream entrada = file.getInputstream();
                byte[] datos = new byte[entrada.available()];
                entrada.read(datos);
                FileOutputStream salida = new FileOutputStream(path);
                salida.write(datos);
                salida.close();
            } catch (Exception w) {
                System.out.println(w.getMessage());
            }
        }
        return path;
    }

    public void cargaMasiva() {
        try {
            usuario = new Usuario();
            rol = new Rol();
            List<Rol> rolesUsuario = new ArrayList();
            String path = this.upload();
            Workbook workbook = Workbook.getWorkbook(new File(path));
            Sheet sheet = workbook.getSheet(0);
            for (int fila = 1; fila < (sheet.getRows()); fila++) {
                usuario.setCedula(sheet.getCell(0, fila).getContents());
                usuario.setNombre(sheet.getCell(1, fila).getContents());
                usuario.setApellido(sheet.getCell(2, fila).getContents());
                Integer.parseInt(sheet.getCell(3, fila).getContents());
                rol.setRolID(Integer.parseInt(sheet.getCell(3, fila).getContents()));
                rolesUsuario.add(rol);
                usuario.setRolesUsuario(rolesUsuario);
                usuario.setEmail(sheet.getCell(4, fila).getContents());
                usuarioFacadeLocal.create(usuario);
                usuario = new Usuario();
                rol = new Rol();
                rolesUsuario = new ArrayList();
            }

            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Usuarios cargados correctamente"));
        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Se ha producido un error al cargar los usuarios"));
        }

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getFlash().setKeepMessages(true);
        try {
            ec.redirect(ec.getRequestContextPath() + "/faces" + ec.getRequestPathInfo());
        } catch (IOException ex) {
            Logger.getLogger(ExportController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void cargaMasivaIncidencias() {
        try {
            incidencia = new Incidencia();
            usuario = new Usuario();
            categoria = new Categoria();
            errorFrecuente = new ErrorFrecuente();
            List<ErrorFrecuente> errores = new ArrayList();
            String path = this.upload();
            Workbook workbook = Workbook.getWorkbook(new File(path));
            Sheet sheet = workbook.getSheet(0);
            for (int fila = 1; fila < (sheet.getRows()); fila++) {

                incidencia.setEstado(true);
                incidencia.setNumeroDeCaso(sheet.getCell(0, fila).getContents());

                usuario = usuarioFacadeLocal.find(Integer.parseInt(sheet.getCell(1, fila).getContents()));
                categoria.setCategoriaID(Integer.parseInt(sheet.getCell(2, fila).getContents()));
                
                incidencia.setDescripcion(sheet.getCell(3, fila).getContents());
                incidencia.setUsuarioID(usuario);
                incidencia.setCategoriaID(categoria);
                                
                String  [] erroresFrecuentes = sheet.getCell(4, fila).getContents().split(",");
                for(int e = 0; e < erroresFrecuentes.length; e++) {
                    errorFrecuente = errorFacadeLocal.find(Integer.parseInt(erroresFrecuentes[e]));
                    errores.add(errorFrecuente);
                }
                incidencia.setErroresIncidencia(errores);
                
                incidenciaFacadeLocal.create(incidencia);
                
                incidencia = new Incidencia();
                usuario = new Usuario();
                categoria = new Categoria();
                errores = new ArrayList();
                erroresFrecuentes = new String[0];

            }
                FacesContext.getCurrentInstance().addMessage(
                        null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Las incidencia han sido cargadas satisfactoriamente"));

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Se ha producido un error al cargar las incidencias"));

        }
        
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getFlash().setKeepMessages(true);
        try {
            ec.redirect(ec.getRequestContextPath() + "/faces" + ec.getRequestPathInfo());
        } catch (IOException ex) {
            Logger.getLogger(ExportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

}
