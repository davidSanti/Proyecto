/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Santi
 */
@Entity
@Table(name = "Incidencias")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Incidencia.findAll", query = "SELECT i FROM Incidencia i WHERE i.estado = true")
    , @NamedQuery(name = "Incidencia.findByIncidenciaID", query = "SELECT i FROM Incidencia i WHERE i.incidenciaID = :incidenciaID")
    , @NamedQuery(name = "Incidencia.findByNumeroDeCaso", query = "SELECT i FROM Incidencia i WHERE i.numeroDeCaso = :numeroDeCaso AND i.estado = true")
    , @NamedQuery(name = "Incidencia.findByFechaRegistro", query = "SELECT i FROM Incidencia i WHERE i.fechaRegistro = :fechaRegistro")
    , @NamedQuery(name = "Incidencia.findByEstado", query = "SELECT i FROM Incidencia i WHERE i.estado = :estado")
    , @NamedQuery(name = "Incidencia.listarPropias", query = "SELECT i FROM Incidencia i WHERE i.estado = true AND i.usuarioID.usuarioID = :usuarioID")})
public class Incidencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "IncidenciaID")
    private Integer incidenciaID;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "NumeroDeCaso")
    private String numeroDeCaso;
    
    @Column(name = "FechaRegistro", insertable = false, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    
    @Lob
    @Size(max = 2147483647)
    @Column(name = "Descripcion")
    private String descripcion;
    
    @Column(name = "Estado", insertable = false)
    private Boolean estado;
    
    @JoinTable(name = "ErroresIncidencia", joinColumns = {
        @JoinColumn(name = "IncidenciaID", referencedColumnName = "IncidenciaID")}, inverseJoinColumns = {
        @JoinColumn(name = "ErrorID", referencedColumnName = "ErrorID")})
    @ManyToMany
    private List<ErrorFrecuente> erroresIncidencia;
    
    @JoinColumn(name = "CategoriaID", referencedColumnName = "CategoriaID")
    @ManyToOne(optional = false)
    private Categoria categoriaID;
    
    @JoinColumn(name = "UsuarioID", referencedColumnName = "UsuarioID")
    @ManyToOne(optional = false)
    private Usuario usuarioID;

    public Incidencia() {
    }

    public Incidencia(Integer incidenciaID) {
        this.incidenciaID = incidenciaID;
    }

    public Incidencia(Integer incidenciaID, String numeroDeCaso) {
        this.incidenciaID = incidenciaID;
        this.numeroDeCaso = numeroDeCaso;
    }

    public Integer getIncidenciaID() {
        return incidenciaID;
    }

    public void setIncidenciaID(Integer incidenciaID) {
        this.incidenciaID = incidenciaID;
    }

    public String getNumeroDeCaso() {
        return numeroDeCaso;
    }

    public void setNumeroDeCaso(String numeroDeCaso) {
        this.numeroDeCaso = numeroDeCaso;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Categoria getCategoriaID() {
        return categoriaID;
    }

    public void setCategoriaID(Categoria categoriaID) {
        this.categoriaID = categoriaID;
    }

    public Usuario getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(Usuario usuarioID) {
        this.usuarioID = usuarioID;
    }

    public List<ErrorFrecuente> getErroresIncidencia() {
        return erroresIncidencia;
    }

    public void setErroresIncidencia(List<ErrorFrecuente> erroresIncidencia) {
        this.erroresIncidencia = erroresIncidencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (incidenciaID != null ? incidenciaID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Incidencia)) {
            return false;
        }
        Incidencia other = (Incidencia) object;
        if ((this.incidenciaID == null && other.incidenciaID != null) || (this.incidenciaID != null && !this.incidenciaID.equals(other.incidenciaID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "incidenciaID=" + incidenciaID;
    }
    
}
