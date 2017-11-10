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
import javax.persistence.CascadeType;
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
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToMany;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
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
@Table(name = "OportunidadesDeAprendizaje")
@XmlRootElement

@NamedQueries({
    
    @NamedQuery(name = "OportunidadDeAprendizaje.findAll", query = "SELECT o FROM OportunidadDeAprendizaje o WHERE o.estado = true ")
    , @NamedQuery(name = "OportunidadDeAprendizaje.findByOportunidadID", query = "SELECT o FROM OportunidadDeAprendizaje o WHERE o.oportunidadID = :oportunidadID")
    , @NamedQuery(name = "OportunidadDeAprendizaje.findByNombre", query = "SELECT o FROM OportunidadDeAprendizaje o WHERE o.nombre = :nombre")
    , @NamedQuery(name = "OportunidadDeAprendizaje.findByFechaInicio", query = "SELECT o FROM OportunidadDeAprendizaje o WHERE o.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "OportunidadDeAprendizaje.findByFechaFin", query = "SELECT o FROM OportunidadDeAprendizaje o WHERE o.fechaFin = :fechaFin")
})

@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name="procedimientoDiasFaltantes",resultClasses = String.class
                          ,procedureName = "diasFaltantes",parameters = {
                          @StoredProcedureParameter(mode = ParameterMode.IN,name = "idUsuario",type = Integer.class),
                          @StoredProcedureParameter(mode = ParameterMode.IN,name = "idoportunidad",type = Integer.class),
                          @StoredProcedureParameter(mode = ParameterMode.OUT,name = "salida",type = Integer.class)}),
@NamedStoredProcedureQuery(name="procedimiento",
        resultClasses = String.class,
        procedureName = "sugerirOportunidad",
        parameters = {@StoredProcedureParameter(mode = ParameterMode.OUT,name = "salida", type = String.class)}),
@NamedStoredProcedureQuery(name = "inhabilitarOportunidadDeAprendizaje",
        resultClasses = String.class,
        procedureName = "inhabilitarOportunidad")
})

public class OportunidadDeAprendizaje implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "OportunidadID", insertable = false)
    private Integer oportunidadID;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Nombre")
    private String nombre;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "FechaInicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "FechaFin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "Descripcion")
    private String descripcion;
    
    @JoinColumn(name = "TipoOportunidadID", referencedColumnName = "TipoID")
    @ManyToOne(optional = false)
    private TipoDeOportunidad tipoOportunidadID;
        
    @OneToMany(mappedBy = "oportunidadID", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OportunidadUsuario> oportunidadesUsuarios;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "Estado", insertable = false, updatable = true)
    private boolean estado;
    
    
    public OportunidadDeAprendizaje() {
    }

    public OportunidadDeAprendizaje(Integer oportunidadID) {
        this.oportunidadID = oportunidadID;
    }

    public OportunidadDeAprendizaje(Integer oportunidadID, String nombre, 
                                    Date fechaInicio, Date fechaFin,
                                    String descripcion, boolean estado)
    {
        this.oportunidadID = oportunidadID;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public Integer getOportunidadID() {
        return oportunidadID;
    }

    public void setOportunidadID(Integer oportunidadID) {
        this.oportunidadID = oportunidadID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoDeOportunidad getTipoOportunidadID() {
        return tipoOportunidadID;
    }

    public void setTipoOportunidadID(TipoDeOportunidad tipoOportunidadID) {
        this.tipoOportunidadID = tipoOportunidadID;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<OportunidadUsuario> getOportunidadesUsuarios() {
        return oportunidadesUsuarios;
    }

    public void setOportunidadesUsuarios(List<OportunidadUsuario> oportunidadesUsuarios) {
        this.oportunidadesUsuarios = oportunidadesUsuarios;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (oportunidadID != null ? oportunidadID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OportunidadDeAprendizaje)) {
            return false;
        }
        OportunidadDeAprendizaje other = (OportunidadDeAprendizaje) object;
        if ((this.oportunidadID == null && other.oportunidadID != null) || (this.oportunidadID != null && !this.oportunidadID.equals(other.oportunidadID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "\nOportunidadID: " + oportunidadID + 
                "Descripción: " + descripcion;
    }
    
}
