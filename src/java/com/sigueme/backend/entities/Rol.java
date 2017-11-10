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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "Roles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rol.findAll", query = "SELECT r FROM Rol r WHERE r.rolID != 4")
    , @NamedQuery(name = "Rol.findByRolID", query = "SELECT r FROM Rol r WHERE r.rolID = :rolID")
    , @NamedQuery(name = "Rol.findByDescripcion", query = "SELECT r FROM Rol r WHERE r.descripcion = :descripcion")
    , @NamedQuery(name = "Rol.findByEstado", query = "SELECT r FROM Rol r WHERE r.estado = :estado")})
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "RolID")
    private Integer rolID;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Descripcion")
    private String descripcion;
    
    @Column(name = "Estado", insertable = false, updatable = true)
    private Boolean estado;
        
    @JoinTable(name = "PermisosRol", joinColumns = {
        @JoinColumn(name = "RolID", referencedColumnName = "RolID")}, inverseJoinColumns = {
        @JoinColumn(name = "PermisoID", referencedColumnName = "PermisoID")})
    @ManyToMany
    private List<Permiso> permisosRol;
    
    @ManyToMany(mappedBy = "rolesUsuario")
    private List<Usuario> usuariosRol;
    
   
    public Rol() {
    }

    public Rol(Integer rolID) {
        this.rolID = rolID;
    }

    public Rol(Integer rolID, String descripcion) {
        this.rolID = rolID;
        this.descripcion = descripcion;
    }

    public Integer getRolID() {
        return rolID;
    }

    public void setRolID(Integer rolID) {
        this.rolID = rolID;
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

    public List<Permiso> getPermisosRol() {
        return permisosRol;
    }

    public void setPermisosRol(List<Permiso> permisosRol) {
        this.permisosRol = permisosRol;
    }

    public List<Usuario> getUsuariosRol() {
        return usuariosRol;
    }

    public void setUsuariosRol(List<Usuario> usuariosRol) {
        this.usuariosRol = usuariosRol;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rolID != null ? rolID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rol)) {
            return false;
        }
        Rol other = (Rol) object;
        if ((this.rolID == null && other.rolID != null) || (this.rolID != null && !this.rolID.equals(other.rolID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sigueme.backend.entities.Rol[ rolID=" + rolID + " ]";
    }
    
}
