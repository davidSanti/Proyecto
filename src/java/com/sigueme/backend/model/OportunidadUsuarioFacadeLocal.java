/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.OportunidadDeAprendizaje;
import com.sigueme.backend.entities.OportunidadUsuario;
import com.sigueme.backend.entities.Usuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Santi
 */
@Local
public interface OportunidadUsuarioFacadeLocal {

    void create(OportunidadUsuario oportunidadUsuario);

    void edit(OportunidadUsuario oportunidadUsuario);

    void remove(OportunidadUsuario oportunidadUsuario);

    OportunidadUsuario find(Object id);

    List<OportunidadUsuario> findAll();

    List<OportunidadUsuario> findRange(int[] range);

    int count();
    
    void editarOportunidadUsuario(OportunidadUsuario algo);
    
    void inactivarOportunidadUsuario(OportunidadUsuario oportunidad);
    
    void eliminarOportunidadUsuario();
    
    public List<OportunidadUsuario> listarOportunidadesUsuario();
    
    void inactivarOportunidadesUsuarioAlEliminarUsuario(OportunidadUsuario oportunidadUsuario);
    
    List<OportunidadUsuario> listarOportunidadesUsuarioPorOportunidad(OportunidadDeAprendizaje oportunidad);
    
}
