/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "FortalezasDeConocimiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FortalezaDeConocimiento.findAll", query = "SELECT f FROM FortalezaDeConocimiento f WHERE f.estado = true")
    , @NamedQuery(name = "FortalezaDeConocimiento.findByFortalezaID", query = "SELECT f FROM FortalezaDeConocimiento f WHERE f.fortalezaID = :fortalezaID")
    , @NamedQuery(name = "FortalezaDeConocimiento.findByFechaRegistro", query = "SELECT f FROM FortalezaDeConocimiento f WHERE f.fechaRegistro = :fechaRegistro")
    , @NamedQuery(name = "FortalezaDeConocimiento.findByNombre", query = "SELECT f FROM FortalezaDeConocimiento f WHERE f.nombre = :nombre")
    , @NamedQuery(name = "FortalezaDeConocimiento.findByArchivo", query = "SELECT f FROM FortalezaDeConocimiento f WHERE f.archivo = :archivo")
    , @NamedQuery(name = "FortalezaDeConocimiento.findByEstado", query = "SELECT f FROM FortalezaDeConocimiento f WHERE f.estado = :estado")})
public class FortalezaDeConocimiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "FortalezaID")
    private Integer fortalezaID;
    
    @Column(name = "FechaRegistro", insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 70)
    @Column(name = "Nombre")
    private String nombre;
    
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "Descripcion")
    private String descripcion;
    
    @Size(max = 100)
    @Column(name = "Archivo")
    private String archivo;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "Estado", insertable = false)
    private boolean estado;
    
    @JoinColumn(name = "CategoriaID", referencedColumnName = "CategoriaID")
    @ManyToOne(optional = false)
    private Categoria categoriaID;
        
    @JoinColumn(name = "UsuarioID", referencedColumnName = "UsuarioID")
    @ManyToOne(optional = false)
    private Usuario usuarioID;
    
    @JoinColumn(name = "OportunidadID", referencedColumnName = "OportunidadID")
    @ManyToOne
    private OportunidadDeAprendizaje oportunidadID;
    
    public FortalezaDeConocimiento() {
    }

    public FortalezaDeConocimiento(Integer fortalezaID) {
        this.fortalezaID = fortalezaID;
    }

    public FortalezaDeConocimiento(Integer fortalezaID, String nombre, String descripcion, boolean estado) {
        this.fortalezaID = fortalezaID;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public Integer getFortalezaID() {
        return fortalezaID;
    }

    public void setFortalezaID(Integer fortalezaID) {
        this.fortalezaID = fortalezaID;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Categoria getCategoriaID() {
        return categoriaID;
    }

    public void setCategoriaID(Categoria categoriaID) {
        this.categoriaID = categoriaID;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fortalezaID != null ? fortalezaID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FortalezaDeConocimiento)) {
            return false;
        }
        FortalezaDeConocimiento other = (FortalezaDeConocimiento) object;
        if ((this.fortalezaID == null && other.fortalezaID != null) || (this.fortalezaID != null && !this.fortalezaID.equals(other.fortalezaID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "\nFortalezaID: " + fortalezaID ;
    }
    
}
