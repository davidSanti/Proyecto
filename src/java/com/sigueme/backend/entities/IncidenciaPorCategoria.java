/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.entities;

/**
 *
 * @author Santi
 */
public class IncidenciaPorCategoria {

    private String nombreCategoria;
    private int numeroIncidencias;

    private int usuarioId;
    private String nombreUsuario;
    
    public IncidenciaPorCategoria() {
    }

    public IncidenciaPorCategoria(String nombreCategoria, int numeroIncidencias) {
        this.nombreCategoria = nombreCategoria;
        this.numeroIncidencias = numeroIncidencias;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public int getNumeroIncidencias() {
        return numeroIncidencias;
    }

    public void setNumeroIncidencias(int numeroIncidencias) {
        this.numeroIncidencias = numeroIncidencias;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    
    
    @Override
    public String toString() {
        return "IncidenciaPorCategoria{" + "nombreCategoria=" + nombreCategoria + ", numeroIncidencias=" + numeroIncidencias + '}';
    }
    
    

}
