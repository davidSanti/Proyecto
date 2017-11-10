/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.ErrorFrecuente;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Santi
 */
@Local
public interface ErrorFrecuenteFacadeLocal {

    void create(ErrorFrecuente errorFrecuente);

    void edit(ErrorFrecuente errorFrecuente);

    void remove(ErrorFrecuente errorFrecuente);

    ErrorFrecuente find(Object id);

    List<ErrorFrecuente> findAll();

    List<ErrorFrecuente> findRange(int[] range);

    int count();
    List<ErrorFrecuente> listar();
    
}
