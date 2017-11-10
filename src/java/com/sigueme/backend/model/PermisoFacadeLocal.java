/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Permiso;
import com.sigueme.backend.entities.Rol;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Santi
 */
@Local
public interface PermisoFacadeLocal {

    void create(Permiso permiso);

    void edit(Permiso permiso);

    void remove(Permiso permiso);

    Permiso find(Object id);

    List<Permiso> findAll();

    List<Permiso> findRange(int[] range);

    int count();
    
    List<Permiso> listarPermisos();
    
    void eliminarPermisosRol(Permiso permiso);
    
    List<Permiso> permisosPorRol(Rol r);
}
