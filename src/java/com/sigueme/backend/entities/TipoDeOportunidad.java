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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author Santi
 */
@Entity
@Table(name = "TiposDeOportunidad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoDeOportunidad.findAll", query = "SELECT t FROM TipoDeOportunidad t")
    , @NamedQuery(name = "TipoDeOportunidad.findByTipoID", query = "SELECT t FROM TipoDeOportunidad t WHERE t.tipoID = :tipoID")
    , @NamedQuery(name = "TipoDeOportunidad.findByDescripcion", query = "SELECT t FROM TipoDeOportunidad t WHERE t.descripcion = :descripcion")})
public class TipoDeOportunidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "TipoID")
    private Integer tipoID;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "Descripcion")
    private String descripcion;
    
    public TipoDeOportunidad() {
    }

    public TipoDeOportunidad(Integer tipoID) {
        this.tipoID = tipoID;
    }

    public TipoDeOportunidad(Integer tipoID, String descripcion) {
        this.tipoID = tipoID;
        this.descripcion = descripcion;
    }

    public Integer getTipoID() {
        return tipoID;
    }

    public void setTipoID(Integer tipoID) {
        this.tipoID = tipoID;
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
        hash += (tipoID != null ? tipoID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoDeOportunidad)) {
            return false;
        }
        TipoDeOportunidad other = (TipoDeOportunidad) object;
        if ((this.tipoID == null && other.tipoID != null) || (this.tipoID != null && !this.tipoID.equals(other.tipoID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "\nTipoID: " + tipoID +
                "\nDescripción: " + descripcion;
    }
    
}
