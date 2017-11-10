/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.frontend.controller;

import com.google.gson.Gson;
import com.sigueme.backend.entities.Chart;
import com.sigueme.backend.entities.Usuario;
import com.sigueme.backend.model.IncidenciaFacadeLocal;
import com.sigueme.backend.model.UsuarioFacadeLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Conest
 */
@Named(value = "graficasController")
@ViewScoped
public class GraficasController implements Serializable {

    private static final long serialVersionUID = 3455105191829156293L;

    @EJB
    private IncidenciaFacadeLocal ifl;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    private String dataStr;
    private String dataPie;
    private String dataLine;
    private List<Chart> dataList;
    private List<Chart> dataListPie;
    public GraficasController() {
    }

    @PostConstruct
    public void init() {
        dataListPie = new ArrayList();
        dataList = new ArrayList();
        List<Object[]> lista = this.ifl.generarReporteIncidenciasPorCategoria();
        for (Object[] ob : lista) {
            Random r = new Random();
            Chart gt = new Chart((String) ob[0], (int) ob[1], generateColor(r));
            dataList.add(gt);
        }
        
        List<Object[]> pie = this.ifl.generarReporteIncidenciasTotalAgente();
        for (Object[] in : pie){
            Random r = new Random();
            Chart gt = new Chart(mostrarNombreAgente((int) in[0]), (int) in[1], generateColor(r));
            dataListPie.add(gt);
        }
        
        dataStr = new Gson().toJson(dataList);
        dataPie = new Gson().toJson(dataListPie);
    }
    
    private static String generateColor(Random r) {
        final char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] s = new char[7];
        int n = r.nextInt(0x1000000);
        s[0] = '#';
        for (int i = 1; i < 7; i++) {
            s[i] = hex[n & 0xf];
            n >>= 4;
        }
        return new String(s);
    }

    private String mostrarNombreAgente(int id) {
        Usuario user = new Usuario();
        user = this.usuarioFacadeLocal.find(id);
        String[] nombres = user.getNombre().split(" ");
        String[] apellidos = user.getApellido().split(" ");
        return nombres[0] + " " + apellidos[0];
    }

    public String getDataStr() {
        return dataStr;
    }

    public void setDataStr(String dataStr) {
        this.dataStr = dataStr;
    }

    public List<Chart> getDataList() {
        return dataList;
    }

    public void setDataList(List<Chart> dataList) {
        this.dataList = dataList;
    }

    public List<Chart> getDataListPie() {
        return dataListPie;
    }

    public void setDataListPie(List<Chart> dataListPie) {
        this.dataListPie = dataListPie;
    }

    public String getDataPie() {
        return dataPie;
    }

    public void setDataPie(String dataPie) {
        this.dataPie = dataPie;
    }

    public String getDataLine() {
        return dataLine;
    }

    public void setDataLine(String dataLine) {
        this.dataLine = dataLine;
    }
    
    
}
