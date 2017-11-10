/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.model;

import com.sigueme.backend.entities.Categoria;
import com.sigueme.backend.entities.Incidencia;
import com.sigueme.backend.entities.IncidenciaAgenteReporte;
import com.sigueme.backend.entities.Usuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Santi
 */
@Local
public interface IncidenciaFacadeLocal {

    void create(Incidencia incidencia);

    void edit(Incidencia incidencia);

    void remove(Incidencia incidencia);

    Incidencia find(Object id);

    List<Incidencia> findAll();

    List<Incidencia> findRange(int[] range);

    int count();

    void editarEstado(Incidencia inci);

    List<Incidencia> listar();

    List<Incidencia> listarIncidenciasPropias(Usuario usuario);

    List<Object[]> generarReporteIncidenciasTotalAgente();

    List<Object[]> generarReporteIncidenciasPorCategoria();
    
    List<Incidencia> listarMismoCasoAgente(Incidencia incidencia);
    List<Incidencia> listarMismoCasoCategoria(Incidencia incidencia);
    
    void inactivarIncidenciasDeUsuarioEliminado(Usuario usuario);
    void listarIncidenciasParaOportunidad();
    
    //optinal
    List<Incidencia> listarIncidenciasParaOportunidades();
    
    //sugerir oportunidades
    List<Incidencia> listarIncidenciasUsuariosPorCategoria(Usuario usuario, Categoria categoria);
    List<IncidenciaAgenteReporte> incidenciaAgenteReporte();
}
