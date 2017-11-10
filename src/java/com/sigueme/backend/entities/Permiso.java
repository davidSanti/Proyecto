/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "Permisos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Permiso.findAll", query = "SELECT p FROM Permiso p WHERE p.estado = true")
    , @NamedQuery(name = "Permiso.findByPermisoID", query = "SELECT p FROM Permiso p WHERE p.permisoID = :permisoID")
    , @NamedQuery(name = "Permiso.findByDescripcion", query = "SELECT p FROM Permiso p WHERE p.descripcion = :descripcion")
    , @NamedQuery(name = "Permiso.findByTipo", query = "SELECT p FROM Permiso p WHERE p.tipo = :tipo")
    , @NamedQuery(name = "Permiso.findByUrl", query = "SELECT p FROM Permiso p WHERE p.url = :url")
    , @NamedQuery(name = "Permiso.findByEstado", query = "SELECT p FROM Permiso p WHERE p.estado = :estado")})
public class Permiso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "PermisoID")
    private Integer permisoID;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "Descripcion")
    private String descripcion;
    
    @Column(name = "Tipo")
    private Boolean tipo;
    
    @Size(max = 100)
    @Column(name = "Url")
    private String url;
    
    @Column(name = "Estado", insertable = false)
    private Boolean estado;
    
    @ManyToMany(mappedBy = "permisosRol")
    private List<Rol> rolesPermisos;
    
    @OneToMany(mappedBy = "dependencia")
    private List<Permiso> permisoList;
    
    @JoinColumn(name = "Dependencia", referencedColumnName = "PermisoID")
    @ManyToOne
    private Permiso dependencia;

    public Permiso() {
    }

    public Permiso(Integer permisoID) {
        this.permisoID = permisoID;
    }

    public Permiso(Integer permisoID, String descripcion) {
        this.permisoID = permisoID;
        this.descripcion = descripcion;
    }

    public Integer getPermisoID() {
        return permisoID;
    }

    public void setPermisoID(Integer permisoID) {
        this.permisoID = permisoID;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getTipo() {
        return tipo;
    }

    public void setTipo(Boolean tipo) {
        this.tipo = tipo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public List<Rol> getRolesPermisos() {
        return rolesPermisos;
    }

    public void setRolesPermisos(List<Rol> rolesPermisos) {
        this.rolesPermisos = rolesPermisos;
    }

    @XmlTransient
    public List<Permiso> getPermisoList() {
        return permisoList;
    }

    public void setPermisoList(List<Permiso> permisoList) {
        this.permisoList = permisoList;
    }

    public Permiso getDependencia() {
        return dependencia;
    }

    public void setDependencia(Permiso dependencia) {
        this.dependencia = dependencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (permisoID != null ? permisoID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permiso)) {
            return false;
        }
        Permiso other = (Permiso) object;
        if ((this.permisoID == null && other.permisoID != null) || (this.permisoID != null && !this.permisoID.equals(other.permisoID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sigueme.backend.entities.Permiso[ permisoID=" + permisoID + " ]";
    }
    
}
