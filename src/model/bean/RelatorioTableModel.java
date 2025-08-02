/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bean;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Lucas B Tsuchiya
 */
public class RelatorioTableModel extends AbstractTableModel {
    private List<Relatorio> dados = new ArrayList<>();
    private String[] colunas = {"Nome", "Matricula","Evento", "Data", "Responsável", "Curso"};
    
    @Override 
    public String getColumnName(int column){
        return colunas[column];
    }
    

    @Override
    public int getRowCount() {
        return dados.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        switch(coluna){
            case 0: 
                return dados.get(linha).getNome_aluno();
            case 1: 
                return dados.get(linha).getMatricula_aluno();
            case 2: 
                return dados.get(linha).getNome_evento();
            case 3:
                return dados.get(linha).getData_evento();
            case 4:
                return dados.get(linha).getResponsavel();
            case 5: 
                return dados.get(linha).getCurso_aluno();
        }
        
        return null;
    }
    
    public void addRow(Relatorio r){
        this.dados.add(r);
        this.fireTableDataChanged();
    }
    
}
