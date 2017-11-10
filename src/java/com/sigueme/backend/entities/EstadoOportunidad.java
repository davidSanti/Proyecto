/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Santi
 */
@Entity
@Table(name = "EstadosOportunidad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoOportunidad.findAll", query = "SELECT e FROM EstadoOportunidad e WHERE e.estadoID NOT IN (1,4,6)")
    , @NamedQuery(name = "EstadoOportunidad.findByEstadoID", query = "SELECT e FROM EstadoOportunidad e WHERE e.estadoID = :estadoID")
    , @NamedQuery(name = "EstadoOportunidad.findByDescripcion", query = "SELECT e FROM EstadoOportunidad e WHERE e.descripcion = :descripcion")})
public class EstadoOportunidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "EstadoID")
    private Integer estadoID;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Descripcion")
    private String descripcion;
   
    public EstadoOportunidad() {
    }

    public EstadoOportunidad(Integer estadoID) {
        this.estadoID = estadoID;
    }

    public EstadoOportunidad(Integer estadoID, String descripcion) {
        this.estadoID = estadoID;
        this.descripcion = descripcion;
    }

    public Integer getEstadoID() {
        return estadoID;
    }

    public void setEstadoID(Integer estadoID) {
        this.estadoID = estadoID;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estadoID != null ? estadoID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadoOportunidad)) {
            return false;
        }
        EstadoOportunidad other = (EstadoOportunidad) object;
        if ((this.estadoID == null && other.estadoID != null) || (this.estadoID != null && !this.estadoID.equals(other.estadoID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sigueme.backend.entities.EstadoOportunidad[ estadoID=" + estadoID + " ]";
    }
    
}
