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
@Table(name = "Categorias")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Categoria.findAll", query = "SELECT c FROM Categoria c WHERE c.estado = true")
    , @NamedQuery(name = "Categoria.findByCategoriaID", query = "SELECT c FROM Categoria c WHERE c.categoriaID = :categoriaID")
    , @NamedQuery(name = "Categoria.findByDescripcion", query = "SELECT c FROM Categoria c WHERE c.descripcion = :descripcion")
    , @NamedQuery(name = "Categoria.findByEstado", query = "SELECT c FROM Categoria c WHERE c.estado = :estado")})
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "CategoriaID")
    private Integer categoriaID;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Descripcion")
    private String descripcion;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "Estado", insertable = false)
    private boolean estado;
   
    public Categoria() {
    }

    public Categoria(Integer categoriaID) {
        this.categoriaID = categoriaID;
    }

    public Categoria(Integer categoriaID, String descripcion) {
        this.categoriaID = categoriaID;
        this.descripcion = descripcion;
    }

    public Integer getCategoriaID() {
        return categoriaID;
    }

    public void setCategoriaID(Integer categoriaID) {
        this.categoriaID = categoriaID;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (categoriaID != null ? categoriaID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Categoria)) {
            return false;
        }
        Categoria other = (Categoria) object;
        if ((this.categoriaID == null && other.categoriaID != null) || (this.categoriaID != null && !this.categoriaID.equals(other.categoriaID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "\nCategoriaID: " + categoriaID +
                "Descripción: " + descripcion;
    }
    
}
