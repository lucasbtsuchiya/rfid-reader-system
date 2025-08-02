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
public class RfidTableModel extends AbstractTableModel
{    
    private List<Rfid> dados = new ArrayList<>();
    private String[] colunas = {"Id","UID_TAG","Nome","Matricula","Curso"};
    
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
                return dados.get(linha).getId();
            case 1: 
                return dados.get(linha).getUid_tag();
            case 2: 
                return dados.get(linha).getNome();
            case 3:
                return dados.get(linha).getMatricula();
            case 4:
                return dados.get(linha).getCurso();
        }
        
        return null;
    }
    
    public void addRow(Rfid r){
        this.dados.add(r);
        this.fireTableDataChanged();
    }
    
}
