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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "Usuarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u WHERE u.estado = true")
    , @NamedQuery(name = "Usuario.findByUsuarioID", query = "SELECT u FROM Usuario u WHERE u.usuarioID = :usuarioID")
    , @NamedQuery(name = "Usuario.findByCedula", query = "SELECT u FROM Usuario u WHERE u.cedula = :cedula ")
    , @NamedQuery(name = "Usuario.findByNombre", query = "SELECT u FROM Usuario u WHERE u.nombre = :nombre")
    , @NamedQuery(name = "Usuario.findByApellido", query = "SELECT u FROM Usuario u WHERE u.apellido = :apellido")
    , @NamedQuery(name = "Usuario.findByClave", query = "SELECT u FROM Usuario u WHERE u.clave = :clave")
    , @NamedQuery(name = "Usuario.findByEstado", query = "SELECT u FROM Usuario u WHERE u.estado = :estado")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "UsuarioID")
    private Integer usuarioID;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "Cedula")
    private String cedula;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "Nombre")
    private String nombre;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "Apellido")
    private String apellido;
    
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(max = 35)
    @Column(name = "Email")
    private String email;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "Clave", insertable = false)
    private String clave;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "Estado", insertable = false, updatable = true)
    private boolean estado;
    
    @JoinTable(name = "UsuariosRol", joinColumns = {
        @JoinColumn(name = "UsuarioID", referencedColumnName = "UsuarioID")}, inverseJoinColumns = {
        @JoinColumn(name = "RolID", referencedColumnName = "RolID")})
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Rol> rolesUsuario;
 
    public Usuario() {
    }

    public Usuario(Integer usuarioID) {
        this.usuarioID = usuarioID;
    }

    public Usuario(Integer usuarioID, String cedula, String nombre, String apellido,
                   String email, String clave ) {
        this.usuarioID = usuarioID;
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.clave = clave;
        this.email = email;
    }

    public Integer getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(Integer usuarioID) {
        this.usuarioID = usuarioID;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<Rol> getRolesUsuario() {
        return rolesUsuario;
    }

    public void setRolesUsuario(List<Rol> rolesUsuario) {
        this.rolesUsuario = rolesUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuarioID != null ? usuarioID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.usuarioID == null && other.usuarioID != null) || (this.usuarioID != null && !this.usuarioID.equals(other.usuarioID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "\nUsuarioID: " + usuarioID +
                "Nombre: " + nombre +
                "Apellido: " + apellido;
    }
    
}
