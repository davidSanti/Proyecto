/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Santi
 */
@Entity
@Table(name = "OportunidadesUsuarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OportunidadUsuario.findAll", query = "SELECT o FROM OportunidadUsuario o WHERE o.estadoID.estadoID NOT IN (6) ORDER BY o.oportunidadUsuarioID DESC")
    , @NamedQuery(name = "OportunidadUsuario.findByOportunidadUsuarioID", query = "SELECT o FROM OportunidadUsuario o WHERE o.oportunidadUsuarioID = :oportunidadUsuarioID")
    , @NamedQuery(name = "OportunidadUsuario.findByArchivo", query = "SELECT o FROM OportunidadUsuario o WHERE o.archivo = :archivo")})
@NamedStoredProcedureQuery(name = "eliminarOportunidadUsuario"
                          ,  resultClasses = String.class
                          , procedureName = "eliminarOportunidadUsuario")
public class OportunidadUsuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "OportunidadUsuarioID")
    private Integer oportunidadUsuarioID;
    
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "Descripcion")
    private String descripcion;
    
    @Size(max = 200)
    @Column(name = "Archivo")
    private String archivo;
    
    @JoinColumn(name = "EstadoID", referencedColumnName = "EstadoID", insertable = false, updatable = true)
    @ManyToOne(optional = false)
    private EstadoOportunidad estadoID;
        
    @JoinColumn(name = "OportunidadID", referencedColumnName = "OportunidadID")
    @ManyToOne(optional = false)
    private OportunidadDeAprendizaje oportunidadID;
    
    @JoinColumn(name = "UsuarioID", referencedColumnName = "UsuarioID")
    @ManyToOne(optional = false)
    private Usuario usuarioID;
    
    @Basic(optional = false)
    @Column(name = "NumeroDeIntentos", insertable = false)
    private int numeroDeIntentos;

    public OportunidadUsuario() {
    }

    public OportunidadUsuario(Integer oportunidadUsuarioID) {
        this.oportunidadUsuarioID = oportunidadUsuarioID;
    }

    public OportunidadUsuario(Integer oportunidadUsuarioID, String descripcion) {
        this.oportunidadUsuarioID = oportunidadUsuarioID;
        this.descripcion = descripcion;
    }

    public Integer getOportunidadUsuarioID() {
        return oportunidadUsuarioID;
    }

    public void setOportunidadUsuarioID(Integer oportunidadUsuarioID) {
        this.oportunidadUsuarioID = oportunidadUsuarioID;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public EstadoOportunidad getEstadoID() {
        return estadoID;
    }

    public void setEstadoID(EstadoOportunidad estadoID) {
        this.estadoID = estadoID;
    }

    public OportunidadDeAprendizaje getOportunidadID() {
        return oportunidadID;
    }

    public void setOportunidadID(OportunidadDeAprendizaje oportunidadID) {
        this.oportunidadID = oportunidadID;
    }

    public Usuario getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(Usuario usuarioID) {
        this.usuarioID = usuarioID;
    }

    public int getNumeroDeIntentos() {
        return numeroDeIntentos;
    }

    public void setNumeroDeIntentos(int numeroDeIntentos) {
        this.numeroDeIntentos = numeroDeIntentos;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (oportunidadUsuarioID != null ? oportunidadUsuarioID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OportunidadUsuario)) {
            return false;
        }
        OportunidadUsuario other = (OportunidadUsuario) object;
        if ((this.oportunidadUsuarioID == null && other.oportunidadUsuarioID != null) || (this.oportunidadUsuarioID != null && !this.oportunidadUsuarioID.equals(other.oportunidadUsuarioID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "oportunidadUsuarioID=" + oportunidadUsuarioID + " ]";
    }
    
}
