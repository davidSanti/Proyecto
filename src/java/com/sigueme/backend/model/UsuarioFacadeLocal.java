/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Usuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Santi
 */
@Local
public interface UsuarioFacadeLocal {

    void create(Usuario usuario);

    void edit(Usuario usuario);

    void remove(Usuario usuario);

    Usuario find(Object id);

    List<Usuario> findAll();

    List<Usuario> findRange(int[] range);

    int count();
    
    Usuario iniciarSesion(Usuario us);
    
    void editarEstado(Usuario us);
    
    List<Usuario> listar();
    
    List<Usuario> listarAgentes();
    
    List<Usuario> verificarDocumento(String cedula);
   
    List<Usuario> verificarExistenciaLider();
    
    List<Usuario> verificarDatosParaRecuperarContrasena(Usuario usuario);
  
}
